package com.example.ch1zart.notes

import android.app.Fragment
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.EditText
import com.example.ch1zart.another.App
import com.example.ch1zart.another.TransferObject
import com.example.ch1zart.dbconnector.MyDatabaseOpenHelper
import com.example.ch1zart.dbconnector.Notes
import com.example.ch1zart.fitnessapp.R
import org.jetbrains.anko.*
import android.content.Intent

import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.ShareActionProvider
import android.text.Editable
import android.text.TextWatcher
import com.example.ch1zart.another.FragmentActions
import com.example.ch1zart.newsfeed.NewsFeedFragment


class NoteEditFragment : Fragment(), ShareActionProvider.OnShareTargetSelectedListener, TextWatcher {

    lateinit var mDbHelper: MyDatabaseOpenHelper
    lateinit var dbW: SQLiteDatabase
    private val to = TransferObject
    lateinit private var FragmentActions: FragmentActions

    lateinit var myShareIntent: Intent
    lateinit var share: ShareActionProvider


    val toolbar by lazy {
        find<Toolbar>(R.id.toolbar_actionbar)
    }

    val newnote by lazy {
        find<EditText>(R.id.e_newnote)
    }

    val titlenote by lazy {
        find<EditText>(R.id.e_note_title)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_edit_note, container, false)
        setHasOptionsMenu(true)
        retainInstance = true
        return view
    }

    //0 - для новых записей
    //1 - для редактирования записи
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        if (to.note_status == 0) {
            inflater.inflate(R.menu.menu_edit_note, menu)
        }
            else
        {

            var item = menu.findItem(R.id.action_share)

            share = ShareActionProvider(activity)
            share.setShareIntent(createShareIntent())
            MenuItemCompat.setActionProvider(item, share)

            share.setOnShareTargetSelectedListener(this)
            inflater.inflate(R.menu.menu_note_editing, menu)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_save -> { saveNote() }
        R.id.action_cancel -> { cancelNote()}
        R.id.action_share -> { shareNote() }
        R.id.action_delete -> { deleteNote()}

        else ->  super.onOptionsItemSelected(item)
    }

    fun shareNote() : Boolean {

     return true
    }

    private fun createShareIntent(): Intent {
        myShareIntent = Intent(Intent.ACTION_SEND)
        myShareIntent.type = "text/plain"

        return myShareIntent
    }


    fun cancelNote():Boolean {
        FragmentActions.openNewFragment(NotesFragment())
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FragmentActions = ctx as FragmentActions
        mDbHelper = MyDatabaseOpenHelper(ctx)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(to.note_status == 1) {
            toolbar.title = resources.getString(R.string.edit_note)

            titlenote.setText(to.for_note_state[0].notename)
            newnote.setText(to.for_note_state[0].description)

            myShareIntent = Intent(Intent.ACTION_SEND)
            myShareIntent.type = "text/plain"
            myShareIntent.putExtra(Intent.EXTRA_TEXT, "Тема:" + titlenote.text +  "\n \n" + newnote.text)
        }

        else {
            toolbar.title = resources.getString(R.string.new_note)
        }

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        FragmentActions.getArrowBack()

        toolbar.setNavigationOnClickListener {
            FragmentActions.openNewFragment(NotesFragment())
        }

        titlenote.addTextChangedListener(this)
        newnote.addTextChangedListener(this)
    }

    override fun afterTextChanged(p0: Editable?) {
        if (to.note_status == 1) {

            myShareIntent.putExtra(Intent.EXTRA_TEXT, "Тема:" + titlenote.text + "\n \n" + newnote.text)
            share.setShareIntent(myShareIntent)
        }
    }

    fun deleteNote() : Boolean {
        doAsync {
           dbW = mDbHelper.writableDatabase
           mDbHelper.deleteNote(dbW,  to.for_note_state[0].id)
           dbW.close()
            to.for_init_ns.remove(to.for_note_state[0])
            uiThread {
                FragmentActions.openNewFragment(NotesFragment())
            }
        }
        return true
    }

    fun saveNote():Boolean {
        if (titlenote.text.isNotEmpty() && newnote.text.isNotEmpty()) {
            if (to.note_status == 0) {
                doAsync {
                    dbW = mDbHelper.writableDatabase
                    mDbHelper.saveNote(dbW, App().getCurrentTime(), "${titlenote.text}", "${newnote.text}", to.idUser)
                    dbW.close()

                    uiThread {
                        ///в бд ид начинается с 1 ///
                        to.for_init_ns.add(NotesClass("${titlenote.text}", "${newnote.text}", App().getCurrentDate(App().getCurrentTime()), to.for_init_ns.size - 1))
                        toast(resources.getString(R.string.task_done))
                        FragmentActions.openNewFragment(NotesFragment())
                    }
                }
            } else {
                //работает
                doAsync {
                    dbW = mDbHelper.writableDatabase
                    mDbHelper.deleteNote(dbW, to.for_note_state[0].id)
                    mDbHelper.saveNote(dbW, App().getCurrentTime(), "${titlenote.text}", "${newnote.text}",to.idUser)

                    dbW.close()

                    to.for_init_ns.remove(to.for_note_state[0])
                    to.for_init_ns.add(NotesClass("${titlenote.text}", "${newnote.text}", App().getCurrentDate(App().getCurrentTime()), to.for_note_state[0].id))

                    uiThread {
                        toast(resources.getString(R.string.task_done))
                        to.for_note_state.clear()
                        FragmentActions.openNewFragment(NotesFragment())
                    }
                }
            }
        }
        else { toast(resources.getString(R.string.warning_string)) }
        return true
    }

    override fun onShareTargetSelected(source: ShareActionProvider?, intent: Intent?): Boolean {
        return (false)
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}