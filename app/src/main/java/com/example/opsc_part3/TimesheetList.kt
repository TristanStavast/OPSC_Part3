package com.example.opsc_part3

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class TimesheetList : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var lstData: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the activity fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_timesheet_list)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        val openDrawer : ImageButton = findViewById(R.id.btnNav)
        openDrawer.setOnClickListener()
        {
            if (!drawerLayout.isDrawerOpen(navView)) {
                drawerLayout.openDrawer(navView)
            }
        }

        //Intents
        navView.setNavigationItemSelectedListener {
            val homeint = Intent(this, Home::class.java)
            val logout = Intent(this, MainActivity::class.java)

            when(it.itemId){
                R.id.nav_home -> startActivity(homeint)
                R.id.nav_settings -> Toast.makeText(applicationContext, "Clicked Settings", Toast.LENGTH_SHORT).show()
                R.id.nav_report -> Toast.makeText(applicationContext, "Clicked Report", Toast.LENGTH_SHORT).show()
                R.id.nav_categories -> Toast.makeText(applicationContext, "Clicked Categories", Toast.LENGTH_SHORT).show()
                R.id.nav_profile -> Toast.makeText(applicationContext, "Clicked Profile", Toast.LENGTH_SHORT).show()
                R.id.nav_logout -> startActivity(logout)
            }
            true

        }


        // Main Code

        // Sample data
        val dataList = listOf(
            listOf("Data 1 Column 1", "Data 1 Column 2", "Data 1 Column 3"),
            listOf("Data 2 Column 1", "Data 2 Column 2", "Data 2 Column 3"),
            listOf("Data 3 Column 1", "Data 3 Column 2", "Data 3 Column 3")
        )

        val listView: ListView = findViewById(R.id.lstTimesheets)

        // Simple ArrayAdapter to populate the ListView
        val adapter = ArrayAdapter(
            this,
            R.layout.list_item_layout,
            R.id.column1TextView,
            dataList.map { it.joinToString("\n") }
        )

        listView.adapter = adapter


        val btnaddtime : Button = findViewById(R.id.btnAddTimesheet)
        val btntimer : Button = findViewById(R.id.btnTimer)

        btnaddtime.setOnClickListener()
        {
            val int = Intent(this, AddTimesheet::class.java)
            startActivity(int)
        }

        btntimer.setOnClickListener()
        {
            val int = Intent(this, Timer::class.java)
            startActivity(int)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){

        }
        return true
    }

    override fun onBackPressed() {

    }

}