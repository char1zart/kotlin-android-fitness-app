package com.example.ch1zart.position

import android.media.Image
import android.support.v7.widget.CardView
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.ch1zart.fitnessapp.R
import com.example.ch1zart.notes.NotesClass

/**
 * Created by Ch1zART on 23.05.2017.
 */
class PositionAdapter (a:MutableList<PositionClass>) : BaseQuickAdapter<PositionClass, BaseViewHolder>(R.layout.recycler_item,a) {

    override fun convert(helper: BaseViewHolder, item: PositionClass) {

        cv = helper.getView(R.id.cv)
        positions = helper.getView(R.id.id_pos)
        description = helper.getView(R.id.id_descript)
        bool = helper.getView(R.id.id_bool)

        positions.text = item.position
        description.text = item.description

        bool.setImageResource(item.photoId)

        if(item.bool == "true")
        {
            bool.setImageResource(R.drawable.unlocked)
        }
    }

    lateinit var cv: CardView
    lateinit var positions: TextView
    lateinit var description: TextView
    lateinit var bool: ImageView


}