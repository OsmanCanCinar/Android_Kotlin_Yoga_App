package com.osmancancinar.yogaapp.ui.view.home

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.ActivityHomeBinding
import com.osmancancinar.yogaapp.ui.viewModel.home.HomeActivityVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*
import kotlin.random.Random


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeActivityVM
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var listener: NavController.OnDestinationChangedListener
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_YogaApp_Home)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.drawerToolBar)

        viewModel = ViewModelProviders.of(this).get(HomeActivityVM::class.java)

        viewModel.sendNotification()

        navController = Navigation.findNavController(this, R.id.fragmentHome)

        binding.navigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

        listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        binding.homeActivityLayout.setBackgroundDrawable(getDrawable(R.drawable.background_blog))
                        //supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.purple_700)))
                    }
                }
                R.id.meditationDetailFragment -> {
                    when (rand(1, 5)) {
                        1 -> {
                            binding.homeActivityLayout.setBackgroundDrawable(getDrawable(R.drawable.background_meditation))
                        }
                        2 -> {
                            binding.homeActivityLayout.setBackgroundDrawable(getDrawable(R.drawable.background_meditation_1))
                        }
                        3 -> {
                            binding.homeActivityLayout.setBackgroundDrawable(getDrawable(R.drawable.background_meditation_2))
                        }
                        4 -> {
                            binding.homeActivityLayout.setBackgroundDrawable(getDrawable(R.drawable.background_meditation_3))
                        }
                        5 -> {
                            binding.homeActivityLayout.setBackgroundDrawable(getDrawable(R.drawable.background_meditation_4))
                        }
                    }
                    supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(android.R.color.transparent)))
                    hideSystemUI()
                    //supportActionBar?.hide()
                }
                R.id.meditationCategoryFragment -> {
                    supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(android.R.color.transparent)))
                    binding.homeActivityLayout.setBackgroundDrawable(getDrawable(R.drawable.background_blog))
                    showSystemUI()
                    //supportActionBar?.show()
                }
                R.id.meditationFragment -> {
                    if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        binding.homeActivityLayout.setBackgroundDrawable(getDrawable(R.drawable.background_blog))
                    }
                }
                R.id.yogaFragment -> {
                    if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        binding.homeActivityLayout.setBackgroundDrawable(getDrawable(R.drawable.background_blog))
                    }
                }
                R.id.blogFragment -> {
                    if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        binding.homeActivityLayout.setBackgroundDrawable(getDrawable(R.drawable.background_blog))
                    }
                }
                R.id.profileFragment -> {
                    if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        binding.homeActivityLayout.setBackgroundDrawable(getDrawable(R.drawable.background_blog))
                    }
                }
                R.id.settingsFragment -> {
                    if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        binding.homeActivityLayout.setBackgroundDrawable(getDrawable(R.drawable.background_blog))
                    }
                }
            }
        }
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, navigationView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(
            window,
            navigationView
        ).show(WindowInsetsCompat.Type.systemBars())
    }


    private fun rand(start: Int, end: Int): Int {
        require(!(start > end || end - start + 1 > Int.MAX_VALUE)) { "Illegal Argument" }
        return Random(System.nanoTime()).nextInt(end - start + 1) + start
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