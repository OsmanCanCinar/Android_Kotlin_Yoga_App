package com.osmancancinar.yogaapp.ui.home

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.ShapeDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.ActivityHomeBinding
import com.osmancancinar.yogaapp.viewModels.home.HomeActivityVM
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeActivityVM
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var listener: NavController.OnDestinationChangedListener
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_YogaApp)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.drawerToolBar)

        viewModel = ViewModelProviders.of(this).get(HomeActivityVM::class.java)

        navController = Navigation.findNavController(this, R.id.fragmentHome)

        binding.navigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        setupActionBarWithNavController(navController,appBarConfiguration)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.purple_700)))
                    }
                }
                R.id.meditationFragment -> {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.green)))
                    }
                }
                R.id.yogaFragment -> {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.purple_200)))
                    }
                }
                R.id.blogFragment -> {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.splash_img_background)))
                    }
                }
                R.id.profileFragment -> {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.teal_700)))
                    }
                }
                R.id.settingsFragment -> {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.purple_500)))
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        super.onPause()
        navController.addOnDestinationChangedListener(listener)
    }

     override fun onSupportNavigateUp(): Boolean {
         val navController = findNavController(R.id.fragmentHome)
         return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}