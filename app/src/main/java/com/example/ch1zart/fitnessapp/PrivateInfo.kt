package com.example.ch1zart.fitnessapp

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.example.ch1zart.achievements.AchivActivity
import com.example.ch1zart.dbconnector.MyDatabaseOpenHelper
import com.example.ch1zart.dbconnector.UserInfo
import com.example.ch1zart.expertsystem.ExpertSystem
import com.example.ch1zart.fragments.ScreenSlidePagerActivity
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import org.jetbrains.anko.*
import org.jetbrains.anko.db.IntParser
import org.jetbrains.anko.db.StringParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update

class PrivateInfo : AppCompatActivity() {

    val TempStorage: MutableList<String> = arrayListOf()
    val ut = UserInfo()

    lateinit var drawerResult: Drawer
    lateinit var toolbar:Toolbar

    lateinit var NameText: TextView
    lateinit var AgeText: TextView
    lateinit var SexText: TextView
    lateinit var HText: TextView
    lateinit var WText: TextView
    lateinit var mDbHelper: MyDatabaseOpenHelper
    lateinit var dbW: SQLiteDatabase
    var click = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_info)

        initDrawerMenu()

        NameText = find<TextView>(R.id.t1)
        AgeText = find<TextView>(R.id.t2)
        SexText = find<TextView>(R.id.t3)
        HText = find<TextView>(R.id.t4)
        WText = find<TextView>(R.id.t5)

        mDbHelper = MyDatabaseOpenHelper(this)

        getUserInfo()

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
        click++
            if(click == 1)
               EditUserInfo()

            else {
                click--
                SaveUserInfo()
            }
           // view -> Snackbar.make(view, R.string.drawer_item_tips_2, Snackbar.LENGTH_LONG).setAction("Action", null).show()
               }
    }


    fun getUserInfo()
    {
       CloseEditInfo()

       TempStorage.clear()

       doAsync {
            dbW = mDbHelper.writableDatabase

           var get_name = dbW.select(ut.table_name, ut.name).where(ut.id + "  = {userid}", "userid" to 1).parseSingle(StringParser)
           var get_age = dbW.select(ut.table_name, ut.age).where(ut.id + "  = {userid}", "userid" to 1).parseSingle(IntParser)
           var get_HT = dbW.select(ut.table_name, ut.height).where(ut.id + "  = {userid}", "userid" to 1).parseSingle(IntParser)
           var get_Sex = dbW.select(ut.table_name, ut.sex).where(ut.id + "  = {userid}", "userid" to 1).parseSingle(StringParser)
           var get_WT = dbW.select(ut.table_name, ut.weight).where(ut.id + "  = {userid}", "userid" to 1).parseSingle(IntParser)


            dbW.close()


            uiThread {

              NameText.text = get_name
              AgeText.text = "$get_age"
              SexText.text = get_Sex
              HText.text = "$get_HT"
              WText.text ="$get_WT"

                //EditText.setFocusable(false) to disable editing
              //  EditText.setFocusableInTouchMode(true)
            }
        }
      //  toast("заглушка")
    }

    fun CloseEditInfo()
    {
        NameText.isFocusable = false
        AgeText.isFocusable = false
        SexText.isFocusable = false
        HText.isFocusable = false
        WText.isFocusable = false
    }


    fun EditUserInfo()
    {
        NameText.isFocusableInTouchMode = true
        AgeText.isFocusableInTouchMode  =  true
        SexText.isFocusableInTouchMode  =  true
        HText.isFocusableInTouchMode  =  true
        WText.isFocusableInTouchMode  =  true


        toast("Редактируй!")
    }

    fun SaveUserInfo()
    {
        TempStorage.add(0,"${NameText.text}")
        TempStorage.add(1,"${AgeText.text}")
        TempStorage.add(2,"${HText.text}")
        TempStorage.add(3,"${WText.text}")
        TempStorage.add(4,"${SexText.text}")

        CloseEditInfo()

        doAsync {

            dbW = mDbHelper.writableDatabase

            dbW.update(ut.table_name, ut.name to TempStorage.get(0),
                    ut.age to TempStorage.get(1), ut.sex to TempStorage.get(4), ut.height to TempStorage.get(2),
                    ut.weight to TempStorage.get(3)).where(ut.id + "  = {userid}", "userid" to 1).exec()

            dbW.close()

        uiThread {
            toast("Результат сохранен!") }
        }
    }



    fun initDrawerMenu() {
        DrawerBuilder().withActivity(this).build()

        toolbar = find<Toolbar>(R.id.toolbar)
        toolbar.title = getResources().getString(R.string.drawer_item_cabinet)
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

      /*  doAsync {
            dbW = mDbHelper.writableDatabase
            val user_GetName= mDbHelper.GetNameUserTable(dbW)
            dbW.close()

            uiThread {
                if(!user_GetName.isEmpty())
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
        }*/
    }
}
