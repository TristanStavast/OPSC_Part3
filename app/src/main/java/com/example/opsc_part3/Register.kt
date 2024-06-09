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
            val isUsernameExists = CheckIfExists.isUsernameExists(uname.text.toString(), MainActivity.userList)

            //error handling
            if ((uname.text.toString().equals("")) || (pass.text.toString().equals(""))) {
                uname.error = "Please enter valid username!"
                pass.error = "Please enter valid password!"
            }
            else if (isUsernameExists)
            {
                uname.error = "Username already exists."
            }
            else {
                val ref = db.getReference("Users/" + (MainActivity.userList.size + 1) + "/Username")
                ref.setValue(uname.text.toString())
                val ref2 = db.getReference("Users/" + (MainActivity.userList.size + 1) + "/Password")
                ref2.setValue(pass.text.toString())
                val ref3 = db.getReference("Users/" + (MainActivity.userList.size + 1) + "/Email")
                ref3.setValue("")
                val ref4 = db.getReference("Users/" + (MainActivity.userList.size + 1) + "/FullName")
                ref4.setValue("")
                val ref5 = db.getReference("Users/" + (MainActivity.userList.size + 1) + "/Image")
                ref5.setValue("")

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