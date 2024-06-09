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
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var btnImage: ImageButton
    private val PICK_IMAGE = 1
    private var imgString: String = ""

    private lateinit var txtDate : EditText
    private lateinit var startTime : EditText
    private lateinit var endTime : EditText

    companion object
    {
        val dbTS = Firebase.database
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
            val categint = Intent(this, Categories::class.java)
            val homeint = Intent(this, Home::class.java)
            val logout = Intent(this, MainActivity::class.java)
            val profile = Intent(this, Profile::class.java)

            when(it.itemId){
                R.id.nav_home -> startActivity(homeint)
                R.id.nav_settings -> Toast.makeText(applicationContext, "Clicked Settings", Toast.LENGTH_SHORT).show()
                R.id.nav_report -> Toast.makeText(applicationContext, "Clicked Report", Toast.LENGTH_SHORT).show()
                R.id.nav_timesheet -> startActivity(timeint)
                R.id.nav_categories -> startActivity(categint)
                R.id.nav_profile -> startActivity(profile)
                R.id.nav_logout -> startActivity(logout)
            }
            true

        }


        // Main Code

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

        val cat : AutoCompleteTextView = findViewById(R.id.cmbCategory)
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

            if ((name.text.toString().equals("")) || (txtDate.text.toString().equals("")) || (startTime.text.toString().equals("")) || (endTime.text.toString().equals("")) || (desc.text.toString().equals("")))
            {
                name.setError("Please enter all fields!")
                txtDate.setError("Please enter all fields!")
                startTime.setError("Please enter all fields!")
                endTime.setError("Please enter all fields!")
                desc.setError("Please enter all fields!")
            }
            else
            {
                val ref = dbTS.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/Username")
                ref.setValue(MainActivity.userList[MainActivity.SignedIn].username)
                val ref2 = dbTS.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/TimesheetName")
                ref2.setValue(name.text.toString())
                val ref3 = dbTS.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/Date")
                ref3.setValue(txtDate.text.toString())
                val ref4 = dbTS.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/StartTime")
                ref4.setValue(startTime.text.toString())
                val ref5 = dbTS.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/EndTime")
                ref5.setValue(endTime.text.toString())

                val totalTime = calculateTotalTime(startTime.text.toString(), endTime.text.toString())
                val ref6 = dbTS.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/TotalTime")
                ref6.setValue(totalTime)

                val ref7 = dbTS.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/Description")
                ref7.setValue(desc.text.toString())
                val ref8 = dbTS.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/Image")
                ref8.setValue(imgString)

                MainActivity.arrTimeSheet.add(TimesheetData(MainActivity.userList[MainActivity.SignedIn].username, name.text.toString(),
                    txtDate.text.toString(),startTime.text.toString(), endTime.text.toString(), totalTime, desc.text.toString(), imgString))

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