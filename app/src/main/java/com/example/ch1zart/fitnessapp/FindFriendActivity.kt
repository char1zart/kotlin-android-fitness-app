package com.example.ch1zart.fitnessapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.example.ch1zart.achievements.AchivActivity
import com.example.ch1zart.achievements.RecyclerItemClickListener
import com.example.ch1zart.dbconnector.MyDatabaseOpenHelper
import com.example.ch1zart.expertsystem.ExpertSystem
import com.example.ch1zart.findfriend.Connected
import com.example.ch1zart.findfriend.FF_Adapter
import com.example.ch1zart.findfriend.FindFriend
import com.example.ch1zart.fragments.ScreenSlidePagerActivity
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import org.jetbrains.anko.*
import org.json.JSONArray
import java.util.*

class FindFriendActivity : AppCompatActivity() {

    lateinit var drawerResult: Drawer
    private lateinit var users: MutableList<FindFriend>
    private lateinit var rv: RecyclerView
    private lateinit var t_count:TextView
    var mystring = ""
    lateinit var toolbar:Toolbar

    lateinit var mDbHelper: MyDatabaseOpenHelper
    lateinit var dbW: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_friend)
        initDrawerMenu()

        mDbHelper = MyDatabaseOpenHelper(this)

        mystring = resources.getString(R.string.drawer_with_me)
        t_count = find<TextView>(R.id.t_wit_me)
        t_count.text = mystring + "0"

        rv = find<RecyclerView>(R.id.revec)
        val llm = LinearLayoutManager(this)
        rv.layoutManager = llm
        rv.setHasFixedSize(true)

        rv.addOnItemTouchListener(
                RecyclerItemClickListener(ctx, object : RecyclerItemClickListener.OnItemClickListener {
                    override  fun onItemClick(view: View, position: Int) {

                        if(position == 0)
                        {
                            updateListView()
                        }
                    }
                }))

        initializeData()
        initializeAdapter()
    }

    fun updateData() {
        doAsync {

            var json = Connected.getJSON()
            uiThread {
              renderDate(json)
            }
        }
    }

   private fun renderDate(json: JSONArray?) {

       doAsync {
           val count = json!!.length()

           users.clear()
            uiThread {
                users.add(FindFriend("", "Обновить", ""))
               var i = 0

                if(count == 0)
                {
                    mystring = resources.getString(R.string.drawer_with_me)
                    t_count.text = mystring + "0"

                    toast("Рядом никого нет")
                }
                else {
                    while (i != count) {

                    users.add(FindFriend(json!!.getJSONObject(i).getString("name"), json!!.getJSONObject(i).getString("message"), json!!.getJSONObject(i).getString("date")))
                        i++
                    }
                    t_count.text = mystring + count
                    initializeAdapter()
                }
            }
       }
   }

    fun initDrawerMenu() {
        DrawerBuilder().withActivity(this).build()
        toolbar = find<Toolbar>(R.id.toolbar)
        toolbar.title = getResources().getString(R.string.drawer_item_find_friend)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationIcon(R.drawable.calories)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override   fun onClick(v: View) {
                onBackPressed()
            }
        })

        drawerResult = DrawerBuilder().withFullscreen(false)
                .withActivity(this)
                .withActionBarDrawerToggleAnimated(true)
                .withTranslucentStatusBar(true)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .withDisplayBelowStatusBar(true)
                .addDrawerItems(
                        PrimaryDrawerItem().withName(R.string.drawer_item_training).withIcon(GoogleMaterial.Icon.gmd_directions_run),
                        PrimaryDrawerItem().withName(R.string.drawer_item_achiv).withIcon(GoogleMaterial.Icon.gmd_beenhere),
                        PrimaryDrawerItem().withName(R.string.drawer_item_stat).withIcon(GoogleMaterial.Icon.gmd_equalizer),
                        PrimaryDrawerItem().withName(R.string.drawer_item_find_friend).withIcon(GoogleMaterial.Icon.gmd_location_on),
                        PrimaryDrawerItem().withName(R.string.drawer_item_expert_system).withIcon(GoogleMaterial.Icon.gmd_contacts),
                        DividerDrawerItem(),
                        PrimaryDrawerItem().withName(R.string.drawer_item_cabinet).withIcon(GoogleMaterial.Icon.gmd_description),
                        DividerDrawerItem(),
                        PrimaryDrawerItem().withName(R.string.drawer_item_callme).withIcon(GoogleMaterial.Icon.gmd_feedback),
                        PrimaryDrawerItem().withName(R.string.drawer_item_rating).withIcon(GoogleMaterial.Icon.gmd_star))
                .withOnDrawerItemClickListener(Drawer.OnDrawerItemClickListener { view, position, drawerItem ->
                    when (position) {
                        -1 -> {
                            startActivity<RegFragment>()
                        }
                        1 -> {
                            startActivity<ScreenSlidePagerActivity>()
                        }
                        2 -> {

                            startActivity<AchivActivity>()
                        }
                        3 -> {
                            startActivity<StatisticActivity>()
                        }
                        4 -> {
                            startActivity<FindFriendActivity>()
                        }
                        5 -> {

                            doAsync {
                                dbW = mDbHelper.writableDatabase
                                val user_GetName = mDbHelper.GetNameUserTable(dbW)
                                val user_haveQuest =  mDbHelper.GetQUEST(dbW)


                                dbW.close()


                                uiThread {

                                    var can = false
                                    if(user_haveQuest == 0) {
                                        can = true


                                        if (user_GetName == "Unreg" && can) {
                                            toast(R.string.drawer_item_tips_1)
                                            drawerResult.setSelection(0)

                                        } else {
                                            startActivity<ExpertSystem>()
                                        }
                                    }
                                    else {
                                        toast("Завершите существующий курс")
                                    }
                                }
                            }
                        }
                        7 -> {
                            doAsync {
                                dbW = mDbHelper.writableDatabase
                                val user_GetName= mDbHelper.GetNameUserTable(dbW)

                                dbW.close()
                                uiThread {

                                    if(user_GetName == "Unreg"){
                                        toast(R.string.drawer_item_tips_1)
                                        drawerResult.setSelection(0)
                                    }
                                    else    {
                                        startActivity<PrivateInfo>()
                                    }


                                }

                            }
                        }
                        9 -> {
                            alert("Написать мне что-то?", "Обратная связь") {
                                positiveButton("Да") { browse("https://vk.com/im?media=&sel=32764093/")}
                                negativeButton("Нет") { toast("Будет время, напиши :)")}
                            }.show()
                        }
                    }
                    true
                }).build()

        drawerResult.setSelection(0)
    }

    fun updateListView() {

        doAsync {
            updateData()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.getActiveNetworkInfo()
        return activeNetworkInfo != null
    }

    override fun onStart() {
        super.onStart()

        if(!isNetworkAvailable())
        {
            toast("Проверьте подключение к сети!")
        }
    }

    fun initializeData() {

            users = ArrayList()
         users.add(FindFriend("", "Обновить", ""))
    }

    private fun initializeAdapter() {
        rv.clearOnScrollListeners()

        val adapter =  FF_Adapter(users)

        rv.setAdapter(adapter)
        }
    }

