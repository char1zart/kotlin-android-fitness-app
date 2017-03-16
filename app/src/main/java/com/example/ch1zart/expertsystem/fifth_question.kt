package com.example.ch1zart.expertsystem

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.ch1zart.fitnessapp.R
import com.example.ch1zart.fragments.ScreenSlidePagerActivity
import com.example.ch1zart.fragments.TransferObject
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity



class fifth_question : Fragment() {

    lateinit var finish_b: TextView
    lateinit var LL_vert_anim:LinearLayout
    lateinit var t_tips:TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fifth_question, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()

        t_tips = find<TextView>(R.id.t_tip)
        LL_vert_anim = find<LinearLayout>(R.id.LL_vert)

        t_tips.text = TransferObject.tips.get()




        YoYo.with(Techniques.FadeInUp)
                .duration(500)
                .playOn(LL_vert_anim)

        finish_b = find<TextView>(R.id.b_finish)

        finish_b.setOnClickListener {
       startActivity<ScreenSlidePagerActivity>()
        }

    }
}