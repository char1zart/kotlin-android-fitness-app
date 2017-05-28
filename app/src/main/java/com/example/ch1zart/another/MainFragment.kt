package com.example.ch1zart.another

import android.app.Fragment
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import com.example.ch1zart.dbconnector.MyDatabaseOpenHelper
import com.example.ch1zart.fitnessapp.R
import org.jetbrains.anko._Toolbar
import org.jetbrains.anko.ctx
import org.jetbrains.anko.find

/**
 * Created by Ch1zART on 26.05.2017.
 */
open class MainFragment :Fragment() {

    lateinit var mDbHelper: MyDatabaseOpenHelper
    lateinit var dbW: SQLiteDatabase
    val to = TransferObject

    val toolbar by lazy {
        find<Toolbar>(R.id.toolbar_actionbar)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_container, container, false)
        setHasOptionsMenu(true)
        retainInstance = true

        return view
    }

    lateinit var IFragmentActions: IFragmentActions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDbHelper = MyDatabaseOpenHelper(activity)
        IFragmentActions = ctx as IFragmentActions
    }
}