package com.example.ch1zart.container

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ShareActionProvider
import android.view.*
import android.widget.TextView
import com.example.ch1zart.another.MainFragment
import com.example.ch1zart.fitnessapp.R
import com.example.ch1zart.rules.RulesFragment
import org.jetbrains.anko.find

class ContainerDetailFragment :MainFragment(), ShareActionProvider.OnShareTargetSelectedListener {


    lateinit var myShareIntent: Intent
    lateinit var share: ShareActionProvider

    val title by lazy {
        find<TextView>(R.id.t_title)
    }

    val subtitle by lazy {
        find<TextView>(R.id.t_subtitle)
    }

    val detail by lazy {
        find<TextView>(R.id.t_detail)
    }

    override fun onShareTargetSelected(source: ShareActionProvider?, intent: Intent?): Boolean {
        return (false)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_containerdetail, container, false)
        setHasOptionsMenu(true)
        retainInstance = true

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

            inflater.inflate(R.menu.menu_share_only, menu)
            val item = menu.findItem(R.id.action_share)

            share = ShareActionProvider(activity)
            share.setShareIntent(createShareIntent())
            MenuItemCompat.setActionProvider(item, share)

            share.setOnShareTargetSelectedListener(this)
    }

    private fun createShareIntent(): Intent {
        myShareIntent = Intent(Intent.ACTION_SEND)
        myShareIntent.type = "text/plain"
        myShareIntent.putExtra(Intent.EXTRA_TEXT, "" + title.text +  "\n \n" + subtitle.text+  "\n \n" + detail.text)

        return myShareIntent
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

            toolbar.title = resources.getString(to.fragment_state)

            title.text = to.current_state!!.title
            subtitle.text = to.current_state!!.subtitle
            detail.text = to.current_state!!.detail

            myShareIntent = Intent(Intent.ACTION_SEND)
            myShareIntent.type = "text/plain"
            myShareIntent.putExtra(Intent.EXTRA_TEXT, "" + title.text +  "\n \n" + subtitle.text+  "\n \n" + detail.text)


        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        IFragmentActions.getArrowBack()

        toolbar.setNavigationOnClickListener {
        IFragmentActions.openNewFragment(to.current_fragment!!,"back")


        }
    }

}