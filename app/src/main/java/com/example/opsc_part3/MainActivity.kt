package com.example.opsc_part3

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    private lateinit var rootNode : FirebaseDatabase
    private lateinit var userReference: DatabaseReference

    companion object
    {
        var arrUsers = ArrayList<Users>()
        var SignedIn : Int = 0
        var numUsers : Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rootNode = FirebaseDatabase.getInstance()
        userReference = rootNode.getReference("Users")

        userReference.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapshot in snapshot.children)
                {
                    val dc = snapshot.getValue(Users::class.java)
                    arrUsers.add(Users("${dc?.username}", "${dc?.password}"))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }
        })

        arrUsers.add(Users("test1", "test2"))
        arrUsers.add(Users("test3", "test4"))
        for (user in arrUsers)
        {
            Log.d("test", user.username + " " + user.password)
        }

        var btnlogin : Button = findViewById(R.id.btnLogin)
        var btnreg : Button = findViewById(R.id.btnRegister)

        btnlogin.setOnClickListener()
        {

            var username : EditText = findViewById(R.id.txtUsername)
            var password : EditText = findViewById(R.id.txtPassword)

            var found = false
            SignedIn = -1

            for(i in 0 until arrUsers.size)
            {
                //error handling
                if((username.text.toString().equals(arrUsers[i].username)) && (password.text.toString().equals(arrUsers[i].password)))
                {
                    Toast.makeText(this, "Successfully logged in!", Toast.LENGTH_SHORT).show()

                    found = true

                    val int = Intent(this, Home::class.java)
                    startActivity(int)
                    SignedIn = i
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


}