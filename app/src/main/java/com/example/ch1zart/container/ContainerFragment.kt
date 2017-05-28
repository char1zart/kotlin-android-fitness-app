package com.example.ch1zart.container

import android.app.Fragment
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.ch1zart.another.*
import com.example.ch1zart.dbconnector.*
import com.example.ch1zart.fitnessapp.R
import org.jetbrains.anko.*
import org.jetbrains.anko.db.IntParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import org.jsoup.Connection
import java.util.*


@Suppress("DEPRECATION")
open class ContainerFragment : MainFragment(), MenuItemCompat.OnActionExpandListener,  SearchView.OnQueryTextListener {

    private lateinit var fullTimeContainer: MutableList<ContainerClass>

    lateinit var adapter: ContainerAdapter

    private lateinit var rv: RecyclerView

    var canLoad = true

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_container, container, false)
        setHasOptionsMenu(true)
        retainInstance = true

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullTimeContainer = ArrayList()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        to.container_search.clear()
        to.container_state.clear()

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        IFragmentActions.openDrawer(activity, toolbar)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv = find<RecyclerView>(R.id.rv1)
        val llm = LinearLayoutManager(ctx)
        rv.layoutManager = llm
        rv.setHasFixedSize(true)

        val mDividerItemDecoration = DividerItemDecoration(rv.context,
                llm.orientation)
        rv.addItemDecoration(mDividerItemDecoration)

        getData()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
         R.id.action_search -> {

             MenuItemCompat.setOnActionExpandListener(item, this)
             loadDataForSearch(to.current_class!!)
             to.status = 0

             false
         }

        R.id.filter_favorite -> {
            startOnlyFavorite()
            true
        }


        R.id.filter_standart -> {
            standardList()
            true
        }

        else ->
            super.onOptionsItemSelected(item)
    }

    lateinit var standard:MenuItem
    lateinit var favorite:MenuItem


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_seach_pas, menu)

        standard = menu.findItem(R.id.filter_standart)
        standard.isVisible = false

        favorite = menu.findItem(R.id.filter_favorite)
        favorite.isVisible = true

        val search = menu.findItem(R.id.action_search)
        val searchView = search.actionView as SearchView

        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    fun loadMore() {
        adapter.setOnLoadMoreListener {

                if (canLoad && to.status == 1) {
                    if (canLoad) {
                   addData(to.current_class!!, to.container_state.size)
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

///добавляем данные при прокрутке
    fun addData(c: ClassExample,size: Int) {
        doAsync {
            to.container_search.clear()

            dbW = mDbHelper.writableDatabase
            val data = dbW.select(c.table_name, c.title, c.subtitle, c.detail, c.photoID).limit(size, to.count).parseList(ContainerRowParser())
            val id = dbW.select(c.table_name, c.id).limit(size, to.count).parseList(IntParser)

            dbW.close()

            for ((ind, i) in data.withIndex()) {
                if (data.isNotEmpty()) {
                    to.container_state.add(ContainerClass(i.title, i.subtitle, i.detail, i.photoId, id[ind]))

                } else {
                    canLoad = false
                    break
                }
            }

            to.container_search.addAll((to.container_state))
            fullTimeContainer.addAll(to.container_search)

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

        adapter = ContainerAdapter(to.container_state)
        rv.adapter = adapter


            adapter.onItemLongClickListener = BaseQuickAdapter.OnItemLongClickListener { adapter, view, position ->  if(favorite.isVisible)
                changeFavorite(to.current_class!!, position)
                true
            }

            adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position -> openDetails(position) }
    }

    fun openDetails(p: Int)
    {
        to.current_state = to.container_state[p]
        IFragmentActions.openNewFragment(ContainerDetailFragment(),"open")
    }

    fun loadData(c: ClassExample)
    {
        to.container_search.clear()
        if(canLoad) {

            dbW = mDbHelper.writableDatabase
            val data = dbW.select(c.table_name, c.title, c.subtitle, c.detail, c.photoID).parseList(ContainerRowParser())
            val id = dbW.select(c.table_name, c.id).parseList(IntParser)

            dbW.close()

            for ((ind, i) in data.withIndex()) {
                to.container_search.add(ContainerClass(i.title, i.subtitle, i.detail, i.photoId, id[ind]))
            }
            fullTimeContainer.addAll(to.container_search)
        }
    }

   fun loadDataForSearch(c:ClassExample) {

      if (canLoad) {
            doAsync {
                loadData(c)

                uiThread {
                    rv.adapter.notifyDataSetChanged()
                    canLoad = false
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

    fun search(s: String?):Boolean
    {
        to.container_state.clear()

        rv.adapter.notifyDataSetChanged()

        for (i in to.container_search) {
            App().regExpTheory(s!!, i)
        }

        rv.adapter.notifyDataSetChanged()

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {

        to.container_state.clear()
        to.container_state.addAll(fullTimeContainer)
        rv.adapter.notifyDataSetChanged()
        to.status = 1

        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean
    {
        fullTimeContainer.addAll(to.container_state)
        return true
    }

    fun changeFavorite(c: ClassExample,id: Int) {
        doAsync {
            dbW = mDbHelper.writableDatabase

            val changedData = dbW.select(c.table_name, c.title, c.subtitle, c.detail, c.photoID).where(c.id + "  = {userid}", "userid" to to.container_state[id].id).parseSingle(ContainerRowParser())
            var img = R.drawable.locked

            if (changedData.photoId.toInt() == img) {
                img = R.drawable.unlocked
                dbW.update(c.table_name, c.photoID to img).where(c.id + "  = {userid}", "userid" to to.container_state[id].id).exec()
            } else {
                img = R.drawable.locked
                dbW.update(c.table_name, c.photoID to img).where(c.id + "  = {userid}", "userid" to to.container_state[id].id).exec()
            }

            val d = dbW.select(c.table_name, c.title, c.subtitle, c.detail, c.photoID).where(c.id + "  = {userid}", "userid" to to.container_state[id].id).parseSingle(ContainerRowParser())

            dbW.close()

            val v = ContainerClass(to.container_state[id].title, to.container_state[id].subtitle, to.container_state[id].detail, img.toString(), to.container_state[id].id)
            //val v = ContainerClass(d.title,d.subtitle, d.detail, img.toString(), to.container_state[id].id)


            to.container_state.removeAt(id)
            to.container_state.add(id,v)
            to.container_search.removeAt(id)
            to.container_search.add(id,v)

            fullTimeContainer.removeAt(id)
            fullTimeContainer.add(v)

            uiThread {
                rv.adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        to.status = 1
    }

    var temp : MutableList<ContainerClass> = ArrayList()

    fun standardList() {

        standard.isVisible = false
        favorite.isVisible = true

        to.container_state.clear()
        to.container_state.addAll(to.container_search)

        rv.adapter.notifyDataSetChanged()
    }

    var click = 0

    fun startOnlyFavorite() {

        standard.isVisible = true
        favorite.isVisible = false

        if(click == 0)
            loadData(to.current_class!!)

        canLoad = false

        temp = ArrayList()

        to.container_search
                .filter { it.photoId.toInt() == R.drawable.locked }
                .forEach { temp.add(it) }

        to.container_state.clear()
        to.container_state.addAll(temp)

        rv.adapter.notifyDataSetChanged()
        click++
    }

}