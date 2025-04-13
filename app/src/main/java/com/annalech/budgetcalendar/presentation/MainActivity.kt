package com.annalech.budgetcalendar.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.annalech.budgetcalendar.R
import com.annalech.budgetcalendar.databinding.ActivityMainBinding
import com.annalech.budgetcalendar.presentation.viewmodels.ProfileViewMoodel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navigateController: NavController
    private val profileViewMoodel: ProfileViewMoodel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkProfileData()

        //add navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navigateController = navHostFragment.findNavController()


        binding.bottomNavBar.setupWithNavController(navigateController)


    }

    //add menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    //обработка пунктов меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navigateController) || super.onOptionsItemSelected(item)
    }

    private fun checkProfileData() {
        profileViewMoodel.receivedProfileLiveData.observe(this){it->
            if (it.isEmpty()){
                navigateController.navigate(R.id.action_global_profileFragment)
            }
        }
    }
}