package com.example.ch1zart.another

import android.app.Fragment
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ch1zart.dbconnector.*
import com.example.ch1zart.fitnessapp.R
import com.example.ch1zart.newsfeed.NewsClass
import com.example.ch1zart.newsfeed.NewsFeedFragment
import com.example.ch1zart.notes.NotesClass
import com.example.ch1zart.position.PositionClass
import com.example.ch1zart.position.PositionClassParse
import com.example.ch1zart.theory.TheoryClass
import com.example.ch1zart.theory.TheoryClassParse
import com.example.ch1zart.userpack.RegFragment
import com.example.ch1zart.userpack.UserClass
import com.example.ch1zart.userpack.UserClassParser
import org.jetbrains.anko.ctx
import org.jetbrains.anko.db.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.*
import java.util.concurrent.TimeUnit

class Loader : Fragment() {

    lateinit var mDbHelper: MyDatabaseOpenHelper
    lateinit var dbW: SQLiteDatabase

    private val to = TransferObject
    var count = to.count
    var ind = 0
    val tt = Theory()
    val nt = Notes()
    val pt = SeaPositionsInfo()
    val ut = UserInfo()

    private lateinit var notes: MutableList<NotesClass>
    private lateinit var theory: MutableList<TheoryClass>
    private lateinit var positions: MutableList<PositionClass>

    lateinit private var FragmentActions: FragmentActions

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_logo, container, false)

        setHasOptionsMenu(true)
        retainInstance = true

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FragmentActions = ctx as FragmentActions
        mDbHelper = MyDatabaseOpenHelper(ctx)
        notes = ArrayList()
        theory = ArrayList()
        positions = ArrayList()

        initData()
    }

    lateinit var dataNotes: List<Triple<String,String,String>>
    lateinit var idN: List<Int>


    fun initData() {
        doAsync {
            dbW = mDbHelper.writableDatabase

            val check =  dbW.select(ut.table_name, ut.id).where(ut.login + "  = {login}", "login" to 1).parseList(IntParser)

            if(check.isNotEmpty()) {
                to.haveUser = true
                to.idUser =check[0]

                dataNotes = dbW.select(nt.table_name, nt.note_name, nt.discription, nt.date).where(nt.ownerID + "  = {login}", "login" to check[0]).parseList(NotesRowParser())
                idN = dbW.select(nt.table_name, nt.id).where(nt.ownerID + "  = {login}", "login" to check[0]).parseList(IntParser)

            }

            val dataTheory = dbW.select(tt.table_name, tt.problem, tt.title, tt.howTo).limit(count).parseList(TheoryRowParser())
            val idT = dbW.select(tt.table_name, tt.id).parseList(IntParser)

            val dataPositions = dbW.select(pt.table_name, pt.position, pt.discription, pt.favorite).limit(count+7).parseList(PositionsRowParser())
            val idP = dbW.select(pt.table_name, pt.id).parseList(IntParser)

            dbW.close()

            if(check.isNotEmpty()) {
                ind = 0
                  for (i in dataNotes) {
                      notes.add(NotesClass(i.first, i.second, App().getCurrentDate(i.third.toLong()), idN[ind]))
                      ind++
                  }
                  to.for_init_ns.addAll(notes)
            }

            to.maxsizePosition.set(idP.size)
            to.maxsizeTheory.set(idT.size)

            ind = 0
            for ((ind, i) in dataPositions.withIndex()) {
                positions.add(PositionClass(i.position, i.description, i.bool, R.drawable.locked, idP[ind]))
            }

            to.for_position_state.addAll(positions)

            ind = 0
            for (i in dataTheory) {
                theory.add(TheoryClass(i.problem, i.title, i.howTo, idT[ind]))
                ind++
            }

            to.for_theory_state.addAll(theory)
            to.for_theory_search.addAll(theory)
          // TimeUnit.SECONDS.sleep(2)


            uiThread {
                if (check.isEmpty()) {
                    FragmentActions.openNewFragment(RegFragment())
                }

                else {
                    FragmentActions.initDrawer()
                    FragmentActions.openNewFragment(NewsFeedFragment())
                }
            }
        }
    }
}

class TheoryRowParser : RowParser<TheoryClassParse> {
    override fun parseRow(columns: Array<Any?>): TheoryClassParse {
        return  TheoryClassParse(columns[0] as String, "${columns[1]}",columns[2] as String)
    }
}

class NotesRowParser : RowParser<Triple<String, String, String>> {
    override fun parseRow(columns: Array<Any?>): Triple<String, String, String> {
        return Triple("${columns[0]}", "${columns[1]}", "${columns[2]}")
    }
}

class PositionsRowParser : RowParser<PositionClassParse> {
    override fun parseRow(columns: Array<Any?>): PositionClassParse {
        return  PositionClassParse("${columns[0]}", "${columns[1]}","${columns[2]}")
    }
}

class UserRowParser: RowParser<UserClassParser> {
    override fun parseRow(columns: Array<Any?>): UserClassParser {
        return UserClassParser("${columns[0]}",columns[1] as Long)
    }
}
