package com.example.opsc_part3

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Timer : AppCompatActivity() {

    //Variables for timer to function correctly
    lateinit var toggle: ActionBarDrawerToggle
    private var seconds = 0
    private var running = false
    private var wasRunning = false
    private var counter: Int = 0

    //Companion object for realtime database
    companion object {
        val dbTimer = Firebase.database
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_timer)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        val openDrawer : ImageButton = findViewById(R.id.btnNav)
        openDrawer.setOnClickListener()
        {
            if (!drawerLayout.isDrawerOpen(navView)) {
                drawerLayout.openDrawer(navView)
            }
        }

        //Intents for different pages
        navView.setNavigationItemSelectedListener {
            val timeint = Intent(this, TimesheetList::class.java)
            val homeint = Intent(this, Home::class.java)
            val logout = Intent(this, MainActivity::class.java)
            val intcat = Intent(this, Categories::class.java)
            val reports = Intent(this, Reports::class.java)
            val profile = Intent(this, Profile::class.java)

            //Intents for burger menu on different pages
            when(it.itemId){
                R.id.nav_home -> startActivity(homeint)
                R.id.nav_profile -> startActivity(profile)
                R.id.nav_report -> startActivity(reports)
                R.id.nav_timesheet -> startActivity(timeint)
                R.id.nav_categories -> startActivity(intcat)
                R.id.nav_logout -> startActivity(logout)
            }
            true

        }

        var btnstart : Button = findViewById(R.id.btnStart)
        var btnstop : Button = findViewById(R.id.btnStop)
        var btnreset : Button = findViewById(R.id.btnReset)

        //Error handling to check in what state the timer is in
        if(savedInstanceState != null)
        {
            seconds = savedInstanceState.getInt("seconds")
            running = savedInstanceState.getBoolean("running")
            wasRunning = savedInstanceState.getBoolean("wasRunning")
        }
        //Run timer
        runTimer()

        //Buttons to start the corresponding functions
        btnstart.setOnClickListener()
        {
            running = true
        }
        btnstop.setOnClickListener()
        {
            running = false
        }
        btnreset.setOnClickListener()
        {
            running = false
            seconds = 0
        }

        //Array to add timer data to categories
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

        //ComboBox for categories
        cat.inputType = 0
        cat.setOnClickListener()
        {
            cat.showDropDown()
        }

        //Adding more categories per user
        val items2 = ArrayList<String?>()
        for (user in MainActivity.userList)
        {
            if (user.username.equals(MainActivity.userList[MainActivity.SignedIn].username))
            {
                for (ts in MainActivity.arrTimeSheet)
                {
                    if (user.username.equals(ts.username))
                    {
                        items2.add(ts.tsName)
                    }
                }
            }
        }


        // Main Code
        val btnaddtimes : Button = findViewById(R.id.btnAddTimes)
        btnaddtimes.setOnClickListener()
        {
            val name : EditText = findViewById(R.id.txtTsName)
            val desc : EditText = findViewById(R.id.txtTimeDescription)
            var txtStopWatch : TextView = findViewById(R.id.txtStopwatch)
            val isTimesheetExists = CheckIfExists.isTimesheetExists(MainActivity.userList[MainActivity.SignedIn].username, name.text.toString(), MainActivity.arrTimeSheet)

            //Error handling to ensure all fields are entered
            if ((name.text.toString().equals("")) ||  (desc.text.toString().equals("")) || (cat.text.toString().equals("")))
            {
                name.error = "Please enter all fields!"
                desc.error = "Please enter all fields!"
                cat.error = "Please select a category"
            }
            else if (isTimesheetExists)
            {
                for (i in 0 until MainActivity.arrTimeSheet.size)
                {
                    //Adding data to the database
                    if ((MainActivity.userList[MainActivity.SignedIn].username.equals(MainActivity.arrTimeSheet[i].username)) &&
                        (name.text.toString().equals(MainActivity.arrTimeSheet[i].tsName)))
                    {
                        counter = i
                    }
                }

                // Update Timesheet entry
                val ref = dbTimer.getReference("Timesheet/" + (counter+1) + "/Username")
                ref.setValue(MainActivity.userList[MainActivity.SignedIn].username)
                val ref2 = dbTimer.getReference("Timesheet/" + (counter+1) + "/TimesheetName")
                ref2.setValue(name.text.toString())
                val ref3 = dbTimer.getReference("Timesheet/" + (counter+1) + "/Category")
                ref3.setValue(cat.text.toString())
                val ref4 = dbTimer.getReference("Timesheet/" + (counter+1) + "/Date")
                ref4.setValue(getTodaysDate())
                val ref5 = dbTimer.getReference("Timesheet/" + (counter+1) + "/StartTime")
                ref5.setValue("")
                val ref6 = dbTimer.getReference("Timesheet/" + (counter+1) + "/EndTime")
                ref6.setValue("")

                val ref7 = dbTimer.getReference("Timesheet/" + (counter+1) + "/TotalTime")
                ref7.setValue(addTimeStrings(MainActivity.arrTimeSheet[counter].totalTime, txtStopWatch.text.toString()))

                val ref8 = dbTimer.getReference("Timesheet/" + (counter+1) + "/Description")
                ref8.setValue(desc.text.toString())
                val ref9 = dbTimer.getReference("Timesheet/" + (counter+1) + "/Image")
                ref9.setValue("")

                Toast.makeText(this, "Successfully modified timesheet data", Toast.LENGTH_SHORT).show()
                val int = Intent(this, TimesheetList::class.java)
                startActivity(int)
            }
            else
            {
                val ref = dbTimer.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/Username")
                ref.setValue(MainActivity.userList[MainActivity.SignedIn].username)
                val ref2 = dbTimer.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/TimesheetName")
                ref2.setValue(name.text.toString())
                val ref3 = dbTimer.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/Category")
                ref3.setValue(cat.text.toString())
                val ref4 = dbTimer.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/Date")
                ref4.setValue(getTodaysDate())
                val ref5 = dbTimer.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/StartTime")
                ref5.setValue("")
                val ref6 = dbTimer.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/EndTime")
                ref6.setValue("")

                val ref7 = dbTimer.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/TotalTime")
                ref7.setValue(formatTime(txtStopWatch.text.toString()))

                val ref8 = dbTimer.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/Description")
                ref8.setValue(desc.text.toString())
                val ref9 = dbTimer.getReference("Timesheet/" + (MainActivity.arrTimeSheet.size + 1) + "/Image")
                ref9.setValue("")

                Toast.makeText(this, "Successfully added timesheet data", Toast.LENGTH_SHORT).show()
                val int = Intent(this, TimesheetList::class.java)
                startActivity(int)
            }
        }

    }

    //Saving instance for timer
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("seconds", seconds)
        outState.putBoolean("running", running)
        outState.putBoolean("wasRunning", wasRunning)
    }
    //Method for paused timer
    override fun onPause()
    {
        super.onPause()
        wasRunning = running
        running = false
    }
    //Method for resumption of timer
    override fun onResume() {
        super.onResume()
        if(wasRunning)
        {
            running = true
        }
    }

    //Method to ensure timer runs correctly
    private fun runTimer()
    {
        //Variables for timer
        var txtstopwatch : TextView = findViewById(R.id.txtStopwatch)

        val handler = Handler()
        handler.post(object : Runnable
        {
            //Code block to make the code run
            override fun run()
            {
                //Calculations to ensure the timer displays correctly
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                val secs = seconds % 60

                //Formatting text for timer
                val time = String.format("%02d:%02d:%02d", hours, minutes, secs)
                txtstopwatch.text = time

                if(running)
                {
                    seconds++
                }
                handler.postDelayed(this, 1000)
            }
        })
    }
    //Getting todays date for the timer
    fun getTodaysDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
    //Formatting time correctly
    fun formatTime(timeString: String): String {
        val parts = timeString.split(":")
        val hours = parts[0].toInt()
        val minutes = parts[1].toInt()
        val seconds = parts[2].toInt()

        val formattedTime = StringBuilder()

        if (hours > 0) {
            formattedTime.append(hours)
            formattedTime.append("h ")
        }

        formattedTime.append(minutes)
        formattedTime.append("m")

        return formattedTime.toString()
    }
    //Adding the times to a string
    fun addTimeStrings(time1: String?, time2: String?): String {
        fun parseHourMinuteString(time: String?): Int {
            if (time == null) return 0
            val regex = "(\\d+)h(\\d+)m".toRegex()
            val matchResult = regex.matchEntire(time)
            return if (matchResult != null) {
                val (hours, minutes) = matchResult.destructured
                hours.toInt() * 60 + minutes.toInt()
            } else {
                0
            }
        }
        //Filling the database with the timer data
        fun parseColonTimeString(time: String?): Int {
            if (time == null) return 0
            val parts = time.split(":").map { it.toInt() }
            return if (parts.size == 3) {
                parts[0] * 60 + parts[1] + parts[2] / 60
            } else {
                0
            }
        }

        val totalMinutes1 = parseHourMinuteString(time1)
        val totalMinutes2 = parseColonTimeString(time2)

        val newTotalMinutes = totalMinutes1 + totalMinutes2

        val hours = newTotalMinutes / 60
        val minutes = newTotalMinutes % 60

        return "${hours}h${if (minutes < 10) "0" else ""}${minutes}m"
    }

}