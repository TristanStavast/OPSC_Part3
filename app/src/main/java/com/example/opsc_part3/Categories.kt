package com.example.opsc_part3

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Categories : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_categories)

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
            val profile = Intent(this, Profile::class.java)

            when(it.itemId){
                R.id.nav_home -> startActivity(homeint)
                R.id.nav_settings -> Toast.makeText(applicationContext, "Clicked Settings", Toast.LENGTH_SHORT).show()
                R.id.nav_report -> Toast.makeText(applicationContext, "Clicked Report", Toast.LENGTH_SHORT).show()
                R.id.nav_timesheet -> startActivity(timeint)
                R.id.nav_profile -> startActivity(profile)
                R.id.nav_logout -> startActivity(logout)
            }
            true

        }

        // Main Code

        var btnAdd : Button = findViewById(R.id.btnAddCategory)
        btnAdd.setOnClickListener()
        {
            var catName : EditText = findViewById(R.id.txtCategoryName)
            var catDesc : EditText = findViewById(R.id.txtCategoryDescription)

            if ((catName.text.toString().equals("")) || (catDesc.text.toString().equals("")))
            {
                catName.error = "Please enter all fields."
                catDesc.error = "Please enter all fields."
            }
            else
            {
                for (user in MainActivity.userList)
                {
                    if (user.username.equals(MainActivity.userList[MainActivity.SignedIn].username))
                    {
                        MainActivity.arrCategoryData.add(CategoryData(user.username, catName.text.toString(), catDesc.text.toString()))
                    }
                }
            }
        }
    }
    override fun onBackPressed() {

    }
}