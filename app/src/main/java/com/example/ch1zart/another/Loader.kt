package com.example.ch1zart.another

import android.app.Fragment
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ch1zart.container.ContainerClass
import com.example.ch1zart.container.ContainerClassParse
import com.example.ch1zart.dbconnector.*
import com.example.ch1zart.fitnessapp.R
import com.example.ch1zart.newsfeed.NewsClass
import com.example.ch1zart.newsfeed.NewsFeedFragment
import com.example.ch1zart.notes.NotesClass
import com.example.ch1zart.userpack.RegFragment
import com.example.ch1zart.userpack.UserClass
import com.example.ch1zart.userpack.UserClassParser
import org.jetbrains.anko.*
import org.jetbrains.anko.db.*
import org.jetbrains.annotations.Mutable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.*
import java.util.concurrent.TimeUnit

class Loader : MainFragment() {

    var count = to.count
    val nt = Notes()
    val ut = UserInfo()

    private lateinit var notes: MutableList<NotesClass>
    private lateinit var conteiner: MutableList<ContainerClass>

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_logo, container, false)

        setHasOptionsMenu(true)
        retainInstance = true

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notes = ArrayList()
        conteiner = ArrayList()
        initData()
    }

    lateinit var dataNotes: List<Triple<String,String,String>>
    lateinit var idN: List<Int>

    fun loadData(c: ClassExample ) : MutableList<ContainerClass>
    {
        conteiner.clear()

        dbW = mDbHelper.writableDatabase
        val data = dbW.select(c.table_name, c.title, c.subtitle, c.detail, c.photoID).limit(count).parseList(ContainerRowParser())
        val id = dbW.select(c.table_name, c.id).parseList(IntParser)

        dbW.close()

        for ((ind, i) in data.withIndex()) {
            conteiner.add(ContainerClass(i.title, i.subtitle, i.detail, i.photoId, id[ind]))
        }

        return conteiner
    }

     var check: List<Int> = ArrayList()

    fun loadNotes(n: Notes)
    {
        dbW = mDbHelper.writableDatabase
        check =  dbW.select(ut.table_name, ut.id).where(ut.login + "  = {login}", "login" to 1).parseList(IntParser)

        if(check.isNotEmpty()) {
            to.haveUser = true
            to.idUser =check[0]

            dataNotes = dbW.select(n.table_name, n.note_name, nt.discription, n.date).where(n.ownerID + "  = {login}", "login" to check[0]).parseList(NotesRowParser())
            idN = dbW.select(n.table_name, n.id).where(n.ownerID + "  = {login}", "login" to check[0]).parseList(IntParser)
        }

        dbW.close()

        if(check.isNotEmpty()) {
            for ((ind, i) in dataNotes.withIndex()) {
                notes.add(NotesClass(i.first, i.second, App().getCurrentDate(i.third.toLong()), idN[ind]))
            }
            to.for_init_ns.addAll(notes)
        }

    }

    fun initData() {
        doAsync {

            to.theory_initialization.addAll(loadData(Theory()))
            to.rules_initialization.addAll(loadData(Rules()))
            to.position_initialization.addAll(loadData(SeaPositionsInfo()))
            to.emergen_initialization.addAll(loadData(Emergencycases()))
            loadNotes(Notes())


           TimeUnit.SECONDS.sleep(2)


            uiThread {
                    if (check.isEmpty()) {
                        IFragmentActions.openNewFragment(RegFragment(),"open")
                    } else {
                        IFragmentActions.initDrawer()
                        IFragmentActions.openNewFragment(NewsFeedFragment(),"open")
                    }

            }
        }
    }
}

class ContainerRowParser : RowParser<ContainerClassParse> {
    override fun parseRow(columns: Array<Any?>): ContainerClassParse {
        return  ContainerClassParse(columns[0] as String, "${columns[1]}","${columns[2]}", "${columns[3]}")
    }
}

class NotesRowParser : RowParser<Triple<String, String, String>> {
    override fun parseRow(columns: Array<Any?>): Triple<String, String, String> {
        return Triple("${columns[0]}", "${columns[1]}", "${columns[2]}")
    }
}

class UserRowParser: RowParser<UserClassParser> {
    override fun parseRow(columns: Array<Any?>): UserClassParser {
        return UserClassParser("${columns[0]}",columns[1] as Long)
    }
}
