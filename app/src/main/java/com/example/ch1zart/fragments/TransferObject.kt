package com.example.ch1zart.fragments

import org.jetbrains.annotations.Mutable
import java.util.concurrent.atomic.*

/**
 * Created by Ch1zART on 19.10.2016.
 */
object TransferObject {

    val speedpersec = AtomicLong(0)
    val start_btn = AtomicBoolean(false)
    val get_current_time = AtomicReference<String>(" ")
    val get_current_speed = AtomicReference<Float>(0f)
    val UserLatitude = AtomicReference<Float>(0f)
    val UserLongitute = AtomicReference<Float>(0f)
    val UserName = AtomicReference<String>("")

    val finish_btn = AtomicBoolean(false)
    val all_energy = AtomicReference<Float>(0f)
    val all_sum_km = AtomicLong(0)
    val all_steps = AtomicInteger(0)
    val bIsStop = AtomicBoolean(false)
    val HaveUser = AtomicBoolean(false)

    val tips = AtomicReference<String>("")
    val step_2 = AtomicInteger(0)


    fun clearExpert()
    {
        step_2.set(0)
        tips.set("")
    }

    fun clearVariables() {
       speedpersec.set(0)
       start_btn.set(false)
       finish_btn.set(false)
       all_sum_km.set(0)
       all_steps.set(0)
       bIsStop.set(false)
    }
}