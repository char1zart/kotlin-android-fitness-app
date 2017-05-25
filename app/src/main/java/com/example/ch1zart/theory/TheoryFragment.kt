package com.example.ch1zart.theory

import android.app.Fragment
import android.database.sqlite.SQLiteDatabase
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.*
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.ch1zart.another.App
import com.example.ch1zart.another.FragmentActions
import com.example.ch1zart.another.TheoryRowParser
import com.example.ch1zart.another.TransferObject
import com.example.ch1zart.dbconnector.MyDatabaseOpenHelper
import com.example.ch1zart.dbconnector.Theory
import com.example.ch1zart.fitnessapp.R
import org.jetbrains.anko.*
import org.jetbrains.anko.db.IntParser
import org.jetbrains.anko.db.select
import java.util.*

@Suppress("DEPRECATION")
/**
 * Created by Ch1zART on 19.05.2017.
 */
class  TheoryFragment: Fragment(), MenuItemCompat.OnActionExpandListener,  SearchView.OnQueryTextListener {

    private lateinit var theory: MutableList<TheoryClass>
    lateinit var mDbHelper: MyDatabaseOpenHelper
    lateinit var dbW: SQLiteDatabase
    lateinit var adapter: TheoryAdapter
    private lateinit var rv: RecyclerView
    private val to = TransferObject
    var canLoad = true
    var tt = Theory()

    val toolbar by lazy {
        find<Toolbar>(R.id.toolbar_actionbar)
    }

    lateinit private var FragmentActions: FragmentActions

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_theory, container, false)
        setHasOptionsMenu(true)
        retainInstance = true

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDbHelper = MyDatabaseOpenHelper(activity)
        FragmentActions = ctx as FragmentActions
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

         toolbar.title = resources.getString(R.string.menu_p_s)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        FragmentActions.openDrawer(activity, toolbar)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv = find<RecyclerView>(R.id.rv1)
        val llm = LinearLayoutManager(ctx)
        rv.layoutManager = llm
        rv.setHasFixedSize(true)

        getData()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
         R.id.action_search -> {

            MenuItemCompat.setOnActionExpandListener(item, this)
             loadDataForSearch()
             to.status = 0
             adapter.loadMoreComplete()

            false
        }
        else ->
            super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_seach_pas, menu)

        val search = menu.findItem(R.id.action_search)
        val searchView = search.actionView as SearchView

        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    val maxSize = to.maxsizeTheory.get()

    fun loadMore() {
        adapter.setOnLoadMoreListener {

                if (to.for_theory_state.size < maxSize && to.status == 1) {
                    canLoad = true
                    if (canLoad) {
                        addData(to.for_theory_state.size)
                    }
                } else {
                    doAsync {
                        adapter.loadMoreEnd()
                        uiThread {
                            canLoad = false
                        }
                    }
                }

        }
        if(!canLoad)
        {
            adapter.loadMoreEnd()
        }
    }

    fun addData(size: Int)
    {
       theory = ArrayList()

        doAsync {
            dbW = mDbHelper.writableDatabase
            val dataTheory = dbW.select(tt.table_name, tt.problem, tt.title, tt.howTo).limit(size,to.count).parseList(TheoryRowParser())
            val idT = dbW.select(tt.table_name, tt.id).limit(size,to.count).parseList(IntParser)
            dbW.close()


            for ((ind, i) in dataTheory.withIndex()) {
                theory.add(TheoryClass(i.problem, i.title, i.howTo, idT[ind]))
            }

            to.for_theory_state.addAll(theory)
            to.for_theory_search.addAll(theory)

            uiThread {
                    rv.adapter.notifyDataSetChanged()
                    adapter.loadMoreComplete()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadMore()
    }

    fun getData() {
        adapter = TheoryAdapter(to.for_theory_state)
        adapter.openLoadAnimation()
        rv.adapter = adapter
        rv.adapter.notifyDataSetChanged()
/*        adapter.setOnItemClickListener(
                object : BaseQuickAdapter.OnItemClickListener {
                    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                        //chooseNote(position)
                    }
                })*/
    }

    fun search(s: String?):Boolean
    {
        to.for_theory_state.clear()
        rv.adapter.notifyDataSetChanged()

        for (i in to.for_theory_search) {
            App().regExpTheory(s!!, i)
        }

        rv.adapter.notifyDataSetChanged()

        return true
    }

    fun loadDataForSearch() {

        if (to.for_theory_search.size < to.maxsizeTheory.get()) {
            doAsync {
                dbW = mDbHelper.writableDatabase
                val dataTheory = dbW.select(tt.table_name, tt.problem, tt.title, tt.howTo).limit(to.for_theory_state.size, to.maxsizeTheory.get()).parseList(TheoryRowParser())
                val idT = dbW.select(tt.table_name, tt.id).limit(to.for_theory_state.size, to.maxsizeTheory.get()).parseList(IntParser)
                dbW.close()

                for ((ind, i) in dataTheory.withIndex()) {
                    theory.add(TheoryClass(i.problem, i.title, i.howTo, idT[ind]))
                }

                to.for_theory_search.addAll(theory)

                uiThread {
                    rv.adapter.notifyDataSetChanged()
                    adapter.loadMoreComplete()
                }
            }
        }
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query!!.isNotEmpty()) {
           search(query)
        }
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {

      to.for_theory_state.clear()
      to.for_theory_state.addAll(to.for_theory_search)

        rv.adapter.notifyDataSetChanged()
        to.status = 1

        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean = true


    override fun onDestroy() {
        super.onDestroy()

        to.status = 1
    }

}