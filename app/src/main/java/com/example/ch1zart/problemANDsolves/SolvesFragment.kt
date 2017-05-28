package com.example.ch1zart.problemANDsolves

import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.ch1zart.container.ContainerFragment
import com.example.ch1zart.dbconnector.Theory
import com.example.ch1zart.emergency.EmergencycasesFragment
import com.example.ch1zart.fitnessapp.R
import org.jetbrains.anko.toast

/**
 * Created by Ch1zART on 26.05.2017.
 */
class SolvesFragment: ContainerFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        to.current_fragment = SolvesFragment()
        to.fragment_state = R.string.menu_p_s
        toolbar.title = resources.getString(to.fragment_state)
        to.current_class = Theory()
        to.container_state.addAll(to.theory_initialization)
    }
}