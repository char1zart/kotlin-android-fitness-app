package com.example.ch1zart.container

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.ch1zart.fitnessapp.R
import com.example.ch1zart.notes.NotesClass

/**
 * Created by Ch1zART on 22.05.2017.
 */
class ContainerAdapter(a:MutableList<ContainerClass>) : BaseQuickAdapter<ContainerClass, BaseViewHolder>(R.layout.recycler_container,a) {

    lateinit var cv: CardView
    lateinit var title: TextView
    lateinit var subtitle: TextView
    lateinit var detail: TextView
    lateinit var bool: ImageView

    override fun convert(viewHolder: BaseViewHolder, item: ContainerClass) {
        cv = viewHolder.getView(R.id.cv)
        title = viewHolder.getView(R.id.t_title)
        subtitle = viewHolder.getView(R.id.t_subtitle)
        detail = viewHolder.getView(R.id.t_detail)
        bool = viewHolder.getView(R.id.id_bool)

        title.text = item.title
        subtitle.text = item.subtitle
        detail.text = item.detail

        bool.setImageResource(item.photoId.toInt())
    }

}