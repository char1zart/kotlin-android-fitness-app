package com.example.ch1zart.userpack

import android.app.Fragment
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.ch1zart.another.IFragmentActions
import com.example.ch1zart.another.MainFragment
import com.example.ch1zart.another.TransferObject
import com.example.ch1zart.another.UserRowParser
import com.example.ch1zart.dbconnector.MyDatabaseOpenHelper
import com.example.ch1zart.dbconnector.UserInfo
import com.example.ch1zart.fitnessapp.R
import com.example.ch1zart.newsfeed.NewsFeedFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.db.IntParser
import org.jetbrains.anko.db.StringParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import org.w3c.dom.Text

class PrivateInfo : MainFragment() {

    var userStorage:UserClass? = null
    val ut = UserInfo()

    lateinit var email: EditText
    lateinit var psswrd: EditText

    var fab:FloatingActionButton? = null
    var click = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_privateinfo, container, false)
        setHasOptionsMenu(true)
        retainInstance = true
        return view
    }

    fun fabClick() {
        fab?.setOnClickListener {
            click++
            if (click == 1)
              //  EditUserInfo()
            else {
                click--
                //SaveUserInfo()
            }
            toast("Заглушка")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fab = find<FloatingActionButton>(R.id.fab)
        email = find<EditText>(R.id.your_email_address)
        psswrd = find<EditText>(R.id.create_new_password)

        fabClick()
        toolbar.title = resources.getString(R.string.drawer_item_cabinet)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
         IFragmentActions.openDrawer(activity, toolbar)

        getUserInfo()
    }

    fun getUserInfo() {
        CloseEditInfo()

        doAsync {
            val idU =  to.idUser
            dbW = mDbHelper.writableDatabase

            val dataUser = dbW.select(ut.table_name, ut.email, ut.password).where(ut.id + "  = {id}", "id" to idU).parseSingle(UserRowParser())
            dbW.close()

            userStorage = (UserClass(dataUser.email,dataUser.passwd, 1, idU))

            uiThread {
              email.setText(userStorage!!.email)
              psswrd.setText(userStorage!!.passwd.toString())
        }
        }
    }

    fun CloseEditInfo() {
        psswrd.isFocusable = false
        email.isFocusable = false
    }


    fun EditUserInfo() {
        psswrd.isFocusableInTouchMode = true
        email.isFocusableInTouchMode = true
        toast(R.string.on_edit)
    }

    fun canSave():Boolean
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

    var pswrd: Int? = null

   /* fun SaveUserInfo() {

        if (canSave()) {
            CloseEditInfo()
            doAsync {

                dbW = mDbHelper.writableDatabase
                dbW.update(ut.table_name, ut.email to email.text, ut.password to pswrd).where(ut.id + "  = {userid}", "userid" to to.idUser).exec()
                dbW.close()
                uiThread {
                    toast(R.string.off_edit)
                }
            }
        }
    }
*/
    fun logOut():Boolean
    {
        doAsync {

            dbW = mDbHelper.writableDatabase

            dbW.update(ut.table_name, ut.login to 0).where(ut.id + "  = {userid}", "userid" to userStorage!!.userID).exec()
            dbW.close()

            to.haveUser = false
            to.for_init_ns.clear()
            to.for_note_state.clear()

            uiThread { IFragmentActions.openNewFragment(RegFragment(),"open") }
        }
     return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_exit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item!!.itemId) {
        R.id.action_logout -> { logOut() }

        else ->  super.onOptionsItemSelected(item)
    }

}
