package com.example.ch1zart.expertsystem

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.ch1zart.fitnessapp.R
import com.example.ch1zart.fragments.TransferObject
import org.jetbrains.anko.find


class second_question : Fragment() {
    val to = TransferObject
    lateinit var t_v1: TextView
    lateinit var t_v2: TextView
    lateinit var t_v3: TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.second_question, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()

        t_v1 = find<TextView>(R.id.t_var_1)
        t_v2 = find<TextView>(R.id.t_var_2)
        t_v3 = find<TextView>(R.id.t_var_3)

        YoYo.with(Techniques.BounceInUp)
                .duration(2000)
                .playOn(t_v1)

        YoYo.with(Techniques.BounceInUp)
                .duration(2000)
                .playOn(t_v2)

        YoYo.with(Techniques.BounceInUp)
                .duration(2000)
                .playOn(t_v3)



        click_buttons()

    }

    fun click_buttons()
    {
        t_v2.setOnClickListener {

            to.step_2.set(150)
            next_window()
        }

        t_v1.setOnClickListener {
            to.step_2.set(50)
            next_window()
        }

        t_v3.setOnClickListener {
            to.step_2.set(200)
            next_window()
        }

    }

    fun next_window()
    {
        val fragment2 = third_question()
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container1,  fragment2).commit()
    }

}