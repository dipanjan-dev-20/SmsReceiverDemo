package com.android.smsdemo.ui

import android.content.Context
import android.view.View
import androidx.paging.PagingDataAdapter
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.IdUtils
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import kotlinx.coroutines.ObsoleteCoroutinesApi

interface SmsClickListener{
    fun smsClicked(sender:String)
}

@ObsoleteCoroutinesApi
class MainController(
    private val listener: SmsClickListener
) : PagingDataEpoxyController<SmsUiItem>(){




    override fun buildItemModel(currentPosition: Int, item: SmsUiItem?): EpoxyModel<*> {
        return if (item != null) {
            when (item) {
                is SmsUiItem.Item -> SmsItemModel_()
                    .id(IdUtils.hashString64Bit(item.sms.toString()))
                    .messageText(item.sms.data)
                    .senderText(item.sms.creator)
                    .clickListener { v: View? -> listener.smsClicked(item.sms.creator)}
                    .timeText("")
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