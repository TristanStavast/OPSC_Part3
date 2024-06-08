package com.example.opsc_part3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TimesheetList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_timesheet_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnaddtime : Button = findViewById(R.id.btnAddTimesheet)
        val btntimer : Button = findViewById(R.id.btnTimer)


        btnaddtime.setOnClickListener()
        {
            val int = Intent(this, AddTimesheet::class.java)
            startActivity(int)
        }

        btntimer.setOnClickListener()
        {
            val int = Intent(this, Timer::class.java)
            startActivity(int)
        }
    }
}