package com.bluebirdcorp.softpos.feacture.payment.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluebirdcorp.softpos.common.utils.debug
import com.bluebirdcorp.softpos.domain.item.BarcodeItem
import com.bluebirdcorp.softpos.domain.usecase.BarcodeDatabaseUsecase
import com.bluebirdcorp.softpos.domain.usecase.BarcodeHandleUsecase
import com.bluebirdcorp.softpos.feacture.interfaces.BarcodeScanRepo
import com.bluebirdcorp.softpos.feacture.payment.model.BarcodeUiModel
import com.bluebirdcorp.softpos.feacture.payment.model.toDomain
import com.bluebirdcorp.softpos.feacture.payment.model.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BarcodeItemDBViewModel @Inject constructor(
    private val barcodeDatabaseUsecase: BarcodeDatabaseUsecase,
    private val barcodeHandleUsecase: BarcodeHandleUsecase,
    private val barcodeScanRepo: BarcodeScanRepo,
    private val barcodeItemMapper: com.bluebirdcorp.softpos.feacture.payment.mapper.BarcodeItemMapper
) : ViewModel() {

    private val _barcodeList = MutableLiveData<List<BarcodeUiModel>>()
    val barcodeList = _barcodeList

    private val _barcodeScanId = MutableLiveData<Long>()
    val barcodeScanId = _barcodeScanId

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val barcodeItems: List<BarcodeItem> = barcodeDatabaseUsecase.getBarcodeItems()
            withContext(Dispatchers.Main) {
                _barcodeList.value = barcodeItems.map { it.toUi() }
            }
        }

        collectBarcodeValue()
    }

    fun collectBarcodeValue() {
        debug("collectBarcodeValue!")
        viewModelScope.launch(Dispatchers.IO) {
            barcodeScanRepo.getBarcodeScanFlow().collect { barcodeId ->
                debug("barcodeId : $barcodeId")
                barcodeId.let {
                    _barcodeScanId.postValue(barcodeId)
                }
            }
        }
    }

    fun addOrUpdateBarcodeItem(barcodeItem: BarcodeUiModel) {
        val currentList = _barcodeList.value ?: emptyList()

        // 기존 리스트에서 일치하는 항목 찾기
        val updatedList = currentList.map { existingItem ->
            if (existingItem.id == barcodeItem.id) {
                // 기존 항목이 있으면 수정
                barcodeItem // 새로운 값으로 업데이트
            } else {
                existingItem // 변경 없으면 그대로 유지
            }
        }.toMutableList()

        // 기존 항목이 없으면 새 항목 추가
        if (updatedList.none { it.id == barcodeItem.id }) {
            updatedList.add(barcodeItem)
        }

        // 변경된 리스트를 LiveData에 반영
        _barcodeList.value = updatedList

        // 데이터베이스에 항목 추가/수정
        viewModelScope.launch(Dispatchers.IO) {
            barcodeDatabaseUsecase.insertBarcodeItem(barcodeItem.toDomain())
        }
    }

    fun deleteBarcode(barcode: BarcodeUiModel) {
        _barcodeList.value?.toMutableList()?.apply {
            remove(barcode)
            _barcodeList.value = this
        }
    }
}