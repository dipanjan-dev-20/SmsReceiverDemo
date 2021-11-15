package com.android.smsdemo.ui

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.android.smsdemo.R

@EpoxyModelClass
abstract class DividerItemModel : EpoxyModelWithHolder<DividerItemModel.DividerItemViewHolder>() {

    @EpoxyAttribute
    lateinit var dividerText: String

    override fun getDefaultLayout(): Int = R.layout.divider_item


    override fun bind(holder: DividerItemViewHolder) {
        super.bind(holder)
        holder.dividerTextView.text = dividerText
    }

    class DividerItemViewHolder : KotlinEpoxyHolder() {
        val dividerTextView by bind<TextView>(R.id.divider_text)

    }
}