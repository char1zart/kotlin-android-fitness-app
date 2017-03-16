package com.example.ch1zart.fragments

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.*
import com.dd.morphingbutton.MorphingButton
import com.example.ch1zart.dbconnector.AchivTable
import com.example.ch1zart.fitnessapp.R
import java.text.SimpleDateFormat
import java.util.*
import com.example.ch1zart.dbconnector.MyDatabaseOpenHelper
import com.example.ch1zart.dbconnector.StatistickTable
import com.example.ch1zart.dbconnector.TimerTable
import io.saeid.fabloading.LoadingView
import org.glassfish.jersey.client.ClientConfig
import org.jetbrains.anko.*
import org.jetbrains.anko.db.*
import org.jetbrains.anko.support.v4.*
import java.util.concurrent.TimeUnit
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

class ScreenSlidePageFragment : Fragment(){

   lateinit var mLoadingView:LoadingView
   lateinit var digital_t: TextView
   lateinit var km_text: TextView
   lateinit var step_text:TextView
   lateinit var out_btn:Button

   lateinit var rdBtn_yes:RadioButton
   lateinit var rdBtn_no:RadioButton
   lateinit var dig_info_t: TextView
   lateinit var dig_km: TextView
   lateinit var contin_text: TextView
   lateinit var finish_step_text: TextView
   lateinit var finish_kkal_text:TextView
   lateinit var input_text:EditText

   var mCurrentPeriod = 0.5f
   var tt = TimerTable()
   var st = StatistickTable()
   var at = AchivTable()
   lateinit var rootView:View

    var temp_km = 0
    var temp_step = 0
    var temp_kkl = 0

    lateinit var b_help: TextView
    var click = 0
    val to = TransferObject
    var timeInMillis = 0L
    // lateinit var t_map:TextView
    lateinit var MyLayout:LinearLayout
    lateinit var MainInfo:LinearLayout
    lateinit var Time: String

   lateinit var mDbHelper:MyDatabaseOpenHelper
   lateinit var dbW: SQLiteDatabase
   var startTime: Long = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
       rootView = inflater!!.inflate(
                R.layout.fragment_screen_slide_page, container, false) as ViewGroup
            mDbHelper = MyDatabaseOpenHelper(activity)

        return rootView
    }

    fun revanim(check:Boolean)    {
        val view = rootView
      //  val window = activity.window
        view.backgroundColor = R.color.colorPrimaryDark

       if(check) {
         //  window.statusBarColor = Color.parseColor("#5b99e8")
           view.backgroundColor =  R.color.colorPrimaryDark

           MyLayout.visibility = View.GONE
           (activity as AppCompatActivity).getSupportActionBar()!!.hide()

           val InfoLayout = find<LinearLayout>(R.id.hiddenInfo)
           InfoLayout.visibility = View.VISIBLE
       }

        else {
           //window.statusBarColor = Color.parseColor("#5b99e8")
           view.backgroundColor = Color.parseColor("#F8FCFC")

           MyLayout = find<LinearLayout>(R.id.hiddenInfo)
           MyLayout.visibility = View.GONE
           (activity as AppCompatActivity).supportActionBar!!.show()
           val InfoLayout = find<LinearLayout>(R.id.LinLayMain)
           InfoLayout.visibility = View.VISIBLE
        }
        val centerX = (view.getLeft() + view.getRight()) / 2
        val centerY = (view.getTop() + view.getBottom()) / 2

        val startRadius = 0f
        val endRadius = (Math.max(view.getWidth(), view.getHeight())).toFloat()
        val anim = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius)
        view.visibility = View.VISIBLE
        anim.start()
    }

    private fun FindMe()    {
       doAsync {
         var text = " "
         if (to.UserName.get() != "") {

             text = "{\"name\":\"${to.UserName.get()}\",\"message\":\"${input_text.text}\",\"latitude\":\"${to.UserLatitude.get()}\",\"longitude\":\"${to.UserLongitute.get()}\"}"
         } else {
             text = "{\"name\":\"anonym\",\"message\":\"${input_text.text}\",\"latitude\":\"${to.UserLatitude.get()}\",\"longitute\":\"${to.UserLongitute.get()}\"}"
         }

         val config = ClientConfig()
         var client = ClientBuilder.newClient(config)
         val target = client.target("http://420ef051.ngrok.io/rest/getmessage/post")
         target.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(text, "application/json; charset=UTF-8"), Response::class.java)
     }
    }

    var timerHandler = Handler()

   var timerRunnable: Runnable = object : Runnable {
        override fun run() {

            mCurrentPeriod+=0.51f
            var seconds = mCurrentPeriod.toLong()

            var tz = SimpleTimeZone(0, "Out Timezone")
            TimeZone.setDefault(tz)
            val temp = SimpleDateFormat("HH:mm:ss").format(Date(seconds  * 1000))
            digital_t.text = temp
            UpdateUI()
            timerHandler.postDelayed(this, 500)
        }
    }

    fun checkFind()    {
       FindMe()
       if(rdBtn_yes.isChecked) {
           doAsync {
               input_text.visibility =  View.GONE

               FindMe()
               uiThread {
                   toast("${R.string.drawer_item_sendtoserver}")
               }
           }
       }
    }

    fun disableLinlay()    {
        input_text.visibility = View.GONE
        rdBtn_yes.isEnabled = false
        rdBtn_no.isEnabled = false
        input_text.isEnabled = false
        out_btn.visibility = View.GONE

        toast(R.string.drawer_item_alert)

       doAsync {
           TimeUnit.SECONDS.sleep(320)
            uiThread {
              activateLinlay()
            }
        }    }

    fun activateLinlay() {
        rdBtn_yes.isEnabled = true
        rdBtn_no.isEnabled = true
        out_btn.visibility = View.VISIBLE
        input_text.isEnabled = true
        input_text.visibility = View.GONE
    }

    fun ClickToTraining()    {
       mLoadingView.setOnClickListener {

       if(!to.HaveUser.get())       {
           toast(R.string.drawer_item_tips_2)
       }
           //onstart
            if (click == 0)
            {
                toast("Тренировка началась!")
                digital_t.text = "00:00:00"
                mCurrentPeriod = 0.0f
                startTime = System.currentTimeMillis()
                timerHandler.postDelayed(timerRunnable, 0)

                StartTraining()
                to.all_sum_km.set(0)

                click++
            }

         else {
                SaveAllStats()
                timerHandler.removeCallbacks(timerRunnable)
                mCurrentPeriod = 0.0f
                revanim(true)
                val btnMorphSave = find<MorphingButton>(R.id.btnMorphSave)
                MainInfo.visibility = View.GONE
                MyLayout = find<LinearLayout>(R.id.LinLayMain)
                MyLayout.visibility = View.GONE
              //  t_map.visibility = View.GONE

                initMorp(btnMorphSave)

               doAsync {
                    SaveResult()
                    uiThread {
                        FinishTraining()
                        toast("Тренировка закончилась!")

                    }
                }
                click--
            }
           mLoadingView.startAnimation()

        }
    }

    fun infoOnFinish() {
      dig_info_t.text = Time
      dig_km.text =  km_text.text
      finish_step_text.text = step_text.text

      finish_kkal_text.text = (String.format("%(.2f",to.all_energy.get()))

        contin_text.setOnClickListener {
            MainInfo.visibility = View.VISIBLE
            // t_map.visibility = View.VISIBLE
            revanim(false)
            digital_t.text = "00:00:00"
            km_text.text = "0 м"
            step_text.text = "0"
            finish_kkal_text.text = "0.00"

            MyLayout = find<LinearLayout>(R.id.LinLayMain)
            MyLayout.visibility = View.VISIBLE

            rdBtn_yes.visibility = View.VISIBLE
            rdBtn_no.visibility = View.VISIBLE
            input_text.visibility = View.GONE
        }
    }

    fun initMorp(btnMorph1: MorphingButton) {
        btnMorph1.drawableNormal
        btnMorph1.setOnClickListener {
            /* val circle =
                    MorphingButton.Params.create()
                            .duration(450).cornerRadius(dimen(R.dimen.mb_height_56)) // 56 dp
                            .width(dimen(R.dimen.mb_height_56)) // 56 dp
                            .height(dimen(R.dimen.mb_height_56)) // 56 dp
                            .color(Color.parseColor("#5ee85b")) // normal state color
                            .colorPressed(Color.parseColor("#5be87b")) // pressed state color
                            .icon(R.drawable.ic_done) // icon
            btnMorph1.morph(circle)*/
            if (btnMorph1.isPressed) {
                btnMorph1.isEnabled = false

               doAsync {
                    dbW = mDbHelper.writableDatabase

                 var  to_str_energy = (String.format("%(.2f",to.all_energy.get()))

                   mDbHelper.SaveResultStatistick(dbW, GetCurrentDate() , to_str_energy, to.all_sum_km.get().toInt(), "${digital_t.text}", to.all_steps.get())
                              dbW.close()

                    uiThread {
                        revanim(false)

                        rdBtn_yes.visibility = View.VISIBLE
                        rdBtn_no.visibility = View.VISIBLE
                        input_text.visibility = View.GONE

                        digital_t.text= "00:00:00"
                        km_text.text= "0 м"
                        step_text.text = "0"

                        btnMorph1.isEnabled = true
                        MainInfo.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    fun GetCurrentDate():String    {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("d-M")
        val formattedDate = df.format(c.time)
        return formattedDate
    }

    override  fun onStart() {
        super.onStart()
            preload()
        }

fun preload()
{
    Time = ""
    b_help = find<TextView>(R.id.t_help)

    b_help.text = " ? "

    b_help.setOnClickListener {
        toast("Разрешение необходимо для отслеживания ваших координат в Компаньон")
    }

    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    if (enabled != true) {
        toast("Проверьте подключение к GPS!")
    }

    MainInfo = find<LinearLayout>(R.id.Lin_lay_1)
    out_btn = find<Button>(R.id.btn_out)
    out_btn.visibility = View.GONE

    out_btn.setOnClickListener {
        disableLinlay()
        checkFind()
    }

    MyLayout = find<LinearLayout>(R.id.LinLayMain)

    dig_km = find<TextView>(R.id.dig_km)
    digital_t = find<TextView>(R.id.tv_timer)
    step_text = find<TextView>(R.id.t_step)
    km_text = find<TextView>(R.id.id_km)

    input_text = find<EditText>(R.id.inputText)
    input_text.visibility = View.GONE

    mLoadingView = find<LoadingView>(R.id.loading_view)
    contin_text = find<TextView>(R.id.textcont)
    finish_step_text = find<TextView>(R.id.t_count_step)
    finish_kkal_text = find<TextView>(R.id.text_kkal)
    rdBtn_no = find<RadioButton>(R.id.rd_btn_no)
    rdBtn_yes = find<RadioButton>(R.id.rd_btn_yes)
    rdBtn_yes.visibility = View.VISIBLE
    rdBtn_no.visibility = View.VISIBLE

    rdBtn_no.isChecked = true

    rdBtn_yes.setOnClickListener {
        input_text.visibility = View.VISIBLE
        out_btn.visibility= View.VISIBLE
        rdBtn_no.isChecked = false }

    rdBtn_no.setOnClickListener {
        input_text.visibility = View.GONE
        out_btn.visibility= View.GONE
        rdBtn_yes.isChecked = false }

    if(CheckPressed() == true)        {
        click = 1
        timerHandler.postDelayed(timerRunnable, 0)
        GetCurrentTime()
    }
    else {
        click = 0
    }

    dig_info_t = find<TextView>(R.id.dig_t)
    dig_km = find<TextView>(R.id.dig_km)
    km_text.text = (to.all_sum_km.get().toString() + " м")
    km_text.text = "0 м"

    ClickToTraining()
    LoadViewAnim()
}

    //кнопка
    fun LoadViewAnim()    {
        mLoadingView
                .addAnimation(Color.parseColor("#858585"), R.drawable.run1, LoadingView.FROM_BOTTOM)
                .addAnimation(Color.parseColor("#4d504c"), R.drawable.run2, LoadingView.FROM_TOP)
        mLoadingView.addListener(object : LoadingView.LoadingListener {
            override fun onAnimationStart(currentItemPosition: Int) {
            }

            override  fun onAnimationRepeat(nextItemPosition: Int) {
            }

            override   fun onAnimationEnd(nextItemPosition: Int) {
            }
        })
    }

    fun SaveResult() {
        doAsync {
            dbW = mDbHelper.writableDatabase
            if(dbW.select(at.table_name, at.title).where(at.id + " = {userid}", "userid" to 2).parseSingle(StringParser) != "true") {
                dbW.update(at.table_name, at.status to "true").where(at.id + "  = {userid}", "userid" to 2).exec()
            }
            dbW.close()
            mCurrentPeriod = 0.0f
        }
    }

    fun RememberTimeStat()    {
        doAsync {
            timeInMillis = System.currentTimeMillis()
            var timeInSec = timeInMillis / 1000
            dbW = mDbHelper.writableDatabase
            dbW.update(tt.table_name, tt.time to timeInSec).where(tt.id + "  = {userid}", "userid" to 1).exec()
            dbW.close()
        }
    }

    fun SaveAllStats()    {
        doAsync {
            dbW = mDbHelper.writableDatabase
            val get_step= dbW.select(st.table_name,st.step).where(st.id + "  = {userid}", "userid" to 1).parseSingle(IntParser)
            val get_kkl = dbW.select(st.table_name,st.kkal).where(st.id + "  = {userid}", "userid" to 1).parseSingle(FloatParser)
            val get_km = dbW.select(st.table_name,st.km).where(st.id + "  = {userid}", "userid" to 1).parseSingle(IntParser)
            dbW.update(st.table_name, st.step to get_step+temp_step, st.kkal to get_kkl+temp_kkl, st.km to get_km + temp_km).where(st.id + "  = {userid}", "userid" to 1).exec()

            dbW.close()
        }
    }

    fun CheckPressed():Boolean{
        var bool: Boolean
            dbW = mDbHelper.writableDatabase
            val get_pressed = dbW.select(tt.table_name, tt.pressed).where(tt.id + "  = {userid}", "userid" to 1).parseSingle(StringParser)

            if (get_pressed == "true") {
                          bool = true
            } else {
               dbW.update(tt.table_name, tt.time to 0).where(tt.id + "  = {userid}", "userid" to 1).exec()
               bool = false
            }
        dbW.close()
        return bool
    }

    fun StartTraining() {
            to.start_btn.set(true)
            doAsync {
                dbW = mDbHelper.writableDatabase
                dbW.update(tt.table_name, tt.pressed to "true").where(tt.id + "  = {userid}", "userid" to 1).exec()
                dbW.close()
                uiThread { RememberTimeStat() }
                    }
    }

    fun FinishTraining()   {
        doAsync {
            dbW = mDbHelper.writableDatabase
            dbW.update(tt.table_name, tt.pressed to "false").where(tt.id + "  = {userid}", "userid" to 1).exec()
            dbW.close()

        uiThread {
            to.finish_btn.set(true)
            to.start_btn.set(false)
            GetCurrentTime()
            }
        }
    }

    fun GetCurrentTime()   {
          doAsync {
            dbW = mDbHelper.writableDatabase
            var get_time = dbW.select(tt.table_name, tt.time).where(tt.id + "  = {userid}", "userid" to 1).parseSingle(IntParser)
            dbW.close()

            var tz = SimpleTimeZone(0, "Out Timezone")
            TimeZone.setDefault(tz)

            var CurrentTime  = System.currentTimeMillis()
            var CurrentTimeSec = CurrentTime / 1000
            var range = CurrentTimeSec - get_time

            mCurrentPeriod = range.toFloat()
            val temp = SimpleDateFormat("HH:mm:ss").format(Date(range * 1000))

            uiThread {

                Time = "$temp"
               digital_t.text = Time
                infoOnFinish()
            }
        }
    }

    fun UpdateUI()    {
        step_text.text= "${to.all_steps.get()}"
        temp_step = to.all_steps.get()

        km_text.text= "${to.all_sum_km.get()} м"
        temp_km = to.all_sum_km.get().toInt()
    }

    override fun onPause() {
        super.onPause()
        timerHandler.removeCallbacks(timerRunnable)
    }

   override fun onDestroy() {
        super.onDestroy()
       timerHandler.removeCallbacks(timerRunnable)
       FinishTraining()
    }
 }
