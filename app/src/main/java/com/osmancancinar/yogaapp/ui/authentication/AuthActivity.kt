package com.osmancancinar.yogaapp.ui.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.ActivityAuthBinding
import com.osmancancinar.yogaapp.viewModels.auth.AuthVM

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var viewModel: AuthVM
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_YogaApp)
        setContentView(binding.root)

        setSupportActionBar(binding.myToolBar)

        viewModel = ViewModelProviders.of(this).get(AuthVM::class.java)
        navController = Navigation.findNavController(this, R.id.fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
}