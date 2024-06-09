package com.example.opsc_part3

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import org.w3c.dom.Text
import android.graphics.BitmapFactory
import android.util.Base64

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
            val reports = Intent(this, Reports::class.java)
            val settings = Intent(this, Settings::class.java)

            when(it.itemId){
                R.id.nav_settings -> startActivity(settings)
                R.id.nav_report -> startActivity(reports)
                R.id.nav_timesheet -> startActivity(timeint)
                R.id.nav_categories -> startActivity(categint)
                R.id.nav_profile -> startActivity(profile)
                R.id.nav_logout -> startActivity(logout)
            }
            true

        }

        // Main Code
        var lblUN : TextView = findViewById(R.id.lblDashUsername)
        var imgUser : ImageView = findViewById(R.id.imgUserImage)
        for (user in MainActivity.userList)
        {
            if (user.username.equals(MainActivity.userList[MainActivity.SignedIn].username))
            {
                lblUN.text = user.username
                val bitmap = decodeBase64ToBitmap(user.image)
                if (bitmap != null) {
                    imgUser.setImageBitmap(bitmap)
                } else {
                    imgUser.setImageResource(R.drawable.baseline_person_24)
                }
            }
        }

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

    private fun decodeBase64ToBitmap(base64Str: String?): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }

    override fun onBackPressed() {

    }

}