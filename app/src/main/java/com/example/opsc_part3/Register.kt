package com.example.opsc_part3

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.database.database

class Register : AppCompatActivity() {

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

        setContentView(R.layout.activity_register)

        //Registration button on register page
        var btnReg : Button = findViewById(R.id.btnReg)
        btnReg.setOnClickListener() {
            var uname: EditText = findViewById(R.id.txtRegUsername)
            var pass: EditText = findViewById(R.id.txtRegPassword)

            //error handling
            if ((uname.text.toString().equals("")) || (pass.text.toString().equals(""))) {
                uname.setError("Please enter valid username!")
                pass.setError("Please enter valid password!")
            }
            else {

                MainActivity.userList.add(Users(uname.text.toString(), pass.text.toString()))

                val ref = db.getReference("Users/" + MainActivity.userList.size + "/Username")
                ref.setValue(uname.text.toString())
                val ref2 = db.getReference("Users/" + MainActivity.userList.size + "/Password")
                ref2.setValue(pass.text.toString())

                Toast.makeText(this, "Successfully registered! Please login.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        var btnLogin : Button = findViewById(R.id.btnNavLogin)
        btnLogin.setOnClickListener()
        {
            val int = Intent(this, MainActivity::class.java)
            startActivity(int)
        }
    }
}