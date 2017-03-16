
package com.example.ch1zart.fitnessapp

import android.os.Bundle
import android.app.Fragment
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ch1zart.dbconnector.MyDatabaseOpenHelper
import com.example.ch1zart.dbconnector.StatistickTable
import com.example.ch1zart.fragments.TransferObject
import im.dacer.androidcharts.LineView
import org.jetbrains.anko.*
import org.jetbrains.anko.db.IntParser
import org.jetbrains.anko.db.StringParser
import org.jetbrains.anko.db.select
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class HistoryTrainingFragment : Fragment() {

    lateinit var mDbHelper: MyDatabaseOpenHelper
    lateinit var dbW: SQLiteDatabase
    lateinit var lineView: LineView
    lateinit var Month_Text: TextView
    lateinit var min_text: TextView
    lateinit var max_text: TextView
    lateinit var all_text: TextView

    lateinit var dates_text: TextView

    var base = " "

    val dates: MutableList<String> = arrayListOf()

    var get_date_count = 0

    val count_training: MutableList<Int> = arrayListOf()

    var st = StatistickTable()

    val to = TransferObject

    lateinit  var t_km: TextView
    lateinit  var t_kkl:TextView
    lateinit  var t_step:TextView
    lateinit  var t_common:TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_graph, container, false)
        mDbHelper = MyDatabaseOpenHelper(activity)

       if(CanPrint()) {
           graphInfo()

           general_information()
       }

        return view
    }

    override  fun onStart() {
        super.onStart()

        Month_Text = find<TextView>(R.id.t_month)
        t_km =  find<TextView>(R.id.t_km)
        t_kkl =  find<TextView>(R.id.t_kkl)
        t_step =  find<TextView>(R.id.t_step)
        lineView = find<LineView>(R.id.linechart)
        t_common =  find<TextView>(R.id.t_common_info)

       dates_text = find<TextView>(R.id.t_dates)
       min_text = find<TextView>(R.id.t_min)
       max_text = find<TextView>(R.id.t_max)
       all_text = find<TextView>(R.id.t_all)

       Month_Text.text = "Новости " + GetCurrentMonthText()
       t_common.text = "Общий счёт"
    }

    fun GetCurrentMonthText():String    {
        val c = Calendar.getInstance()

        val df = SimpleDateFormat("MMMM")

        val formattedDate = df.format(c.time)
        return formattedDate
    }

    fun GetCurrentMonth():String    {
        val c = Calendar.getInstance()

        val df = SimpleDateFormat("M")
        val formattedDate = df.format(c.time)
        return formattedDate
    }

    fun checkWithRegExp(dateString: String): Boolean {

        val p = Pattern.compile("^.+${GetCurrentMonth()}$")
        val m = p.matcher(dateString)

        return m.matches()
    }

    fun CanPrint():Boolean {

        var b = false

            doAsync {
                dbW = mDbHelper.writableDatabase

                var mystr = dbW.select(st.table_name, st.date).parseList(StringParser)

                get_date_count = mystr.count()

                dbW.close()

                uiThread {
                    if(get_date_count > 1)
                    {
                        b = true
                    }
                }
            }
                return b
    }

    fun GetCountTrainingInCurMonth() {

        doAsync {
            dbW = mDbHelper.writableDatabase

           var mystr = dbW.select(st.table_name, st.date).parseList(StringParser)

            get_date_count = mystr.count()

            dbW.close()

            dates.clear()
            count_training.clear()
              uiThread {
                  var count = 1
                  var j = 0

                  if(checkWithRegExp("${mystr[1]}")) {
                     base = mystr[1]

                      dates.add(0, base)
                      count_training.add(0,count)
                      j++
                  }

                  for (i in 0..mystr.count()-1) {
                       if(checkWithRegExp("${mystr[i]}"))
                       {
                           if(base != mystr[i])
                           {
                               base = mystr[i]
                               dates.add(j,mystr[i])
                               count_training.add(j,count)
                               j++
                               count = 1
                           }

                           else  {
                                count++
                           }

                       }
                   }

                 init_graph_month()
           }
       }
   }

    fun init_graph_month()
    {
        if(!dates.isEmpty())
{
          val dataList = ArrayList<Int>()

    for (i in 0..count_training.size-1) {
          dataList.add(i, count_training[i])
      }

      val dataLists = ArrayList<ArrayList<Int>>()
      dataLists.add(dataList)

        val test = ArrayList<String>()
        for (i in 0..dates.size-1) {
            test.add(i, dates[i])
        }

        lineView.setColorArray(intArrayOf(Color.BLACK, Color.GREEN, Color.GRAY, Color.CYAN))

        lineView.setShowPopup(LineView.SHOW_POPUPS_MAXMIN_ONLY) //optional
        lineView.setBottomTextList(test)
        lineView.setDrawDotLine(true)
        lineView.setDataList(dataLists)
        }
        initTextInfo()
    }

    fun initTextInfo()
    {
        min_text.text ="Мин. количество тренировок в день: " + "${count_training.min()}"
        max_text.text ="Макс. количество тренировок в день: "+ "${count_training.max()}"

        var i = 0
        var size = 0

      while(i != count_training.count()-1)
        {
          size += count_training[++i]
        }
       all_text.text = "Общ. количество тренировок: "+ "${size}"
    }

fun graphInfo() {
    GetCountTrainingInCurMonth()
}

fun general_information()    {
   doAsync {
      dbW = mDbHelper.writableDatabase

     val get_step= dbW.select(st.table_name, st.step).where(st.id + "  = {userid}", "userid" to 1).parseSingle(IntParser)
     val get_kkl = dbW.select(st.table_name,st.kkal).where(st.id + "  = {userid}", "userid" to 1).parseSingle(IntParser)
     val get_km = dbW.select(st.table_name,st.km).where(st.id + "  = {userid}", "userid" to 1).parseSingle(IntParser)

      dbW.close()

  uiThread {
      t_kkl.text = "$get_kkl"
      t_km.text = "$get_km"
      t_step.text = "$get_step"
      dates_text.text = "$get_date_count"
  }
  }
}
}