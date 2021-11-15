package com.android.smsdemo.ui

import com.android.smsdemo.data.Sms

sealed class SmsUiItem {
    class Item(
        val sms: Sms
    ):SmsUiItem()
    class Seperator(
        val text:String
    ):SmsUiItem()
}