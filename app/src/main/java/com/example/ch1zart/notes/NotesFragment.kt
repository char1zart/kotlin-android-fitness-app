package com.example.ch1zart.notes

import android.app.Fragment
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.example.ch1zart.fitnessapp.R
import org.jetbrains.anko.*
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.TextView
import com.example.ch1zart.another.*
import com.chad.library.adapter.base.BaseQuickAdapter
import android.view.ViewGroup
import android.support.v7.widget.GridLayoutManager



class NotesFragment : MainFragment(), MenuItemCompat.OnActionExpandListener, SearchView.OnQueryTextListener {

    override fun onQueryTextSubmit(query: String?): Boolean  {return false}

    lateinit var search:MenuItem
    lateinit var note_menu:Menu

    lateinit var adapter: NotesAdapter
    private lateinit var rv: RecyclerView
    lateinit var empty: TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_notes, container, false)
        setHasOptionsMenu(true)
        retainInstance = true

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_note_new, menu)

        note_menu = menu
        search = menu.findItem(R.id.action_search)
        val searchView = search.actionView as SearchView

        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }


    fun showOverflowMenu(showMenu: Boolean) {
        note_menu.setGroupVisible(R.id.group_action_new,showMenu)
    }

   fun createNewNote():Boolean
   {
       IFragmentActions.openNewFragment(NoteEditFragment(),"open")
       to.note_status = 0
       return true
   }

    fun search(s: String?):Boolean
    {
        to.for_search.clear()
        rv.adapter.notifyDataSetChanged()


        for(i in to.for_init_ns)
        {
            App().regExpNote(s!!,i)
        }

        rv.adapter.notifyDataSetChanged()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item!!.itemId) {
        R.id.action_new -> {
            createNewNote() }
        R.id.action_search -> {
            showOverflowMenu(false)
            MenuItemCompat.setOnActionExpandListener(item, this)
            false
        }
        else ->
            super.onOptionsItemSelected(item)
    }

     override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        empty = find<TextView>(R.id.t_empty)

        rv = find<RecyclerView>(R.id.rv1)

        val numberOfColumns = 2

        val glm = GridLayoutManager(ctx,numberOfColumns)

        rv.layoutManager = glm
        rv.setHasFixedSize(true)

        getData()
     }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.title = resources.getString(R.string.drawer_item_note)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        if (!to.for_init_ns.isEmpty()) {
            empty.visibility = View.GONE
        } else {
            empty.visibility = View.VISIBLE
        }

       IFragmentActions.openDrawer(activity, toolbar)
    }

    fun chooseNote(p: Int)
    {
        to.note_status = 1
        to.for_note_state.clear()
        to.status = p
        to.for_note_state.add(to.for_search[p])

        IFragmentActions.openNewFragment(NoteEditFragment(),"open")
    }

    fun getData() {
        to.for_search.clear()
        val r = App().reversedCo(to.for_init_ns)
        to.for_search.addAll(r)

        adapter = NotesAdapter(to.for_search)
        adapter.openLoadAnimation()
        rv.adapter = adapter
        rv.adapter.notifyDataSetChanged()

        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position -> chooseNote(position) }
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        showOverflowMenu(true)

        to.for_search.clear()
        val r = App().reversedCo(to.for_init_ns)
        to.for_search.addAll(r)

        rv.adapter.notifyDataSetChanged()

        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {

        if (query!!.isNotEmpty()) {
            search(query)
        }
        else
        {          }
        return false
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        return true
    }
}