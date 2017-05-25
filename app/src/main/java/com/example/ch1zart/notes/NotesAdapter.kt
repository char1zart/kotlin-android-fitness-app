package com.example.ch1zart.notes

import com.chad.library.adapter.base.BaseQuickAdapter
import android.support.v7.widget.CardView
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.example.ch1zart.fitnessapp.R

class NotesAdapter(a:MutableList<NotesClass>) : BaseQuickAdapter<NotesClass, BaseViewHolder>(R.layout.recycler_notes,a) {

    lateinit var cv: CardView
    lateinit var positions: TextView
    lateinit var description: TextView
    lateinit var date: TextView


    override fun convert(viewHolder: BaseViewHolder, item: NotesClass) {
        cv = viewHolder.getView(R.id.cv1)
        positions = viewHolder.getView(R.id.t_notename)
        description = viewHolder.getView(R.id.t_discript_n)
        date = viewHolder.getView(R.id.t_date)

        positions.text = item.notename
        description.text = item.description
        date.text = item.date
    }
}