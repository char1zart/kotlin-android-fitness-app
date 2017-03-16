package com.example.ch1zart.fitnessapp

import android.content.DialogInterface
import android.graphics.Color
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.find
import org.w3c.dom.Text


class StatAdapter internal constructor(internal var story:

                                          List<Statistick>) : RecyclerView.Adapter<StatAdapter.StringViewHolder>() {

        class StringViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

             val cv: CardView
             val time: TextView
             val kkal: TextView
             val date: TextView
             val km:  TextView
             val step: TextView

            init {
                cv = itemView.find<CardView>(R.id.recv)
                time =itemView.find<TextView>(R.id.t_x_time)
                kkal = itemView.find<TextView>(R.id.text_kkl)
                date = itemView.find<TextView>(R.id.text_date)
                km = itemView.find<TextView>(R.id.text_km)
                step = itemView.find<TextView>(R.id.text_step)
            }
        }

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
            super.onAttachedToRecyclerView(recyclerView)
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): StringViewHolder {
            val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.recycler_item_stat, viewGroup, false)
            val pvh = StringViewHolder(v)

            return pvh
        }

        override fun onBindViewHolder(storyViewHolder: StringViewHolder, i: Int) {

            storyViewHolder.time.text = story[i].time
            storyViewHolder.kkal.text = "${story[i].kkal}"
            storyViewHolder.date.text = story[i].date
            storyViewHolder.km.text = "${story[i].km}"
            storyViewHolder.step.text = "${story[i].step}"
        }

        override fun getItemCount(): Int {
            return story.size
        }
}