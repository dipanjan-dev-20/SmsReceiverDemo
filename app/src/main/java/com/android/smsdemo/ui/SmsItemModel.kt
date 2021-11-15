package com.android.smsdemo.ui

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.android.smsdemo.R

@EpoxyModelClass
abstract class SmsItemModel : EpoxyModelWithHolder<SmsItemModel.SmsItemViewHolder>() {

    @EpoxyAttribute
    lateinit var senderText: String

    @EpoxyAttribute
    lateinit var timeText: String

    @EpoxyAttribute
    lateinit var messageText: String

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var clickListener: View.OnClickListener

    override fun getDefaultLayout(): Int = R.layout.message_item_text


    override fun bind(holder: SmsItemViewHolder) {
        super.bind(holder)
        holder.senderName.text = senderText
        holder.timeText.text = timeText
        holder.messageText.text = messageText
        holder.cardContainer.setOnClickListener(clickListener)
    }

    class SmsItemViewHolder : KotlinEpoxyHolder() {
        val senderName by bind<TextView>(R.id.sender_name)
        val timeText by bind<TextView>(R.id.time_text)
        val messageText by bind<TextView>(R.id.message_text)
        val cardContainer by bind<CardView>(R.id.card_container)

    }
}