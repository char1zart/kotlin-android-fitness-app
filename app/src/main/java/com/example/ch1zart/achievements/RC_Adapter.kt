package com.example.ch1zart.achievements

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.example.ch1zart.fitnessapp.R


class RC_Adapter internal constructor(internal var persons:

                                      List<Timer>) : RecyclerView.Adapter<RC_Adapter.StringViewHolder>() {


    class StringViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var cv: CardView
        internal var personName: TextView
        internal var personAge: TextView
        internal var personPhoto: ImageView

        init {
            cv = itemView.findViewById(R.id.cv) as CardView
            personName = itemView.findViewById(R.id.id_p) as TextView
            personAge = itemView.findViewById(R.id.person_age) as TextView
            personPhoto = itemView.findViewById(R.id.person_photo) as ImageView
                  }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): StringViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.recycler_item, viewGroup, false)
        val pvh = StringViewHolder(v)

        return pvh
    }

    override fun onBindViewHolder(personViewHolder: StringViewHolder, i: Int) {

        personViewHolder.personName.text = persons[i].name
        personViewHolder.personAge.text = persons[i].desc
        personViewHolder.personPhoto.setImageResource(persons[i].photoId)

        if(persons[i]._status == "true")
        {
            personViewHolder.personPhoto.setImageResource(R.drawable.unlocked)
        }
    }

    override fun getItemCount(): Int {
        return persons.size
    }

}
