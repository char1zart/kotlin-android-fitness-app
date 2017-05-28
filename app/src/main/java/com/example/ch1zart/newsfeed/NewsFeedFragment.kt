package com.example.ch1zart.newsfeed

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.*
import android.widget.ProgressBar
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.ch1zart.another.IFragmentActions
import com.example.ch1zart.another.MainFragment
import com.example.ch1zart.another.TransferObject
import com.example.ch1zart.fitnessapp.R
import org.jetbrains.anko.*
import org.jetbrains.anko.db.IntParser
import org.jetbrains.anko.db.select
import org.jsoup.Jsoup
import org.jsoup.Jsoup.*
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.*


class NewsFeedFragment : MainFragment() {

    lateinit var adapter: NewsAdapter
    private lateinit var rv: RecyclerView

    private lateinit var news: MutableList<NewsClass>


    lateinit var progress: ProgressBar
    var i = 0
    val max = 10
    var lastIndex = 2

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_newsfeed, container, false)
        setHasOptionsMenu(true)
        retainInstance = true

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.title = resources.getString(R.string.menu_newsfeed)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        progress = find<ProgressBar>(R.id.progress)

        IFragmentActions.openDrawer(activity, toolbar)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv = find<RecyclerView>(R.id.rv1)

        val llm = LinearLayoutManager(ctx)
        rv.layoutManager = llm
        rv.setHasFixedSize(true)

        val mDividerItemDecoration = DividerItemDecoration(rv.context, llm.orientation)
        rv.addItemDecoration(mDividerItemDecoration)

        loadNews()
    }

    fun refreshData() {
        rv.visibility = View.GONE
        progress.visibility = View.VISIBLE
        doAsync {
            news.clear()

            doc = connect("http://www.seafarersjournal.com")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .get()

            i = 0
            lastIndex = 2
            var j = 0

            while (i != max) {

                title = doc!!.select("body > div.container > div.row.parent_right_border > div.span7.left_menu > div:nth-child(1) > p:nth-child($lastIndex) > a")
                news.add(NewsClass(title!!.text(), title!!.attr("href")))

                if (news[i].title == to.for_news_state[j].title) {
                    runOnUiThread({
                        rv.visibility = View.VISIBLE
                        progress.visibility = View.GONE
                    })
                    i = max
                    break
                } else {
                    to.for_news_state.add(j, news[i])
                    runOnUiThread({
                        rv.visibility = View.VISIBLE
                        progress.visibility = View.GONE
                        })
                }

                lastIndex++
                j++
                i++
            }
            uiThread {
                rv.adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_refresh_news, menu)
    }

    var title: Elements? = null
    var doc: Document? = null

    fun loadNews() {

        news = ArrayList()

        doAsync {
            if (to.for_news_state.isEmpty()) {
                var lastIndex = 2
                doc = Jsoup.connect("http://www.seafarersjournal.com")
                        .userAgent("Mozilla")
                        .cookie("auth", "token")
                        .get()

                while (i != max) {
                    title = doc!!.select("body > div.container > div.row.parent_right_border > div.span7.left_menu > div:nth-child(1) > p:nth-child($lastIndex) > a")
                    if(title!!.text().isNotEmpty())
                    news.add(NewsClass(title!!.text(), title!!.attr("href")))
                    i++
                    lastIndex++
                }
                to.for_news_state.addAll(news)
            }
            uiThread {
                progress.visibility = View.GONE
                adapter = NewsAdapter(to.for_news_state)
                rv.adapter = adapter
                adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position -> chooseNews(position) }
            }

        }
    }


    fun chooseNews(p: Int)
    {
       to.link_i = p
       IFragmentActions.openNewFragment(NewsDetailFragment(),"open")
    }

    fun refreshNews():Boolean
    {
        refreshData()
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item!!.itemId) {

        R.id.action_refresh -> { refreshNews()}

        else ->
            super.onOptionsItemSelected(item)
    }
}