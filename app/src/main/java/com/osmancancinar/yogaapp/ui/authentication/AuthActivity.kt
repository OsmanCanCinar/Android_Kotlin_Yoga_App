package com.osmancancinar.yogaapp.ui.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.ActivityAuthBinding
import com.osmancancinar.yogaapp.utils.notification.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_YogaApp)
        setContentView(binding.root)

        setSupportActionBar(binding.myToolBar)

        navController = Navigation.findNavController(this, R.id.fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        val notificationTime = GregorianCalendar.getInstance().timeInMillis as Long
        NotificationUtils().setNotification(notificationTime, this@AuthActivity)

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
}