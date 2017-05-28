package com.example.ch1zart.position

import android.app.Fragment
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.ch1zart.another.TransferObject
import com.example.ch1zart.dbconnector.MyDatabaseOpenHelper
import com.example.ch1zart.dbconnector.SeaPositionsInfo
import com.example.ch1zart.fitnessapp.R
import com.example.ch1zart.another.IFragmentActions
import org.jetbrains.anko.*
import org.jetbrains.anko.db.*
import java.util.*

@Suppress("DEPRECATION")
class PositionFragment: Fragment() {
/*
    var pt = SeaPositionsInfo()

    lateinit var mDbHelper: MyDatabaseOpenHelper
    lateinit var dbW: SQLiteDatabase
    lateinit var adapter: PositionAdapter
    private lateinit var positions: MutableList<PositionClass>
    private lateinit var rv: RecyclerView
    private val to = TransferObject

    var canLoad = true

    lateinit var toolbar: Toolbar

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_position, container, false)

        setHasOptionsMenu(true)
        retainInstance = true
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_position_filter, menu)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = find<Toolbar>(R.id.toolbar_actionbar)
        rv = find<RecyclerView>(R.id.rv1)

        val llm = LinearLayoutManager(ctx)
        rv.layoutManager = llm
        rv.setHasFixedSize(true)

        val mDividerItemDecoration = DividerItemDecoration(rv.context,
                llm.orientation)
        rv.addItemDecoration(mDividerItemDecoration)
    }

    lateinit private var IFragmentActions: IFragmentActions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDbHelper = MyDatabaseOpenHelper(ctx)
        positions = ArrayList()

        IFragmentActions = ctx as IFragmentActions
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.title = resources.getString(R.string.menu_position)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        IFragmentActions.openDrawer(activity, toolbar)

        getData()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.filter_favorite -> {
            startOnlyFavorite()
            true
        }
        R.id.filter_random -> {
            false
        }

        R.id.filter_standart -> {
           // getData()
            true
        }

        else ->
            super.onOptionsItemSelected(item)
    }

    fun getData() {

        positions.addAll(to.for_position_state)
        adapter = PositionAdapter(positions)
        adapter.openLoadAnimation()
        rv.adapter = adapter
        rv.adapter.notifyDataSetChanged()

        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position -> chanceFavorite(position) }

        loadMore()
    }

    fun startOnlyFavorite() {

        positions.clear()
        TransferObject.for_favorite_state.clear()

        val temp = TransferObject.for_position_state

        temp
                .filter { it.bool == "true" }
                .forEach { TransferObject.for_favorite_state.add(it) }

        val t = to.for_favorite_state

        for (i in t) {
            positions.add(PositionClass(i.position, i.description, i.bool, R.drawable.locked, i.id))
        }

        rv.adapter.notifyDataSetChanged()
    }

    fun loadMore() {
        adapter.setOnLoadMoreListener {

            if (to.for_position_state.size < to.maxsizePosition.get() && to.status == 1) {
                canLoad = true
                if (canLoad) {
                    addData(to.for_position_state.size)
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
    }

    fun addData(size: Int)
    {
        doAsync {
            dbW = mDbHelper.writableDatabase
            val dataPosition = dbW.select(pt.table_name, pt.position, pt.discription, pt.favorite).limit(size,to.count).parseList(PositionsRowParser())
            val idP = dbW.select(pt.table_name, pt.id).limit(size,to.count).parseList(IntParser)
            dbW.close()


            for ((ind, i) in dataPosition.withIndex()) {
                positions.add(PositionClass(i.position, i.description, i.bool, R.drawable.locked,idP[ind]))
            }

            to.for_position_state.addAll(positions)
            //to.for_theory_search.addAll(theory)

            uiThread {
               // getData()
                toast("Данные добавили")
                rv.adapter.notifyDataSetChanged()
                adapter.loadMoreComplete()
            }
        }
    }

    fun chanceFavorite(id: Int) {

        when (TransferObject.status) {
            1 -> {
                doAsync {
                    dbW = mDbHelper.writableDatabase

                    val changedData = dbW.select(pt.table_name, pt.position, pt.discription, pt.favorite).where(pt.id + "  = {userid}", "userid" to to.for_position_state[id].id).parseSingle(PositionsRowParser())

                    if (changedData.bool == "false") {
                        dbW.update(pt.table_name, pt.favorite to "true").where(pt.id + "  = {userid}", "userid" to to.for_position_state[id].id).exec()
                    } else {
                        dbW.update(pt.table_name, pt.favorite to "false").where(pt.id + "  = {userid}", "userid" to to.for_position_state[id].id).exec()
                    }

                    dbW.close()

                    positions.removeAt(id)
                    positions.add(id, PositionClass(changedData.position, changedData.description, changedData.bool, R.drawable.locked, to.for_position_state[id].id))


                    uiThread {
                        //   to.for_position_state.clear()
                        //   to.for_position_state.addAll(positions)

                        rv.adapter.notifyDataSetChanged()

                    }
                }
            }
        }
    }*/
}
