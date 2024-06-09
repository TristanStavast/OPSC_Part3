package com.example.opsc_part3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Tag
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class MainActivity : AppCompatActivity() {

    private lateinit var ref1: DatabaseReference
    private lateinit var ref2: DatabaseReference

    companion object
    {
        val userList = mutableListOf<Users>()
        val arrTimeSheet = mutableListOf<TimesheetData>()
        var SignedIn : Int = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Make the activity fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_main)

        SignedIn = -1

        ref1 = FirebaseDatabase.getInstance().getReference("Users")
        ref2 = FirebaseDatabase.getInstance().getReference("Timesheet")
        readDataFromFirebase()

    }

    private fun readDataFromFirebase() {
        ref1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (userSnapshot in snapshot.children) {
                    val username = userSnapshot.child("Username").getValue(String::class.java)
                    val password = userSnapshot.child("Password").getValue(String::class.java)
                    val email = userSnapshot.child("Email").getValue(String::class.java)
                    val fullname = userSnapshot.child("FullName").getValue(String::class.java)
                    val image = userSnapshot.child("Image").getValue(String::class.java)
                    if (username != null && password != null && email != null && fullname != null && image != null) {
                        val user = Users(username, password, email, fullname, image)
                        userList.add(user)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }
        })

        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrTimeSheet.clear()
                for (timeSnapshot in snapshot.children) {
                    val username = timeSnapshot.child("Username").getValue(String::class.java)
                    val tsname = timeSnapshot.child("TimesheetName").getValue(String::class.java)
                    val date = timeSnapshot.child("Date").getValue(String::class.java)
                    val stime = timeSnapshot.child("StartTime").getValue(String::class.java)
                    val etime = timeSnapshot.child("EndTime").getValue(String::class.java)
                    val totaltime = timeSnapshot.child("TotalTime").getValue(String::class.java)
                    val description = timeSnapshot.child("Description").getValue(String::class.java)
                    val img = timeSnapshot.child("Image").getValue(String::class.java)
                    if (username != null && tsname != null && date != null && stime != null && etime != null && totaltime != null && description != null) {
                        val ts = TimesheetData(username, tsname, date, stime, etime, totaltime, description, img)
                        arrTimeSheet.add(ts)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }

        })

        MainCode(userList)
    }

    private fun MainCode(users: List<Users>) {

        var btnlogin : Button = findViewById(R.id.btnLogin)
        var btnreg : Button = findViewById(R.id.btnRegister)

        btnlogin.setOnClickListener()
        {

            var username : EditText = findViewById(R.id.txtUsername)
            var password : EditText = findViewById(R.id.txtPassword)

            var found = false

            for(i in 0 until users.size)
            {
                //error handling
                if((username.text.toString().equals(users[i].username)) && (password.text.toString().equals(users[i].password)))
                {
                    Toast.makeText(this, "Successfully logged in!", Toast.LENGTH_SHORT).show()

                    found = true

                    SignedIn = i
                    val int = Intent(this, Home::class.java)
                    startActivity(int)
                    break
                }
            }
            if(found == false)
            {
                username.setError("Please enter valid username!")
                password.setError("Please enter valid password!")
            }

        }

        btnreg.setOnClickListener()
        {
            val int = Intent(this, Register::class.java)
            startActivity(int)
        }
    }

    override fun onBackPressed() {

    }


}