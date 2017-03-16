package com.example.ch1zart.fitnessapp

import android.app.ProgressDialog
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.example.ch1zart.dbconnector.MyDatabaseOpenHelper
import org.jetbrains.anko.db.update
import com.example.ch1zart.dbconnector.UserInfo
import com.example.ch1zart.fragments.ScreenSlidePagerActivity
import org.jetbrains.anko.*

class RegFragment : AppCompatActivity() {

    val TempStorage: MutableList<String> = arrayListOf()
    lateinit var actionBtn: FloatingActionButton

    lateinit var toolbar:Toolbar
    val ut = UserInfo()

    private lateinit var progress: ProgressDialog
    lateinit var NameText: TextView
    lateinit var AgeText: TextView
    lateinit var SexText:TextView
    lateinit var HText: TextView
    lateinit var WText: TextView
    lateinit var mDbHelper: MyDatabaseOpenHelper
    lateinit var dbW: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_fragment)

        progress = ProgressDialog(this)
        progress.setMessage("Ждем")
        progress.setCancelable(false)

        NameText = find<TextView>(R.id.t1)
        AgeText = find<TextView>(R.id.t2)
        SexText = find<TextView>(R.id.t3)
        HText = find<TextView>(R.id.t4)
        WText = find<TextView>(R.id.t5)

        mDbHelper = MyDatabaseOpenHelper(this)

        actionBtn = find<FloatingActionButton>(R.id.fab)

        actionBtn.setOnClickListener {
            MakeStorage()
        }
    }

    fun MakeStorage()
    {
        TempStorage.clear()
        TempStorage.add(0,"${NameText.text}")
        TempStorage.add(1,"${AgeText.text}")
        TempStorage.add(2,"${HText.text}")
        TempStorage.add(3,"${WText.text}")
        TempStorage.add(4,"${SexText.text}")
        progress.show()

        doAsync {
            dbW = mDbHelper.writableDatabase
            dbW.update (ut.table_name, ut.name to  TempStorage.get(0),
                    ut.age to TempStorage.get(1), ut.sex to TempStorage.get(4), ut.height to TempStorage.get(2),
                    ut.weight to TempStorage.get(3)).where(ut.id + "  = {userid}", "userid" to 1).exec()

            dbW.close()
            uiThread {
                progress.dismiss()
                startActivity<ScreenSlidePagerActivity>()
            }
       }
    }
}