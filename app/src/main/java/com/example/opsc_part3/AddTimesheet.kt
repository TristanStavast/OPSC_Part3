package com.example.opsc_part3

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
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
import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.view.WindowManager
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.io.InputStream
import java.util.Calendar

class AddTimesheet : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var btnImage: ImageButton
    private val PICK_IMAGE = 1

    private lateinit var startDate : EditText
    private lateinit var endDate : EditText

    companion object
    {
        val db = Firebase.database
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the activity fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_add_timesheet)

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
                R.id.nav_profile -> Toast.makeText(applicationContext, "Clicked Profile", Toast.LENGTH_SHORT).show()
                R.id.nav_logout -> startActivity(logout)
            }
            true

        }


        // Main Code

        startDate = findViewById(R.id.txtStartDate)
        endDate = findViewById(R.id.txtEndDate)

        startDate.setOnClickListener {
            showDatePickerDialog(startDate)
        }
        endDate.setOnClickListener {
            showDatePickerDialog(endDate)
        }

        btnImage = findViewById(R.id.ibtnAddImage)
        btnImage.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE)
        }

        val btnaddtimes : Button = findViewById(R.id.btnAddTimes)

        btnaddtimes.setOnClickListener()
        {
            val name : EditText = findViewById(R.id.txtAddTimeName)
            val desc : EditText = findViewById(R.id.txtTimeDescription)

            if ((name.text.equals("")) || (startDate.text.equals("")) || (endDate.text.equals("")) || (desc.text.equals("")))
            {
                name.setError("Please enter all fields!")
                startDate.setError("Please enter all fields!")
                endDate.setError("Please enter all fields!")
                desc.setError("Please enter all fields!")
            }
            else
            {
                val ref = Register.db.getReference("Timesheet/" + MainActivity.arrTimeSheet.size + "/Username")
                ref.setValue(MainActivity.userList[MainActivity.SignedIn].username)
                val ref2 = Register.db.getReference("Timesheet/" + MainActivity.arrTimeSheet.size + "/TimesheetName")
                ref2.setValue(name.text.toString())
                val ref3 = Register.db.getReference("Timesheet/" + MainActivity.arrTimeSheet.size + "/StartDate")
                ref3.setValue(startDate.text.toString())
                val ref4 = Register.db.getReference("Timesheet/" + MainActivity.arrTimeSheet.size + "/EndDate")
                ref4.setValue(endDate.text.toString())
                val ref5 = Register.db.getReference("Timesheet/" + MainActivity.arrTimeSheet.size + "/TotalTime")
                ref5.setValue("4h32m")
                val ref6 = Register.db.getReference("Timesheet/" + MainActivity.arrTimeSheet.size + "/Description")
                ref6.setValue(desc.text.toString())
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri? = data?.data
            if (selectedImage != null) {
                val imageStream: InputStream? = contentResolver.openInputStream(selectedImage)
                val bitmap: Bitmap = BitmapFactory.decodeStream(imageStream)
                btnImage.setImageBitmap(bitmap)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){

        }
        return true
    }

    private fun showDatePickerDialog(txt: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            txt.setText("$selectedDate")
        }, year, month, day)

        datePickerDialog.show()
    }

    override fun onBackPressed() {

    }
}