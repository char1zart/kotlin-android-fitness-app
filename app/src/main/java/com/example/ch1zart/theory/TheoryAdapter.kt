package com.example.ch1zart.theory

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.ch1zart.fitnessapp.R
import com.example.ch1zart.notes.NotesClass

/**
 * Created by Ch1zART on 22.05.2017.
 */
class TheoryAdapter(a:MutableList<TheoryClass>) : BaseQuickAdapter<TheoryClass, BaseViewHolder>(R.layout.recycler_theory,a) {


    lateinit var cv: CardView
    lateinit var problem: TextView
    lateinit var title: TextView
    lateinit var howto: TextView

    override fun convert(viewHolder: BaseViewHolder, item: TheoryClass) {
        cv = viewHolder.getView(R.id.cv1)
        problem = viewHolder.getView(R.id.t_problem)
        title = viewHolder.getView(R.id.t_title)
        howto = viewHolder.getView(R.id.t_howTo)

        problem.text = item.problem
        title.text = item.title
        howto.text = item.howTo
    }
}