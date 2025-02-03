package com.bluebirdcorp.iba.feacture.payment.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluebirdcorp.iba.common.utils.debug
import com.bluebirdcorp.iba.domain.usecase.BarcodeDatabaseUsecase
import com.bluebirdcorp.iba.domain.usecase.BarcodeHandleUsecase
import com.bluebirdcorp.iba.feacture.interfaces.BarcodeScanRepo
import com.bluebirdcorp.iba.feacture.payment.model.BarcodeUiModel
import com.bluebirdcorp.iba.feacture.payment.model.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarcodePriceCheckerViewModel @Inject constructor(
    private val barcodeDatabaseUsecase: BarcodeDatabaseUsecase,
    private val barcodeHandleUsecase: BarcodeHandleUsecase,
    private val barcodeScanRepo: BarcodeScanRepo,
) : ViewModel() {

    private val _barcodeList = MutableLiveData<List<BarcodeUiModel>>()
    val barcodeList = _barcodeList

    init {
        collectBarcodeScan()
    }

    fun collectBarcodeScan() {
        debug("collectBarcodeValue!")
        viewModelScope.launch(Dispatchers.IO) {
            barcodeScanRepo.getBarcodeScanFlow().collect { barcodeId ->
                debug("barcodeId : $barcodeId")
                val barcodeUiModel = barcodeDatabaseUsecase.findItemByBarcodeId(barcodeId)?.toUi()
                barcodeUiModel?.let {
                    val currentList = _barcodeList.value?.toMutableList() ?: mutableListOf()
                    currentList.add(it)
                    _barcodeList.postValue(currentList)
                }
            }
        }
    }

    fun deleteItem(item: BarcodeUiModel) {
        viewModelScope.launch {
            val currentList = _barcodeList.value?.toMutableList() ?: mutableListOf()
            currentList.remove(item) // Use remove or removeAt depending on your needs
            _barcodeList.value = currentList
        }
    }
}