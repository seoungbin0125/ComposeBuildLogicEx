package com.bluebirdcorp.softpos.feacture.payment.mapper

import com.bluebirdcorp.softpos.domain.item.BarcodeItem
import com.bluebirdcorp.softpos.feacture.payment.model.BarcodeUiModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BarcodeItemMapper @Inject constructor(
) {
    fun map(model: BarcodeItem): BarcodeUiModel {
        return BarcodeUiModel(
            id = model.id,
            name = model.name,
            dollarPrice = model.dollarPrice,
            euroPrice = model.euroPrice,
        )
    }

    fun mapList(models: List<BarcodeItem>): List<BarcodeUiModel> {
        return models.map { map(it) }
    }
}
