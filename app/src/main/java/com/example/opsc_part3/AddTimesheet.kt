package com.example.opsc_part3

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.view.WindowManager
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.Calendar
import android.util.Base64
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

class AddTimesheet : AppCompatActivity() {
    //Private Variables
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var btnImage: ImageButton
    private val PICK_IMAGE = 1
    private var imgString: String = ""

    private lateinit var txtDate : EditText
    private lateinit var startTime : EditText
    private lateinit var endTime : EditText

    //Creating variable for Firebase database
    companion object
    {
        val dbTS = Firebase.database
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Make the activity fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_add_timesheet)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        //Navigation drawer button
        val openDrawer : ImageButton = findViewById(R.id.btnNav)
        openDrawer.setOnClickListener()
        {
            if (!drawerLayout.isDrawerOpen(navView)) {
                drawerLayout.openDrawer(navView)
            }
        }

        //Intents for navigation drawer
        navView.setNavigationItemSelectedListener {
            val timeint = Intent(this, TimesheetList::class.java)
            val categint = Intent(this, Categories::class.java)
            val homeint = Intent(this, Home::class.java)
            val logout = Intent(this, MainActivity::class.java)
            val profile = Intent(this, Profile::class.java)
            val reports = Intent(this, Reports::class.java)
            val settings = Intent(this, Settings::class.java)

            when(it.itemId){
                R.id.nav_home -> startActivity(homeint)
                R.id.nav_settings -> startActivity(settings)
                R.id.nav_report -> startActivity(reports)
                R.id.nav_timesheet -> startActivity(timeint)
                R.id.nav_categories -> startActivity(categint)
                R.id.nav_profile -> startActivity(profile)
                R.id.nav_logout -> startActivity(logout)
            }
            true

        }

        //Main Code
        val items = ArrayList<String?>()
        for (user in MainActivity.userList)
        {
            if (user.username.equals(MainActivity.userList[MainActivity.SignedIn].username))
            {
                for (cat in MainActivity.arrCategoryData)
                {
                    if (user.username.equals(cat.username))
                    {
                        items.add(cat.CategoryName)
                    }
                }
            }
        }

        val cat : AutoCompleteTextView = findViewById(R.id.cmbTsCategory)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
        cat.setAdapter(adapter)

        cat.inputType = 0
        cat.setOnClickListener()
        {
            cat.showDropDown()
        }

        txtDate = findViewById(R.id.txtDate)
        startTime = findViewById(R.id.txtStartTime)
        endTime = findViewById(R.id.txtEndTime)

        txtDate.setOnClickListener {
            showDatePickerDialog(txtDate)
        }
        startTime.setOnClickListener {
            showTimePickerDialog(startTime)
        }
        endTime.setOnClickListener {
            showTimePickerDialog(endTime)
        }

        btnImage = findViewById(R.id.btnAddTsImage)
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
            val isTimesheetExists = CheckIfExists.isTimesheetExists(MainActivity.userList[MainActivity.SignedIn].username, name.text.toString(), MainActivity.arrTimeSheet)

            //Making sure the user enters all fields
            if ((name.text.toString().equals("")) || (txtDate.text.toString().equals("")) || (startTime.text.toString().equals("")) || (endTime.text.toString().equals("")) || (desc.text.toString().equals("")) || (cat.text.toString().equals("")))
            {
                name.error = "Please enter all fields!"
                txtDate.error = "Please enter all fields!"
                startTime.error = "Please enter all fields!"
                endTime.error = "Please enter all fields!"
                desc.error = "Please enter all fields!"
                cat.error = "Please select a category"
            }
            //Making sure 2 timesheets cant have the same name
            else if (isTimesheetExists)
            {
                name.error = "Timesheet name already exists."
            }
            else
            {
                //Adding values to the database
                val ref = dbTS.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/Username")
                ref.setValue(MainActivity.userList[MainActivity.SignedIn].username)
                val ref2 = dbTS.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/TimesheetName")
                ref2.setValue(name.text.toString())
                val ref3 = dbTS.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/Category")
                ref3.setValue(cat.text.toString())
                val ref4 = dbTS.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/Date")
                ref4.setValue(txtDate.text.toString())
                val ref5 = dbTS.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/StartTime")
                ref5.setValue(startTime.text.toString())
                val ref6 = dbTS.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/EndTime")
                ref6.setValue(endTime.text.toString())

                //Calculating the amount of hours
                val totalTime = calculateTotalTime(startTime.text.toString(), endTime.text.toString())
                val ref7 = dbTS.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/TotalTime")
                ref7.setValue(totalTime)

                val ref8 = dbTS.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/Description")
                ref8.setValue(desc.text.toString())
                val ref9 = dbTS.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/Image")
                ref9.setValue(imgString)

                Toast.makeText(this, "Successfully added timesheet data", Toast.LENGTH_SHORT).show()
                val int = Intent(this, TimesheetList::class.java)
                startActivity(int)
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

                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                imgString = Base64.encodeToString(byteArray, Base64.DEFAULT)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){

        }
        return true
    }

    //Making date picker
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

    //Setting the format
    private fun showTimePickerDialog(txt: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            txt.setText(selectedTime)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    //Calculating total hours
    private fun calculateTotalTime(startTime: String, endTime: String): String {
        val startTimeParts = startTime.split(":")
        val endTimeParts = endTime.split(":")

        val startHour = startTimeParts[0].toInt()
        val startMinute = startTimeParts[1].toInt()

        val endHour = endTimeParts[0].toInt()
        val endMinute = endTimeParts[1].toInt()

        var totalMinutes = (endHour * 60 + endMinute) - (startHour * 60 + startMinute)

        if (totalMinutes < 0) {
            totalMinutes += 24 * 60
        }

        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        return "$hours h $minutes m"
    }

    override fun onBackPressed() {

    }
}