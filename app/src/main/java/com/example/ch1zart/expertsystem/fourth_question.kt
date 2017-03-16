package com.example.ch1zart.expertsystem

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
import com.example.ch1zart.fitnessapp.R
import com.example.ch1zart.fragments.ScreenSlidePagerActivity
import com.example.ch1zart.fragments.TransferObject
import org.jetbrains.anko.*
import org.jetbrains.anko.db.update
import java.text.SimpleDateFormat
import java.util.*


class fourth_question : Fragment() {

    lateinit var mDbHelper: MyDatabaseOpenHelper
    lateinit var dbW: SQLiteDatabase
    val to = TransferObject
    val ut = UserInfo()
    val qt = QuestTable()

    lateinit var t_Time:TextView
    lateinit var t_Course:TextView
    lateinit var t_Discript:TextView
    lateinit var t_How:TextView
    lateinit var t_accept:TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fourth_question, container, false)
        mDbHelper = MyDatabaseOpenHelper(activity)

        return view
    }


    override fun onStart() {
        super.onStart()

        t_Course = find<TextView>(R.id.t_course)
        t_Discript= find<TextView>(R.id.t_discript)
        t_How= find<TextView>(R.id.t_how)
        t_Time= find<TextView>(R.id.t_time)
        t_accept = find<TextView>(R.id.t_accept)

        t_accept.setOnClickListener {
            updateQuest()
            startActivity<ScreenSlidePagerActivity>()
        }

        sum_steps()
    }

    fun GetCurrentMonthText():String    {
        val c = Calendar.getInstance()

        val df = SimpleDateFormat("MMMM")

        val formattedDate = df.format(c.time)
        return formattedDate
    }

    fun createQuest()
    {
        doAsync {

            dbW = mDbHelper.writableDatabase


            dbW.update(qt.table_name, qt.data_start to GetCurrentMonthText(),
                    qt.finish to "0", qt.complexity to t_Course.text,
                    qt.t_how to t_How.text,
                    qt.t_time to t_Time.text).where(ut.id + "  = {userid}", "userid" to 1).exec()

            dbW.close()
        }
    }

    fun sum_steps()
    {

     val sum =  to.step_2.get()

    when(sum)
    {
        50 -> {
            easy_sport()}
        150 -> {
            normal_sport()}
        200 -> {
            hard_sport()}
    }

    }

    fun updateQuest()
    {
        createQuest()

        doAsync {

            dbW = mDbHelper.writableDatabase

            dbW.update(ut.table_name, ut.haveQuest to 1).where(ut.id + "  = {userid}", "userid" to 1).exec()

            dbW.close()
        }
    }

    fun easy_sport()
    {
        t_Course.text =  "Курс на 4 недели"
        t_Discript.text = "-Бегаем через один-два дня"
        t_How.text = "-Чередуем  бег трусцой и медленный шаг"
        t_Time.text =  "-Длительность тренировки 10-15 минут"
    }

    fun normal_sport()
    {
        t_Course.text =  "Курс на 6 недель"
        t_Discript.text = "-Бегаем через один день"
        t_How.text = "-Время от времени бег нужно ускорять, а с ускорения снова переходить на бег трусцой"
        t_Time.text =  "-Длительность тренировки 40 минут"
    }

    fun hard_sport()
    {
        t_Course.text =  "Курс на 8 недель"
        t_Discript.text = "-Бегаем через один-два дня"
        t_How.text = "-5:00 минут — бег трусцой в качестве разминки 0:50 минут — быстрый бег 1:30 минут — максимально медленный бег или шаг"
        t_Time.text =  "-Разминка и разогрев - 40 минут, а на остальное время ускоряемся"
    }
}
