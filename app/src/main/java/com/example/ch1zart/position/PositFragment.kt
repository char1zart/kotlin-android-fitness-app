package com.example.ch1zart.position

import android.os.Bundle
import com.example.ch1zart.container.ContainerFragment
import com.example.ch1zart.dbconnector.SeaPositionsInfo
import com.example.ch1zart.emergency.EmergencycasesFragment
import com.example.ch1zart.fitnessapp.R

/**
 * Created by Ch1zART on 26.05.2017.
 */
class PositFragment: ContainerFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        to.current_fragment = PositFragment()
        to.fragment_state = R.string.menu_position
        toolbar.title = resources.getString(to.fragment_state)
        to.current_class = SeaPositionsInfo()
        to.container_state.addAll(to.position_initialization)
    }
}