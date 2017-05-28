package com.example.ch1zart.another

import android.app.Application
import com.example.ch1zart.container.ContainerClass
import com.example.ch1zart.notes.NotesClass
import java.text.SimpleDateFormat
import java.util.*


class App : Application(){

    var reverse: MutableList<NotesClass>
    private val to = TransferObject

    init {
        reverse = ArrayList()
    }

    fun reversedCo(c: MutableList<NotesClass>): MutableList<NotesClass>
    {
        reverse = ArrayList()
        reverse.addAll(c.reversed())

        return reverse
    }

    fun getCurrentDate(t: Long):String    {
        val c = t
        val df = SimpleDateFormat("dd MMM HH:mm:ss")
        val formattedDate = df.format(c)
        return formattedDate
    }

    fun getCurrentTime() = (System.currentTimeMillis())

    fun regExpNote(find: String,where: NotesClass) {
        val regex = Regex("(?i)(|\\W|^)($find)(|\\W|$)*(|\\S)", RegexOption.IGNORE_CASE)

        regex.findAll(where.notename).let {
            val s = it.toMutableList()
            if (s.size > 0) {
                to.for_search.add(where)
            }
        }

        regex.findAll(where.description).let {
            val s = it.toMutableList()
            if (s.size > 0) {
                to.for_search.add(where)

            }
        }
    }

    fun regExpTheory(find: String,where: ContainerClass) {
        val regex = Regex("(?i)(|\\W|^)($find)(|\\W|$)*(|\\S)", RegexOption.IGNORE_CASE)

        val set = HashSet<ContainerClass>()

        regex.findAll(where.title).let {
            val s = it.toMutableList()
            if (s.size > 0) {
                set.add(where)
                //to.for_theory_state.add(where)
            }
        }

        regex.findAll(where.subtitle).let {
            val s = it.toMutableList()
            if (s.size > 0) {
                set.add(where)
                //to.for_theory_state.add(where)

            }
        }

        regex.findAll(where.detail).let {
            val s = it.toMutableList()
            if (s.size > 0) {
                set.add(where)
               // to.for_theory_state.add(where)
            }
        }

         for(i in set)
        {
            to.container_state.add(ContainerClass(where.title,where.subtitle,where.detail,where.photoId,where.id))
        }

        val arrObj = set.size
        to.status = arrObj
    }


}