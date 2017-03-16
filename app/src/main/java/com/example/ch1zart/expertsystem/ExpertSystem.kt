package com.example.ch1zart.expertsystem

import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.ch1zart.dbconnector.MyDatabaseOpenHelper
import com.example.ch1zart.dbconnector.StatistickTable
import com.example.ch1zart.fitnessapp.*
import com.example.ch1zart.fragments.TransferObject
import org.jetbrains.anko.*
import org.jetbrains.anko.db.IntParser
import org.jetbrains.anko.db.StringParser
import org.jetbrains.anko.db.select

class ExpertSystem : AppCompatActivity() {

    lateinit var mDbHelper: MyDatabaseOpenHelper
    lateinit var dbW: SQLiteDatabase
    val to = TransferObject

    var st = StatistickTable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expert_system)

        to.clearExpert()
//        check_stats()

       openGraphfragment()

    }

    fun openGraphfragment() {

        val fragment2 = first_question()
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container1,  fragment2).commit()
    }


    fun analysis_date():Boolean
    {
        var bcan = false
        doAsync {
            dbW = mDbHelper.writableDatabase

            var dates = dbW.select(st.table_name, st.date).parseList(StringParser)


            dbW.close()

            uiThread {
                if(dates.count() >= 10)
                {
                    toast("we can test you")
                    bcan = true
                }
                else
                {
                    bcan = false
                }
            }
        }
        return bcan
    }

    fun check_stats()
    {
        doAsync {
            dbW = mDbHelper.writableDatabase

            var distance = dbW.select(st.table_name, st.km).parseList(IntParser)

            // get_date_count = mystr.count()

            dbW.close()

            uiThread {
                if(distance.count() != 0)
                {
                var i = 0
                var sum = 0
                while(i != distance.count())
                {
                  sum +=  distance[i]
                }


            if(distance.count() < 5 && sum < 5000)
            {
                toast("Необходимо еще потрениться браток")
            }
            }
            }
        }
    }

   /* fun openHistoryfragment() {

        val fragment2 = PlusOneFragment()
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container,  fragment2).commit()
    }*/


}
