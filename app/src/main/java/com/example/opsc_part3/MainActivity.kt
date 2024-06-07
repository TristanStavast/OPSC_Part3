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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var username : EditText = findViewById(R.id.txtUsername)
        var password : EditText = findViewById(R.id.txtPassword)
        var btnlogin : Button = findViewById(R.id.btnLogin)
        var btnreg : Button = findViewById(R.id.btnRegister)

        btnlogin.setOnClickListener()
        {

            if((username.text.toString().isEmpty()) || (password.text.toString().isEmpty()))
            {
                Toast.makeText(this, "Enter all fields!", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val int = Intent(this, Home::class.java)
                startActivity(int)
            }

        }

        btnreg.setOnClickListener()
        {
            val int = Intent(this, Register::class.java)
            startActivity(int)
        }
    }



}