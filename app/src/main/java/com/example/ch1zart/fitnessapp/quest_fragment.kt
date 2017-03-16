package com.example.ch1zart.fitnessapp

import android.app.Fragment
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ch1zart.dbconnector.MyDatabaseOpenHelper
import com.example.ch1zart.dbconnector.QuestTable
import com.example.ch1zart.dbconnector.UserInfo
import org.jetbrains.anko.db.IntParser
import org.jetbrains.anko.db.StringParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread


class quest_fragment: Fragment() {

    val ut = UserInfo()
    lateinit var mDbHelper: MyDatabaseOpenHelper
    lateinit var dbW: SQLiteDatabase
    val qt = QuestTable()
    lateinit var b_finish:TextView

    lateinit var t_course:TextView
    lateinit var t_how:TextView
    lateinit var t_data_start:TextView
    lateinit var t_time_training:TextView


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.quest_fragment, container, false)

        mDbHelper = MyDatabaseOpenHelper(activity)
        return view
    }

    override fun onStart() {
        super.onStart()
        b_finish = find<TextView>(R.id.t_finish_quest)
        b_finish.setOnClickListener { finish_course() }


        t_course = find<TextView>(R.id.t_time_course)
        t_data_start = find<TextView>(R.id.t_start)
        t_time_training = find<TextView>(R.id.t_time_course)
        t_how = find<TextView>(R.id.t_how)

        doAsync {

            dbW = mDbHelper.writableDatabase

            val have_quest =  dbW.select(ut.table_name, ut.haveQuest).where(qt.id + "  = {userid}", "userid" to 1).parseSingle(IntParser)

          dbW.close()
            uiThread {

           if(have_quest == 1)
            {
                get_info()
            }
                else {
               toast("На текущий момент, рекомендаций нет")
           }
        }
        }
    }

    fun get_info()    {
        b_finish.text = "Завершить курс"

        doAsync {
            dbW = mDbHelper.writableDatabase

       val data_start =  dbW.select(qt.table_name, qt.data_start).where(qt.id + "  = {userid}", "userid" to 1).parseSingle(StringParser)
       val complexity =  dbW.select(qt.table_name, qt.complexity).where(qt.id + "  = {userid}", "userid" to 1).parseSingle(StringParser)
       val time_training =  dbW.select(qt.table_name, qt.t_time).where(qt.id + "  = {userid}", "userid" to 1).parseSingle(StringParser)
       val how_how =  dbW.select(qt.table_name, qt.t_how).where(qt.id + "  = {userid}", "userid" to 1).parseSingle(StringParser)

            dbW.close()

            uiThread {
                t_course.text = data_start
                t_data_start.text = complexity
                t_time_training.text = time_training
                t_how.text = how_how
            }
        }
    }

    fun finish_course()
    {
        doAsync {
            dbW = mDbHelper.writableDatabase

            dbW.update(ut.table_name, ut.haveQuest to 0).where(ut.id + "  = {userid}", "userid" to 1).exec()

            dbW.update(qt.table_name, qt.finish to "0").where(qt.id + "  = {userid}", "userid" to 1).exec()

            dbW.close()

            uiThread { toast("Курс завершен!") }
        }
    }

}