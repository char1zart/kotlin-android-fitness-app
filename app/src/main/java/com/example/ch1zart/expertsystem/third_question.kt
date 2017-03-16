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

class third_question: Fragment() {
     val to = TransferObject
    lateinit var t_v1: TextView
    lateinit var t_alarm:TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.third_question, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()

        t_alarm = find<TextView>(R.id.t_alarm)
        t_v1 = find<TextView>(R.id.t_var1)

        YoYo.with(Techniques.BounceInUp)
                .duration(2000)
                .playOn(t_v1)

        val sum = to.step_2.get()

        when(sum)
        {
            50 -> {t_alarm.text = "Нагрузку подбирайте так, чтобы успевать восстанавливаться к следующей тренировке — если восстановление затягивается, снижайте нагрузку!"}
            150 ->{t_alarm.text = "Запомните и не забывайте, что наилучший эффект и пользу можно получить, сочетая систематические физические нагрузки со здоровым образом жизни и правильным питанием"}
            200 ->{t_alarm.text = "Особое внимание! При беге трусцой больше часа может возникнуть такой промежуток, когда организм уже исчерпал весь запас гликогена, а жир еще не начал расщепляться. В этот момент организм начинает сжигать легко расщепляемый мышечный белок"}
        }

        click_buttons()
    }

    fun click_buttons()
    {
        t_v1.setOnClickListener {
            next_window()
        }

    }

    fun next_window()
    {
        val fragment2 = fourth_question()
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container1,  fragment2).commit()
    }

}