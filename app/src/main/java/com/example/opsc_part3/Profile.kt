package com.example.opsc_part3

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.regex.Pattern

class Profile : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var btnImage: ImageButton
    private val PICK_IMAGE = 1
    private var imgString: String? = ""

    companion object
    {
        val dbref = Firebase.database
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the activity fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_profile)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        val openDrawer : ImageButton = findViewById(R.id.btnNav)
        openDrawer.setOnClickListener()
        {
            if (!drawerLayout.isDrawerOpen(navView)) {
                drawerLayout.openDrawer(navView)
            }
        }

        //Intents
        navView.setNavigationItemSelectedListener {
            val timeint = Intent(this, TimesheetList::class.java)
            val homeint = Intent(this, Home::class.java)
            val categint = Intent(this, Categories::class.java)
            val logout = Intent(this, MainActivity::class.java)
            val reports = Intent(this, Reports::class.java)
            val settings = Intent(this, Settings::class.java)

            when(it.itemId){
                R.id.nav_home -> startActivity(homeint)
                R.id.nav_settings -> startActivity(settings)
                R.id.nav_report -> startActivity(reports)
                R.id.nav_timesheet -> startActivity(timeint)
                R.id.nav_categories -> startActivity(categint)
                R.id.nav_logout -> startActivity(logout)
            }
            true

        }


        // Main Code
        var username : EditText = findViewById(R.id.txtTsName)
        var email : EditText = findViewById(R.id.txtTsDescription)
        var password : EditText = findViewById(R.id.txtTsTotalTime)
        var fullname : EditText = findViewById(R.id.txtTsDate)
        btnImage = findViewById(R.id.btnAddTsImage)

        for (user in MainActivity.userList)
        {
            if (user.username.equals(MainActivity.userList[MainActivity.SignedIn].username))
            {
                username.hint = user.username
                if (user.email == null)
                {
                    email.hint = "Email"
                }
                else
                {
                    email.setText(user.email)
                }
                if (user.fullname == null)
                {
                    fullname.hint = "Full Name"
                }
                else
                {
                    fullname.setText(user.fullname)
                }
                password.setText(user.password)

                if (user.image != null)
                {
                    imgString = user.image
                    val bitmap = decodeBase64ToBitmap(imgString)
                    bitmap?.let {
                        btnImage.setImageBitmap(it)
                    }
                }
            }
        }

        btnImage.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE)
        }

        var saveChanges : Button = findViewById(R.id.btnTsSaveChanges)
        saveChanges.setOnClickListener()
        {
            var save = false
            for (user in MainActivity.userList)
            {
                if (user.username.equals(MainActivity.userList[MainActivity.SignedIn].username))
                {
                    if ((email.text.toString() != "") && (isValidEmail(email)))
                    {
                        user.email = email.text.toString()
                    }
                    else if ((email.text.toString() != "") && (!isValidEmail(email)))
                    {
                        email.setError("Email is not in the right format")
                        save = false
                        break
                    }
                    if (fullname.text.toString() != "")
                    {
                        user.fullname = fullname.text.toString()
                    }

                    user.password = password.text.toString()
                    user.image = imgString
                    save = true
                }
            }

            if (save == true)
            {
                val ref = dbref.getReference("Users/" + (MainActivity.SignedIn + 1) + "/Username")
                ref.setValue(MainActivity.userList[MainActivity.SignedIn].username)
                val ref2 = dbref.getReference("Users/" + (MainActivity.SignedIn + 1) + "/Password")
                ref2.setValue(password.text.toString())
                val ref3 = dbref.getReference("Users/" + (MainActivity.SignedIn + 1) + "/Email")
                ref3.setValue(email.text.toString())
                val ref4 = dbref.getReference("Users/" + (MainActivity.SignedIn + 1) + "/FullName")
                ref4.setValue(fullname.text.toString())
                val ref5 = dbref.getReference("Users/" + (MainActivity.SignedIn + 1) + "/Image")
                ref5.setValue(imgString)
            }

            Toast.makeText(this, "Profile changes have been saved", Toast.LENGTH_SHORT).show()
            val int = Intent(this, Home::class.java)
            startActivity(int)
        }

        val btndeleteprofile : Button = findViewById(R.id.btnDeleteProfile)
        btndeleteprofile.setOnClickListener()
        {
            val ref = dbref.getReference("Users/" + (MainActivity.SignedIn + 1) + "/Username")
            ref.setValue("")
            val ref2 = dbref.getReference("Users/" + (MainActivity.SignedIn + 1) + "/Password")
            ref2.setValue("")
            val ref3 = dbref.getReference("Users/" + (MainActivity.SignedIn + 1) + "/Email")
            ref3.setValue("")
            val ref4 = dbref.getReference("Users/" + (MainActivity.SignedIn + 1) + "/FullName")
            ref4.setValue("")
            val ref5 = dbref.getReference("Users/" + (MainActivity.SignedIn + 1) + "/Image")
            ref5.setValue("")

            Toast.makeText(this, "Profile deleted", Toast.LENGTH_SHORT).show()
            val int = Intent(this, MainActivity::class.java)
            startActivity(int)
        }


    }

    private fun decodeBase64ToBitmap(base64Str: String?): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri? = data?.data
            if (selectedImage != null) {
                val imageStream: InputStream? = contentResolver.openInputStream(selectedImage)
                val bitmap: Bitmap = BitmapFactory.decodeStream(imageStream)
                btnImage.setImageBitmap(bitmap)

                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                imgString = Base64.encodeToString(byteArray, Base64.DEFAULT)
            }
        }
    }

    fun isValidEmail(editText: EditText): Boolean {
        val emailPattern = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE
        )
        val email = editText.text.toString().trim()
        return emailPattern.matcher(email).matches()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){

        }
        return true
    }

    override fun onBackPressed() {

    }
}