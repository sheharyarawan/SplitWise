package com.example.splitwise

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.splitwise.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->

            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        setUpNavigation()
    }

    fun setUpNavigation() {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer)
                    as NavHostFragment

        val navController = navHostFragment.navController

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {

            navController.setGraph(R.navigation.auth_nav_graph)

            hideBottomNav()
            hideAddExpenseButton()

        } else {

            showMainApp()
        }
    }

    fun showMainApp() {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer)
                    as NavHostFragment

        val navController = navHostFragment.navController

        navController.setGraph(R.navigation.nav_graph)

        binding.bottomNav.setupWithNavController(navController)

        showBottomNav()
    }

    fun showAddExpenseButton() {
        binding.addExpenseBtn.visibility = View.VISIBLE
    }

    fun hideAddExpenseButton() {
        binding.addExpenseBtn.visibility = View.GONE
    }

    fun shrinkAddExpenseButton() {
        binding.addExpenseBtn.shrink()
    }

    fun extendAddExpenseButton() {
        binding.addExpenseBtn.extend()
    }

    fun setAddExpenseClickListener(action: () -> Unit) {
        binding.addExpenseBtn.setOnClickListener {
            action()
        }
    }

    fun clearAddExpenseClickListener() {
        binding.addExpenseBtn.setOnClickListener(null)
    }

    fun showBottomNav() {
        binding.bottomNav.visibility = View.VISIBLE
    }

    fun hideBottomNav() {
        binding.bottomNav.visibility = View.GONE
    }
}