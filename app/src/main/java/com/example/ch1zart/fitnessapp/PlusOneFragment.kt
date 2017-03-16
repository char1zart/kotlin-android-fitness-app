package com.example.ch1zart.fitnessapp

import android.os.Bundle
import android.app.Fragment
import android.database.sqlite.SQLiteDatabase
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ch1zart.achievements.RecyclerItemClickListener
import com.example.ch1zart.dbconnector.MyDatabaseOpenHelper
import com.example.ch1zart.dbconnector.StatistickTable
import org.jetbrains.anko.*
import org.jetbrains.anko.db.FloatParser
import org.jetbrains.anko.db.IntParser
import org.jetbrains.anko.db.StringParser
import org.jetbrains.anko.db.select
import java.util.*

class PlusOneFragment : Fragment() {

    lateinit var mDbHelper: MyDatabaseOpenHelper
    lateinit var db: SQLiteDatabase
    private lateinit var statistick: MutableList<Statistick>
    var ind = 0
    private lateinit var rv: RecyclerView

    var st = StatistickTable()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_history, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()

        rv = find<RecyclerView>(R.id.recv)
        val llm = LinearLayoutManager(activity)
        rv.layoutManager = llm
        rv.setHasFixedSize(true)

        mDbHelper = MyDatabaseOpenHelper(ctx)

        rv.addOnItemTouchListener(
                RecyclerItemClickListener(ctx, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                                            }
                }))

            initializeData()
            initializeAdapter()
    }

    fun initializeData() {

      db = mDbHelper.writableDatabase

      val parse_date = db.select(st.table_name, st.date).parseList(StringParser)
      val parse_time = db.select(st.table_name, st.time).parseList(StringParser)
      val parse_kkl = db.select(st.table_name, st.kkal).parseList(StringParser)
      var get_km = db.select(st.table_name, st.km).parseList(IntParser)
      val parse_step = db.select(st.table_name, st.step).parseList(IntParser)

      db.close()

      statistick = ArrayList()
      ind = parse_date.count()-1
        if (ind > 0) {

           while (ind != 0) {

      statistick.add(Statistick(parse_date[ind] , parse_time[ind], parse_kkl[ind], get_km[ind], parse_step[ind]))
             ind--
          }
        }
    }

    private fun initializeAdapter() {
        val adapter = StatAdapter(statistick)
        rv.setAdapter(adapter)
    }
}