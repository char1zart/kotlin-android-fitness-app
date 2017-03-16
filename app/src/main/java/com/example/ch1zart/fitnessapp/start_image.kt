package com.example.ch1zart.fitnessapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.ch1zart.fragments.ScreenSlidePagerActivity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import java.util.concurrent.TimeUnit

class start_image : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_imag)


        doAsync {
            TimeUnit.SECONDS.sleep(1)
            startActivity<ScreenSlidePagerActivity>()
            uiThread {
               // startActivity<ScreenSlidePagerActivity>()
            }
        }

    }
}
