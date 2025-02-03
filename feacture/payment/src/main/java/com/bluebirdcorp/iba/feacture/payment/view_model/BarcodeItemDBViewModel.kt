package com.bluebirdcorp.iba.feacture.payment.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluebirdcorp.iba.common.utils.debug
import com.bluebirdcorp.iba.domain.item.BarcodeItem
import com.bluebirdcorp.iba.domain.usecase.BarcodeDatabaseUsecase
import com.bluebirdcorp.iba.domain.usecase.BarcodeHandleUsecase
import com.bluebirdcorp.iba.feacture.interfaces.BarcodeScanRepo
import com.bluebirdcorp.iba.feacture.payment.mapper.BarcodeItemMapper
import com.bluebirdcorp.iba.feacture.payment.model.BarcodeUiModel
import com.bluebirdcorp.iba.feacture.payment.model.toDomain
import com.bluebirdcorp.iba.feacture.payment.model.toUi
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
    private val barcodeItemMapper: BarcodeItemMapper
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

    fun addBarcodeItem(barcodeItem: BarcodeUiModel) {
        val currentList = _barcodeList.value ?: emptyList()
        val updatedList = currentList + barcodeItem

        _barcodeList.value = updatedList
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