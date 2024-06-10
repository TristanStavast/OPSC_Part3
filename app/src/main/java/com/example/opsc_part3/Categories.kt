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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.database.database

class Categories : AppCompatActivity() {

    companion object {
        val dbCat = Firebase.database
    }
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
            val reports = Intent(this, Reports::class.java)

            when(it.itemId){
                R.id.nav_home -> startActivity(homeint)
                R.id.nav_report -> startActivity(reports)
                R.id.nav_timesheet -> startActivity(timeint)
                R.id.nav_profile -> startActivity(profile)
                R.id.nav_logout -> startActivity(logout)
            }
            true

        }


        // Main Code
        val rv: RecyclerView = findViewById(R.id.rvCategories)
        rv.layoutManager = LinearLayoutManager(this)

        val username = MainActivity.userList[MainActivity.SignedIn].username
        val filteredCategories = MainActivity.arrCategoryData.filter { it.username == username }

        val adapter = CategoryAdapter(this, filteredCategories)
        rv.adapter = adapter


        var btnAdd : Button = findViewById(R.id.btnAddCategory)
        btnAdd.setOnClickListener()
        {
            var catName : EditText = findViewById(R.id.txtCategoryName)
            var catDesc : EditText = findViewById(R.id.txtCategoryDescription)
            val isCategoryExists = CheckIfExists.isCategoryExists(MainActivity.userList[MainActivity.SignedIn].username,catName.text.toString(), MainActivity.arrCategoryData)

            if ((catName.text.toString().equals("")) || (catDesc.text.toString().equals("")))
            {
                catName.error= "Please enter all fields."
                catDesc.error = "Please enter all fields."
            }
            else if (isCategoryExists)
            {
                catName.error = "Category already exists."
            }
            else
            {
                for (user in MainActivity.userList)
                {
                    if (user.username.equals(MainActivity.userList[MainActivity.SignedIn].username))
                    {
                        val ref = dbCat.getReference("Category/" + (MainActivity.arrCategoryData.size + 1) + "/Username")
                        ref.setValue(MainActivity.userList[MainActivity.SignedIn].username)
                        val ref2 = dbCat.getReference("Category/" + (MainActivity.arrCategoryData.size + 1) + "/CategoryName")
                        ref2.setValue(catName.text.toString())
                        val ref3 = dbCat.getReference("Category/" + (MainActivity.arrCategoryData.size + 1) + "/Description")
                        ref3.setValue(catDesc.text.toString())

                        Toast.makeText(this, "Category successfully added.", Toast.LENGTH_SHORT).show()
                        /*val int = Intent(this, Home::class.java)
                        startActivity(int)*/
                    }
                }

                /*val updateDataset = MainActivity.arrCategoryData.filter {
                    it.username == username
                  }
                adapter.updateData(updateDataset)*/

                catName.setText("")
                catDesc.setText("")
            }
        }
    }

    override fun onBackPressed() {

    }
}