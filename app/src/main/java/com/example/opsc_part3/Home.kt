package com.example.opsc_part3

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Home : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    var uname : String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the activity fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_home)

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
            val timeint = Intent(this, TimesheetList::class.java)
            val logout = Intent(this, MainActivity::class.java)
            val profile = Intent(this, Profile::class.java)
            val categint = Intent(this, Categories::class.java)

            when(it.itemId){
                R.id.nav_settings -> Toast.makeText(applicationContext, "Clicked Settings", Toast.LENGTH_SHORT).show()
                R.id.nav_report -> Toast.makeText(applicationContext, "Clicked Report", Toast.LENGTH_SHORT).show()
                R.id.nav_timesheet -> startActivity(timeint)
                R.id.nav_categories -> startActivity(categint)
                R.id.nav_profile -> startActivity(profile)
                R.id.nav_logout -> startActivity(logout)
            }
            true

        }

        //main code
        for (user in MainActivity.userList)
        {
            if (user.username.equals(MainActivity.userList[MainActivity.SignedIn].username))
            {
                uname = user.username
            }
        }
        var welcome: TextView = findViewById(R.id.txtWelcome)
        welcome.setText("Welcome, " + uname)


        var clock : ImageButton = findViewById(R.id.btnClock)
        clock.setOnClickListener()
        {
            val clockint = Intent(this, Timer::class.java)
            startActivity(clockint)
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