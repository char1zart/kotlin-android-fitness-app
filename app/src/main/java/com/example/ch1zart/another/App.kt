package com.example.ch1zart.another

import android.app.Application
import com.example.ch1zart.notes.NotesClass
import com.example.ch1zart.theory.TheoryClass
import java.text.SimpleDateFormat
import java.util.*



/**
 * Created by Ch1zART on 18.05.2017.
 */
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
            var s = it.toMutableList()
            if (s.size > 0) {
                to.for_search.add(where)
            }
        }

        regex.findAll(where.description).let {
            var s = it.toMutableList()
            if (s.size > 0) {
                to.for_search.add(where)

            }
        }
    }


    fun regExpTheory(find: String,where: TheoryClass) {
        val regex = Regex("(?i)(|\\W|^)($find)(|\\W|$)*(|\\S)", RegexOption.IGNORE_CASE)

        val set = HashSet<TheoryClass>()

        regex.findAll(where.title).let {
            val s = it.toMutableList()
            if (s.size > 0) {
                set.add(where)
                //to.for_theory_state.add(where)
            }
        }

        regex.findAll(where.problem).let {
            val s = it.toMutableList()
            if (s.size > 0) {
                set.add(where)
                //to.for_theory_state.add(where)

            }
        }

        regex.findAll(where.howTo).let {
            val s = it.toMutableList()
            if (s.size > 0) {
                set.add(where)
               // to.for_theory_state.add(where)
            }
        }

         for(i in set)
        {
            to.for_theory_state.add(TheoryClass(where.problem,where.title,where.howTo,where.id))
        }

        val arrObj = set.size
        to.status = arrObj
    }
}