package com.android.smsdemo.ui

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.android.smsdemo.R

@EpoxyModelClass
abstract class SmsDetailsItem : EpoxyModelWithHolder<SmsDetailsItem.SmsDetailsViewHolder>() {


    @EpoxyAttribute
    lateinit var messageText: String

    override fun getDefaultLayout(): Int = R.layout.sms_details_item


    override fun bind(holder: SmsDetailsViewHolder) {
        super.bind(holder)
        holder.messageText.text = messageText
    }

    class SmsDetailsViewHolder : KotlinEpoxyHolder() {

        val messageText by bind<TextView>(R.id.message_text)

    }
}