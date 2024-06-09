package com.example.opsc_part3

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.util.regex.Pattern

class Profile : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the activity fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_profile)

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
            val homeint = Intent(this, Home::class.java)
            val logout = Intent(this, MainActivity::class.java)

            when(it.itemId){
                R.id.nav_home -> startActivity(homeint)
                R.id.nav_settings -> Toast.makeText(applicationContext, "Clicked Settings", Toast.LENGTH_SHORT).show()
                R.id.nav_report -> Toast.makeText(applicationContext, "Clicked Report", Toast.LENGTH_SHORT).show()
                R.id.nav_timesheet -> startActivity(timeint)
                R.id.nav_categories -> Toast.makeText(applicationContext, "Clicked Categories", Toast.LENGTH_SHORT).show()
                R.id.nav_logout -> startActivity(logout)
            }
            true

        }


        // Main Code
        var username : EditText = findViewById(R.id.txtProfileUsername)
        var email : EditText = findViewById(R.id.txtProfileEmail)
        var password : EditText = findViewById(R.id.txtChangePassword)
        var fullname : EditText = findViewById(R.id.txtFullName)

        for (user in MainActivity.userList)
        {
            if (user.username.equals(MainActivity.userList[MainActivity.SignedIn].username))
            {
                username.hint = user.username
                if (user.email == null)
                {
                    email.hint = "Email"
                }
                else
                {
                    email.setText(user.email)
                }
                if (user.fullname == null)
                {
                    fullname.hint = "Full Name"
                }
                else
                {
                    fullname.setText(user.fullname)
                }
                password.setText(user.password)
            }
        }

        var saveChanges : Button = findViewById(R.id.btnSaveChanges)
        saveChanges.setOnClickListener()
        {
            for (user in MainActivity.userList)
            {
                if (user.username.equals(MainActivity.userList[MainActivity.SignedIn].username))
                {
                    if ((email.text.toString() != "") && (isValidEmail(email)))
                    {
                        user.email = email.text.toString()
                    }
                    else if ((email.text.toString() != "") && (!isValidEmail(email)))
                    {
                        email.setError("Email is not in the right format")
                        break
                    }
                    if (fullname.text.toString() != "")
                    {
                        user.fullname = fullname.text.toString()
                    }

                    user.password = password.text.toString()
                }
            }
        }
    }

    fun isValidEmail(editText: EditText): Boolean {
        val emailPattern = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE
        )
        val email = editText.text.toString().trim()
        return emailPattern.matcher(email).matches()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){

        }
        return true
    }

    override fun onBackPressed() {

    }
}