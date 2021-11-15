package com.android.smsdemo.ui

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.IdUtils
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
class SmsDetailsController(
) : PagingDataEpoxyController<SmsUiItem>() {


    override fun buildItemModel(currentPosition: Int, item: SmsUiItem?): EpoxyModel<*> {
        return if (item != null) {
            when (item) {
                is SmsUiItem.Item -> SmsDetailsItem_()
                    .id(IdUtils.hashString64Bit(item.sms.toString()))
                    .messageText(item.sms.data)
                is SmsUiItem.Seperator -> DividerItemModel_()
                    .id(IdUtils.hashString64Bit(item.text))
                    .dividerText(item.text)
            }
        } else {
            DividerItemModel_()
                .id(IdUtils.hashString64Bit("placeholder"))
                .dividerText("placeholder")
        }
    }
}