package com.example.ch1zart.fitnessapp

import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.ch1zart.achievements.AchivActivity
import com.example.ch1zart.achievements.RC_Adapter
import com.example.ch1zart.achievements.RecyclerItemClickListener
import com.example.ch1zart.achievements.Timer
import com.example.ch1zart.dbconnector.MyDatabaseOpenHelper
import com.example.ch1zart.dbconnector.StatistickTable
import com.example.ch1zart.dbconnector.TimerTable
import com.example.ch1zart.expertsystem.ExpertSystem
import com.example.ch1zart.fragments.ScreenSlidePageFragment
import com.example.ch1zart.fragments.ScreenSlidePageFragment2
import com.example.ch1zart.fragments.ScreenSlidePagerActivity
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import org.jetbrains.anko.*
import org.jetbrains.anko.db.FloatParser
import org.jetbrains.anko.db.IntParser
import org.jetbrains.anko.db.StringParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.support.v4.toast
import java.util.*

class StatisticActivity : AppCompatActivity() {

    lateinit var drawerResult: Drawer
    lateinit var mDbHelper: MyDatabaseOpenHelper
    lateinit var dbW: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)

        mDbHelper = MyDatabaseOpenHelper(this)
        openHistoryfragment()
    }

    override fun onStart() {
        super.onStart()

        createSpinnerAdapter()
        initDrawerMenu()
    }

    fun createSpinnerAdapter() {
        val spinner = find<Spinner>(R.id.stat_spinner)

        spinner.getBackground().setColorFilter(Color.parseColor("#F8FCFC"), PorterDuff.Mode.SRC_ATOP)
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.menu_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.setAdapter(adapter)

        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        position: Int, id: Long) {
                when(position) {
                  0->  openHistoryfragment()
                  1->  openGraphfragment()
                  2->  openQuestfragment()
                }

            }

            override   fun onNothingSelected(arg0: AdapterView<*>) {
            }
        })
    }

    fun openQuestfragment() {

        val fragment2 = quest_fragment()
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container,  fragment2).commit()
    }

    fun openGraphfragment() {

        val fragment2 = HistoryTrainingFragment()
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container,  fragment2).commit()
    }

    fun openHistoryfragment() {

        val fragment2 = PlusOneFragment()
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container,  fragment2).commit()
    }

    fun initDrawerMenu() {

        DrawerBuilder().withActivity(this).build()

        val toolbar = find<Toolbar>(R.id.toolbar)
        toolbar.title = getResources().getString(R.string.drawer_item_stat)

        this.setSupportActionBar(toolbar)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar!!.setDisplayShowHomeEnabled(true)
        this.supportActionBar!!.setDisplayShowTitleEnabled(false)


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

        drawerResult.setSelection(4)
    }

}


