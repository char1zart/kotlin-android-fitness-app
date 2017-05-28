package com.example.ch1zart.userpack

import android.app.Fragment
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.ch1zart.another.*
import com.example.ch1zart.dbconnector.MyDatabaseOpenHelper
import com.example.ch1zart.dbconnector.Notes
import com.example.ch1zart.dbconnector.UserInfo
import com.example.ch1zart.fitnessapp.R
import com.example.ch1zart.newsfeed.NewsFeedFragment
import com.example.ch1zart.notes.NotesClass
import org.jetbrains.anko.*
import org.jetbrains.anko.db.IntParser
import org.jetbrains.anko.db.StringParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import java.util.*

class RegFragment : MainFragment() {

    val b_reg by lazy {
        find<Button>(R.id.b_reg)
    }


    val b_login by lazy {
        find<Button>(R.id.b_login)
    }
    val ut = UserInfo()

    lateinit var email: EditText
    lateinit var psswrd: EditText
    private lateinit var notes: MutableList<NotesClass>

    val nt = Notes()

    var pswrd: Int? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_reg, container, false)
        setHasOptionsMenu(true)
        retainInstance = true
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        email = find<EditText>(R.id.your_email_address)
        psswrd = find<EditText>(R.id.create_new_password)

        b_login.setOnClickListener {
            notes = ArrayList()
            checkUser()
        }

        b_reg.setOnClickListener { MakeStorage() }
    }

    fun checkUser() {
      if(canStart())
      {
          doAsync {
              val t = UserClassParser(email.text.toString(), pswrd!!.toLong())
              dbW = mDbHelper.writableDatabase
              val dataUser = dbW.select(ut.table_name, ut.email, ut.password).where(ut.email + "  = {email}", "email" to t.email).parseList(UserRowParser())
              val idU = dbW.select(ut.table_name, ut.id).where(ut.email + "  = {email}", "email" to t.email).parseList(IntParser)

              dbW.update(ut.table_name, ut.login to 1).where(ut.email + "  = {email}", "email" to t.email).exec()

              val dataNotes = dbW.select(nt.table_name, nt.note_name, nt.discription, nt.date).where(nt.ownerID + "  = {ownerID}", "ownerID" to idU[0]).parseList(NotesRowParser())
              val idN = dbW.select(nt.table_name, nt.id).where(nt.ownerID + "  = {ownerID}", "ownerID" to idU[0]).parseList(IntParser)

              dbW.close()

              for ((ind, i) in dataNotes.withIndex()) {
                  notes.add(NotesClass(i.first, i.second, App().getCurrentDate(i.third.toLong()), idN[ind]))
              }

              to.for_init_ns.addAll(notes)
              to.for_search.addAll(notes)

              uiThread {
                  if(dataUser.isNotEmpty()) {
                      if (email.text.toString() == dataUser[0].email && pswrd!!.toLong() == dataUser[0].passwd) {

                           to.idUser = idU[0]
                           to.haveUser = true
                           IFragmentActions.initDrawer()
                           IFragmentActions.openNewFragment(NewsFeedFragment(),"open")
                      }
                      else
                      {
                          toast("Неверный логин или пароль")
                      }
                  }

                  else {
                      toast("Ошибка входа")
                  }
              }
          }
      }
      else {
          toast(R.string.ops)
      }
    }

    fun canStart():Boolean
    {
        var b = false
        if (psswrd.text.isNotEmpty() && email.text.isNotEmpty()) {
            pswrd = Integer.parseInt(psswrd.text.toString())
            b = true
        }
        else {
            toast(R.string.warning_string)
        }
        return b
    }

    fun MakeStorage() {

      if(canStart())
      {
            val t = UserClassParser(email.text.toString(),pswrd!!.toLong())
            doAsync {
                dbW = mDbHelper.writableDatabase
                mDbHelper.saveUser(dbW, t.email, pswrd!!.toLong(), 1)
                val check =  dbW.select(ut.table_name, ut.id).where(ut.login + "  = {login}", "login" to 1).parseList(IntParser)
                dbW.close()

                to.haveUser = true
                to.idUser = check[0]
                IFragmentActions.initDrawer()
                IFragmentActions.openNewFragment(NewsFeedFragment(),"open")


                    to.haveUser = true
                    to.idUser =check[0]



                uiThread {
                    toast(R.string.drawer_hello_name)
                    IFragmentActions.initDrawer()
                    IFragmentActions.openNewFragment(NewsFeedFragment(),"open")
                }
            }
        }
        else {
            toast(R.string.warning_string)
        }
    }
}