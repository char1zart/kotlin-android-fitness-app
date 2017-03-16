package com.example.ch1zart.findfriend

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ch1zart.fitnessapp.R
import org.jetbrains.anko.find

class FF_Adapter  internal constructor(internal var persons:

                                      List<FindFriend>) : RecyclerView.Adapter<FF_Adapter.StringViewHolder>() {

    class StringViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val cv: CardView
        internal val personName: TextView
        internal val personAge: TextView
        internal val cur_date: TextView

        init {
            cv = itemView.find<CardView>(R.id.revec)
            personName = itemView.find<TextView>(R.id.us_name)
            personAge = itemView.find<TextView>(R.id.us_mes)
            cur_date = itemView.find<TextView>(R.id.cur_date)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): StringViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.recycler_find, viewGroup, false)
        val pvh = StringViewHolder(v)

        return pvh
    }

    override fun onBindViewHolder(personViewHolder: StringViewHolder, i: Int) {

        personViewHolder.personName.text = persons[i].name
        personViewHolder.personAge.text = persons[i].message
        personViewHolder.cur_date.text = persons[i].date

    }

    override fun getItemCount(): Int {
        return persons.size
    }

}
