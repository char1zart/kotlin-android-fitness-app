package com.example.ch1zart.another

import android.app.Activity
import android.app.Fragment
import android.support.v7.widget.Toolbar

/**
 * Created by Ch1zART on 18.05.2017.
 */
interface FragmentActions {
    fun openNewFragment(nf: Fragment)

    fun openDrawer(activity:Activity ,toolbar: Toolbar)

    fun initDrawer()
    fun getArrowBack()
}