package com.example.ch1zart.achievements

import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import com.example.ch1zart.dbconnector.AchivTable
import com.example.ch1zart.dbconnector.MyDatabaseOpenHelper
import com.example.ch1zart.expertsystem.ExpertSystem
import com.example.ch1zart.fitnessapp.*
import com.example.ch1zart.fragments.ScreenSlidePagerActivity
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import org.jetbrains.anko.*
import org.jetbrains.anko.db.StringParser
import org.jetbrains.anko.db.select

import java.util.*


class AchivActivity : AppCompatActivity() {

    var tt = AchivTable()

    lateinit var mDbHelper: MyDatabaseOpenHelper
    lateinit var dbW: SQLiteDatabase
    private lateinit var persons: MutableList<Timer>
    private lateinit var rv: RecyclerView
    private lateinit var drawerResult:Drawer

    var ind = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achiv)
        initDrawerMenu()
        mDbHelper = MyDatabaseOpenHelper(ctx)
        rv = find<RecyclerView>(R.id.rv)

        val llm = LinearLayoutManager(this)
        rv.layoutManager = llm
        rv.setHasFixedSize(true)

        rv.addOnItemTouchListener(
                RecyclerItemClickListener(ctx, object : RecyclerItemClickListener.OnItemClickListener {
                 override  fun onItemClick(view: View, position: Int) {
                     toast("элемент $position")
                    }
                }))

            initializeData()
            initializeAdapter()
    }

   fun initializeData() {

             dbW = mDbHelper.writableDatabase
             //плохой способ
             var parse_status = dbW.select(tt.table_name, tt.status).parseList(StringParser)
             var parse_title = dbW.select(tt.table_name, tt.title).parseList(StringParser)
             var parse_desc = dbW.select(tt.table_name, tt.description).parseList(StringParser)
             dbW.close()

             persons = ArrayList()

             while (ind != parse_title.count()) {
                 persons.add(Timer(parse_title.get(ind), parse_status.get(ind), parse_desc.get(ind), R.drawable.locked))
                 ind++
             }
    }

    private fun initializeAdapter() {
        val adapter =  RC_Adapter(persons)
        rv.setAdapter(adapter)
    }

    fun initDrawerMenu() {
        DrawerBuilder().withActivity(this).build()

        val toolbar = find<Toolbar>(R.id.toolbar)
        toolbar.title = getResources().getString(R.string.drawer_item_achiv)
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)

        drawerResult = DrawerBuilder()
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
                            finish()
                            startActivity<RegFragment>()
                        }
                        1 -> {
                            finish()
                            startActivity<ScreenSlidePagerActivity>()
                        }
                        2 -> {
                            finish()
                            startActivity<AchivActivity>()
                        }
                        3 -> {
                            finish()
                            startActivity<StatisticActivity>()
                        }
                        4 -> {
                            finish()
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


        drawerResult.setSelection(3)

    }

}
