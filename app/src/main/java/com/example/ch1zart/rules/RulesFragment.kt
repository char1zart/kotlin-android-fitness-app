package com.example.ch1zart.rules

import android.os.Bundle
import com.example.ch1zart.container.ContainerFragment
import com.example.ch1zart.dbconnector.Rules
import com.example.ch1zart.emergency.EmergencycasesFragment
import com.example.ch1zart.fitnessapp.R

/**
 * Created by Ch1zART on 26.05.2017.
 */
class RulesFragment: ContainerFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        to.current_fragment = RulesFragment()
        to.fragment_state = R.string.menu_rules
        toolbar.title = resources.getString(to.fragment_state)
        to.current_class = Rules()
        to.container_state.addAll(to.rules_initialization)
    }
}