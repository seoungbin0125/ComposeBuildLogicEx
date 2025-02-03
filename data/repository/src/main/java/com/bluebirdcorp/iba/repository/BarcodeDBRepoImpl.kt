package com.bluebirdcorp.iba.repository

import android.content.Context
import com.bluebirdcorp.iba.common.utils.debug
import com.bluebirdcorp.iba.data.model.toDomain
import com.bluebirdcorp.iba.data.model.toEntity
import com.bluebirdcorp.iba.datasource.dao.BarcodeDao
import com.bluebirdcorp.iba.domain.interfaces.BarcodeDBRepo
import com.bluebirdcorp.iba.domain.item.BarcodeItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BarcodeDBRepoImpl @Inject constructor(
    @ApplicationContext private val mContext: Context,
    private val barcodeDao: BarcodeDao,
    ) : BarcodeDBRepo<BarcodeItem>{
    override suspend fun deleteBarcodeItem() {
        TODO("Not yet implemented")
    }

    override suspend fun findItemByBarcodeId(barcodeId: Long): BarcodeItem? {
        return barcodeDao.findById(barcodeId)?.toDomain()
    }

    override suspend fun insertBarcodeItem(barcodeItem: BarcodeItem) {
        debug("insertBarcodeItem : ${barcodeItem}")
        barcodeDao.insertBarcode(barcodeItem.toEntity())
    }

    override suspend fun getBarcodeItems(): List<BarcodeItem> {
        return barcodeDao.getAll().map { it.toDomain() }
    }
}