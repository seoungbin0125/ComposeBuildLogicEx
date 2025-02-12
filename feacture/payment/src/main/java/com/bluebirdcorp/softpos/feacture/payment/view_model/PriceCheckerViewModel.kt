package com.bluebirdcorp.softpos.feacture.payment.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluebirdcorp.softpos.common.utils.debug
import com.bluebirdcorp.softpos.domain.usecase.BarcodeDatabaseUsecase
import com.bluebirdcorp.softpos.domain.usecase.BarcodeHandleUsecase
import com.bluebirdcorp.softpos.feacture.interfaces.BarcodeScanRepo
import com.bluebirdcorp.softpos.feacture.payment.model.BarcodeUiModel
import com.bluebirdcorp.softpos.feacture.payment.model.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PriceCheckerViewModel @Inject constructor(
    private val barcodeDatabaseUsecase: BarcodeDatabaseUsecase,
    private val barcodeHandleUsecase: BarcodeHandleUsecase,
    private val barcodeScanRepo: BarcodeScanRepo,
) : ViewModel() {

    private val _barcodeList = MutableLiveData<List<BarcodeUiModel>>()
    val barcodeList = _barcodeList

    fun startCollectBarcodeScan() {
        debug("collectBarcodeValue!")
        viewModelScope.launch(Dispatchers.IO) {
            barcodeScanRepo.getBarcodeScanFlow().throttleFirst(300).collect { barcodeId ->
                debug("barcodeId : $barcodeId")
                val barcodeUiModel = barcodeDatabaseUsecase.findItemByBarcodeId(barcodeId)?.toUi()
                barcodeUiModel?.let {
                    debug("barcodeUiModel : $barcodeUiModel")
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

    fun <T> Flow<T>.throttleFirst(windowDuration: Long): Flow<T> = flow {
        var lastEmissionTime = 0L
        collect { upstream ->
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastEmissionTime > windowDuration) {
                lastEmissionTime = currentTime
                emit(upstream)
            }
        }
    }
}