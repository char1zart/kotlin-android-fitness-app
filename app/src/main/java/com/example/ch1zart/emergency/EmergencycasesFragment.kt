package com.example.ch1zart.emergency

import android.os.Bundle
import com.example.ch1zart.container.ContainerFragment
import com.example.ch1zart.dbconnector.Emergencycases
import com.example.ch1zart.fitnessapp.R
import org.jetbrains.anko.toast

/**
 * Created by Ch1zART on 26.05.2017.
 */
class EmergencycasesFragment: ContainerFragment() {


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        to.current_fragment = EmergencycasesFragment()
        to.fragment_state = R.string.menu_emergencycases
        toolbar.title = resources.getString(to.fragment_state)
        to.current_class = Emergencycases()

        to.container_state.addAll(to.emergen_initialization)
    }
}