package com.example.ch1zart.newsfeed

import android.support.v7.widget.CardView
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.ch1zart.fitnessapp.R

/**
 * Created by Ch1zART on 24.05.2017.
 */
class NewsAdapter (a:MutableList<NewsClass>) : BaseQuickAdapter<NewsClass, BaseViewHolder>(R.layout.recycler_news,a) {

    override fun convert(helper: BaseViewHolder, item: NewsClass) {

        cv = helper.getView(R.id.cv1)
        title = helper.getView(R.id.id_pos)
        title.text = item.title
    }

    lateinit var cv: CardView
    lateinit var title: TextView
}