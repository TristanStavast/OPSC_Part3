package com.example.opsc_part3

import TimesheetAdapter
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Reports : AppCompatActivity() {
    private lateinit var txtStartDate: EditText
    private lateinit var txtEndDate: EditText
    private lateinit var adapter: TimesheetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_reports)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val openDrawer: ImageButton = findViewById(R.id.btnNav)
        openDrawer.setOnClickListener {
            if (!drawerLayout.isDrawerOpen(navView)) {
                drawerLayout.openDrawer(navView)
            }
        }

        // Intents
        navView.setNavigationItemSelectedListener {
            val timeint = Intent(this, TimesheetList::class.java)
            val profile = Intent(this, Profile::class.java)
            val homeint = Intent(this, Home::class.java)
            val categint = Intent(this, Categories::class.java)
            val logout = Intent(this, MainActivity::class.java)

            when (it.itemId) {
                R.id.nav_home -> startActivity(homeint)
                R.id.nav_profile -> startActivity(profile)
                R.id.nav_timesheet -> startActivity(timeint)
                R.id.nav_categories -> startActivity(categint)
                R.id.nav_logout -> startActivity(logout)
            }
            true
        }

        val rvReport: RecyclerView = findViewById(R.id.rvReport)
        val username = MainActivity.userList[MainActivity.SignedIn].username
        val filteredTimesheets = MainActivity.arrTimeSheet.filter { it.username == username }
        adapter = TimesheetAdapter(this, filteredTimesheets)
        rvReport.adapter = adapter
        rvReport.layoutManager = LinearLayoutManager(this)

        txtStartDate = findViewById(R.id.txtStartDate)
        txtEndDate = findViewById(R.id.txtEndDate)

        txtStartDate.setOnClickListener {
            showDatePickerDialog(txtStartDate)
        }
        txtEndDate.setOnClickListener {
            showDatePickerDialog(txtEndDate)
        }

        val btnFilter: Button = findViewById(R.id.btnFilter)
        btnFilter.setOnClickListener {
            filterTimesheets()
        }
    }

    private fun showDatePickerDialog(txt: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            txt.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun filterTimesheets() {
        val startDateStr = txtStartDate.text.toString()
        val endDateStr = txtEndDate.text.toString()

        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            Toast.makeText(this, "Please select both start and end dates", Toast.LENGTH_SHORT).show()
            return
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val startDate: Date? = sdf.parse(startDateStr)
        val endDate: Date? = sdf.parse(endDateStr)

        if (startDate != null && endDate != null) {
            val username = MainActivity.userList[MainActivity.SignedIn].username
            val filteredTimesheets = MainActivity.arrTimeSheet.filter {
                it.username == username && isDateInRange(it.date, startDate, endDate, sdf)
            }
            adapter.updateData(filteredTimesheets)
        }
    }

    private fun isDateInRange(dateStr: String?, startDate: Date, endDate: Date, sdf: SimpleDateFormat): Boolean {
        return try {
            val date = sdf.parse(dateStr)
            date != null && date >= startDate && date <= endDate
        } catch (e: Exception) {
            false
        }
    }

    override fun onBackPressed() {
        // Handle the back press logic here if needed
    }
}
