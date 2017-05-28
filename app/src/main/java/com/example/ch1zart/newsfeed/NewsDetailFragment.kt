package com.example.ch1zart.newsfeed


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import com.example.ch1zart.another.MainFragment
import com.example.ch1zart.fitnessapp.R
import org.jetbrains.anko.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements




@Suppress("DEPRECATION", "CAST_NEVER_SUCCEEDS")
class NewsDetailFragment : MainFragment() {

    val title by lazy {
        find<TextView>(R.id.te_title)
    }

    val detail by lazy {
        find<TextView>(R.id.te_detail)
    }

    val progress by lazy {
        find<ProgressBar>(R.id.p_progressBar)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_newsdetail, container, false)
        setHasOptionsMenu(true)
        retainInstance = true

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.title = resources.getString(R.string.menu_newsfeed)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        IFragmentActions.getArrowBack()

        toolbar.setNavigationOnClickListener {
            IFragmentActions.openNewFragment(NewsFeedFragment(),"back")
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadNews()
    }

    var description: Elements? = null
    var doc: Document? = null

    fun loadNews() {

        title.text = to.for_news_state[to.link_i].title

        doAsync {

            doc = Jsoup.connect("http://www.seafarersjournal.com${to.for_news_state[to.link_i].link}")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .get()

            description = doc!!.select("body > div.container > div.row.parent_right_border > div.span18.right_border > div.news_view > p")

            uiThread {
                detail.text = description!!.text()
                progress.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_link_to, menu)
    }

    fun linkTo():Boolean
    {
        browse("http://www.seafarersjournal.com${to.for_news_state[to.link_i].link}")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item!!.itemId) {
        R.id.action_linkto -> { linkTo()}

        else ->
            super.onOptionsItemSelected(item)
    }

}