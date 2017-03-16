package com.example.ch1zart.fragments




import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.PagerTitleStrip
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.View
import com.example.ch1zart.achievements.AchivActivity
import com.example.ch1zart.dbconnector.MyDatabaseOpenHelper
import com.example.ch1zart.expertsystem.ExpertSystem
import com.example.ch1zart.fitnessapp.*
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import org.jetbrains.anko.*
import java.util.concurrent.TimeUnit

class ScreenSlidePagerActivity : AppCompatActivity()  {
    private var mPager: ViewPager? = null
    lateinit var drawerResult: Drawer
    lateinit var toolbar:Toolbar

    lateinit var mDbHelper: MyDatabaseOpenHelper
    lateinit var dbW: SQLiteDatabase
    var name = " "

    private var mPagerAdapter: PagerAdapter? = null
    var position_p = 0
    val to = TransferObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_slide)

        mDbHelper = MyDatabaseOpenHelper(this)

        val pagerTitleStrip = find<PagerTitleStrip>(R.id.pager_title_strip)
        pagerTitleStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

                initDrawerMenu()
                mPager = find<ViewPager>(R.id.pager)
                mPagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
                mPager!!.adapter = mPagerAdapter


    }


    override fun onBackPressed() {
            if (mPager!!.currentItem == 0) {
            super.onBackPressed()
        }
        if (mPager!!.currentItem != 0)
            mPager!!.currentItem = mPager!!.currentItem - 1

    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            position_p = position

            when (position) {
                0 -> return ScreenSlidePageFragment()
                1 -> return ScreenSlidePageFragment2()
            }
            return ScreenSlidePageFragment()
        }
        override fun getCount(): Int {
            return NUM_PAGES
        }

      override  fun getPageTitle(position: Int): CharSequence {

          var text = " "
          when (position) {
              0 -> text = getResources().getString(R.string.drawer_panel)
              1 -> text = getResources().getString(R.string.drawer_map_1)
          }


            return text
        }

    }

    companion object {
        private val NUM_PAGES = 2
    }

    fun initDrawerMenu() {
        DrawerBuilder().withActivity(this).build()

        toolbar = find<Toolbar>(R.id.toolbar)
        toolbar.title = getResources().getString(R.string.drawer_item_training)
        toolbar.navigationIcon = ContextCompat.getDrawable(ctx, R.drawable.calories)
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

        doAsync {
            dbW = mDbHelper.writableDatabase
            val user_GetName= mDbHelper.GetNameUserTable(dbW)
            dbW.close()

            uiThread {
               if(user_GetName != "Unreg")
                {
                    val hello_string = resources.getString(R.string.drawer_hello_name)
                    name = hello_string + (" $user_GetName!")
                    TransferObject.UserName.set("$user_GetName")
                    drawerResult.addStickyFooterItem(PrimaryDrawerItem().withName(name))

                    if(to.UserName.get() != "Unreg")
                    {
                        to.HaveUser.set(true)
                    }
                    else {
                    to.HaveUser.set(false)
                    }
                }

                else drawerResult.addStickyFooterItem(PrimaryDrawerItem().withName(R.string.drawer_item_reg))
            }
        }
    }
}