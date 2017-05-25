package com.example.ch1zart.another

import android.app.Activity
import android.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.example.ch1zart.fitnessapp.R
import com.example.ch1zart.newsfeed.NewsFeedFragment
import com.example.ch1zart.position.PositionFragment
import com.example.ch1zart.notes.NotesFragment
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import org.jetbrains.anko.*
import com.example.ch1zart.theory.TheoryFragment
import com.example.ch1zart.userpack.PrivateInfo


class MainActivity : AppCompatActivity() , FragmentActions {

    lateinit var drawerResult: Drawer

    val toolbar by lazy {
        find<Toolbar>(R.id.toolbar_actionbar)
    }

    private val to = TransferObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)

       doAsync {
           replaceFragment(Loader())
       }
    }

    override fun initDrawer() {
        initDrawerMenu()
    }

    override fun openNewFragment(nf: Fragment) {
        replaceFragment(nf)
    }

    override fun openDrawer(activity: Activity,toolbar: Toolbar) {
       drawerResult.setToolbar(activity,toolbar,true)
    }

    override fun getArrowBack() {
        drawerResult.actionBarDrawerToggle.isDrawerIndicatorEnabled = false
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun replaceFragment(new_fragment: Fragment) {

            var fragment2 = new_fragment
            val fragmentManager = fragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment2)

            fragmentTransaction.commitAllowingStateLoss()
    }

    fun initDrawerMenu(): Boolean {
        DrawerBuilder().withActivity(this).build()
        drawerResult = DrawerBuilder()
                .withActivity(this)
                .withActionBarDrawerToggleAnimated(true)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggle(true)
                .withDisplayBelowStatusBar(true)
                .addDrawerItems(
                        //0
                        PrimaryDrawerItem().withName(R.string.menu_newsfeed).withIcon(GoogleMaterial.Icon.gmd_home),
                        PrimaryDrawerItem().withName(R.string.drawer_item_cabinet).withIcon(GoogleMaterial.Icon.gmd_account_box),
                        DividerDrawerItem(),
                        PrimaryDrawerItem().withName(R.string.menu_p_s).withIcon(GoogleMaterial.Icon.gmd_build),
                        PrimaryDrawerItem().withName(R.string.menu_position).withIcon(GoogleMaterial.Icon.gmd_event),
                        DividerDrawerItem(),
                        PrimaryDrawerItem().withName(R.string.drawer_item_note).withIcon(GoogleMaterial.Icon.gmd_equalizer),
                        DividerDrawerItem(),
                        PrimaryDrawerItem().withName(R.string.drawer_item_callme).withIcon(GoogleMaterial.Icon.gmd_feedback),
                        PrimaryDrawerItem().withName(R.string.drawer_item_rating).withIcon(GoogleMaterial.Icon.gmd_star))
                .withOnDrawerItemClickListener(Drawer.OnDrawerItemClickListener { view, position, drawerItem ->
                    when (position) {
                        0 -> {
                            openIt(NewsFeedFragment(),position)

                        }
                        1 -> {
                            if(to.haveUser)
                            openIt(PrivateInfo(),position)
                        }

                       3 -> {
                           openIt(TheoryFragment(),position)
                        }

                        4 -> {
                            openIt(PositionFragment(),position)
                        }

                        6 -> {
                            openIt(NotesFragment(),position)
                        }
                        8->  {
                            alert("Обратиться за помощью по номеру +38 098 677 33 77 или info@seafaresjournal.com", "Обратная связь") {
                                positiveButton("Да") { browse("https://vk.com/im?media=&sel=32764093/") }
                                negativeButton("Нет")
                            }.show()
                        }
                        9 -> {
                            alert("Оценить приложение?", "PlayMarket") {
                                positiveButton("Да") { browse("https://vk.com/im?media=&sel=32764093/") }
                                negativeButton("Нет")
                            }.show()
                        }
                    }
                    true
                }).build()

        _onStart()
        return true
    }

    override fun onBackPressed() {
    }

    fun _onStart()
    {
        openIt(NewsFeedFragment(),0)
    }

    fun openIt(f:Fragment, p: Int)
    {
        if(p != to.inMenu.get()) {
            doAsync {
                replaceFragment(f)
                to.inMenu.set(p)
                uiThread { drawerResult.closeDrawer() }
            }
        }
        else
        {
            drawerResult.closeDrawer()
        }
    }

}


