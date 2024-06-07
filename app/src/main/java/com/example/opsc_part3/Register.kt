package com.example.opsc_part3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        var btnreg : Button = findViewById(R.id.btnReg)
        var txtRegUser : EditText = findViewById(R.id.txtRegUsername)
        var txtRegPass : EditText = findViewById(R.id.txtRegPassword)

        btnreg.setOnClickListener()
        {
            if((txtRegUser.text.toString().isEmpty()) || (txtRegPass.text.toString().isEmpty()))
            {
                Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val int = Intent(this, Home::class.java)
                startActivity(int)
            }
        }
    }
}