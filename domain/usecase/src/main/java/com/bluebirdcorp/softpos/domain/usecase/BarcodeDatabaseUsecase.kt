package com.bluebirdcorp.softpos.domain.usecase

import com.bluebirdcorp.softpos.domain.interfaces.BarcodeDBRepo
import com.bluebirdcorp.softpos.domain.item.BarcodeItem
import javax.inject.Inject

class BarcodeDatabaseUsecase @Inject constructor(
    private val barcodeDBRepo: BarcodeDBRepo<BarcodeItem>
) {
    suspend fun insertBarcodeItem(barcodeItem: BarcodeItem) {
        barcodeDBRepo.insertBarcodeItem(barcodeItem)
    }

    suspend fun deleteBarcodeItem() {
        barcodeDBRepo.deleteBarcodeItem()
    }

    suspend fun findItemByBarcodeId(barcodeId: Long): BarcodeItem? {
        // barcodeId로 barcode를 조회하는 로직
        return barcodeDBRepo.findItemByBarcodeId(barcodeId)
    }

    suspend fun getBarcodeItems(): List<BarcodeItem> {
        return barcodeDBRepo.getBarcodeItems()
    }
}