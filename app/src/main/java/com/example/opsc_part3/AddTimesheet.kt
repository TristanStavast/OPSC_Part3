package com.example.opsc_part3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddTimesheet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_timesheet)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val ibtnimage : ImageButton = findViewById(R.id.ibtnAddImage)
        val btnaddtimes : Button = findViewById(R.id.btnAddTimes)

        val txtname : EditText = findViewById(R.id.txtAddTimeName)
        val txtdesc : EditText = findViewById(R.id.txtTimeDescription)

        ibtnimage.setOnClickListener()
        {

        }

        btnaddtimes.setOnClickListener()
        {
            
        }

    }
}