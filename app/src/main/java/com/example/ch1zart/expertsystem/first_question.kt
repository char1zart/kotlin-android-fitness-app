package com.example.ch1zart.expertsystem

import android.app.Fragment
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.ch1zart.dbconnector.MyDatabaseOpenHelper
import com.example.ch1zart.dbconnector.UserInfo
import com.example.ch1zart.fitnessapp.R
import com.example.ch1zart.fragments.TransferObject
import org.jetbrains.anko.db.IntParser
import org.jetbrains.anko.db.StringParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread


class first_question: Fragment() {


    val ut = UserInfo()
    lateinit var mDbHelper: MyDatabaseOpenHelper
    lateinit var dbW: SQLiteDatabase
    var need_slim = false
    lateinit var anim_text:TextView
    lateinit var t_v_1: TextView
    lateinit var t_v_2: TextView
    val to = TransferObject

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.first_question, container, false)
        mDbHelper = MyDatabaseOpenHelper(activity)
        return view
    }

   override fun onStart() {
        super.onStart()

       anim_text = find<TextView>(R.id.textView10)

       YoYo.with(Techniques.BounceInUp)
       .duration(10000)
       .playOn(anim_text)

//       t_v_2 = find<TextView>(R.id.t_var_2)
       t_v_1 = find<TextView>(R.id.t_var_1)


       YoYo.with(Techniques.BounceInLeft)
               .duration(5500)
               .playOn(t_v_1)

       need_it()

       t_v_1.setOnClickListener {

        if (need_slim) {
               next_window()
          }
         else {
               finish_window()
          }

       }

     /* t_v_2.setOnClickListener {
                  next_window()

       }*/
    }

    fun finish_window()
    {
        val fragment2 = fifth_question()
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container1,  fragment2).commit()
    }


   fun next_window()
    {
        val fragment2 = second_question()
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container1,  fragment2).commit()
    }

    fun need_it():Boolean {
        var formula_wt = 0f

        doAsync {
            dbW = mDbHelper.writableDatabase

            // var get_name = dbW.select(ut.table_name, ut.name).where(ut.id + "  = {userid}", "userid" to 1).parseSingle(StringParser)
           var get_age = dbW.select(ut.table_name, ut.age).where(ut.id + "  = {userid}", "userid" to 1).parseSingle(IntParser)
            var get_HT = dbW.select(ut.table_name, ut.height).where(ut.id + "  = {userid}", "userid" to 1).parseSingle(IntParser)
            var get_Sex = dbW.select(ut.table_name, ut.sex).where(ut.id + "  = {userid}", "userid" to 1).parseSingle(StringParser)
            var get_WT = dbW.select(ut.table_name, ut.weight).where(ut.id + "  = {userid}", "userid" to 1).parseSingle(IntParser)

            dbW.close()

           uiThread {

               formula_wt = 50 + 0.75f * (get_WT - 150) + (get_age - 16) / 4
               val needit_M = (get_HT - formula_wt).toFloat()
                val needit_W = (get_HT - (formula_wt - 10f)).toFloat()

               if ((get_Sex == "М" || get_Sex == "M"))
                {
                    need_slim(needit_M)
                }
                else {
                    need_slim(needit_W)
                }
            }
        }

        return need_slim
    }

    fun need_slim(needit: Float)
    {
        var need = false

        if(needit > 4)
        {
            to.tips.set("Можно чуть похудеть")
            need = true
        }

        if(needit < 0)
        {
            need = false
            to.tips.set("Худеть не нужно")
        }

        if(needit < 0 && needit < -3)
        {
            to.tips.set("Cоветую набрать вес")
            need = false
        }

        need_slim = need
    }

}