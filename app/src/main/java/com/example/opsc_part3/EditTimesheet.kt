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
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.io.ByteArrayOutputStream
import java.io.InputStream

class EditTimesheet : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var btnImage: ImageButton
    private val PICK_IMAGE = 1
    private var imgString: String? = ""
    private var entry: String? = "ts with image"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the activity fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_edit_timesheet)

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
            val profile = Intent(this, Profile::class.java)

            when(it.itemId){
                R.id.nav_home -> startActivity(homeint)
                R.id.nav_settings -> startActivity(settings)
                R.id.nav_report -> startActivity(reports)
                R.id.nav_timesheet -> startActivity(timeint)
                R.id.nav_categories -> startActivity(categint)
                R.id.nav_profile -> startActivity(profile)
                R.id.nav_logout -> startActivity(logout)
            }
            true

        }


        // Main Code
        val items = ArrayList<String?>()
        for (user in MainActivity.userList)
        {
            if (user.username.equals(MainActivity.userList[MainActivity.SignedIn].username))
            {
                for (cat in MainActivity.arrCategoryData)
                {
                    if (user.username.equals(cat.username))
                    {
                        items.add(cat.CategoryName)
                    }
                }
            }
        }

        val cat : AutoCompleteTextView = findViewById(R.id.cmbTsCategory)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
        cat.setAdapter(adapter)

        cat.inputType = 0
        cat.setOnClickListener()
        {
            cat.showDropDown()
        }


        var tsname : EditText = findViewById(R.id.txtTsName)
        var tsdesc : EditText = findViewById(R.id.txtTsDescription)
        var tsdate : EditText = findViewById(R.id.txtTsDate)
        var tstotaltime : EditText = findViewById(R.id.txtTsTotalTime)
        var btnImage : ImageButton = findViewById(R.id.btnAddTsImage)

        for (ts in MainActivity.arrTimeSheet)
        {
            if ((ts.username.equals(MainActivity.userList[MainActivity.SignedIn].username)) && (ts.tsName.equals(entry)))
            {
                tsname.setText(ts.tsName)
                tsdesc.setText(ts.description)
                tsdate.setText(ts.date)
                tstotaltime.setText(ts.totalTime)
                cat.setText(ts.tsCategory)

                if (ts.image != null)
                {
                    imgString = ts.image
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

            for (ts in MainActivity.arrTimeSheet)
            {
                if ((ts.username.equals(MainActivity.userList[MainActivity.SignedIn].username)) && (ts.tsName.equals(entry)))
                {
                    if ((tsname.text.toString() != "") && (tsdesc.text.toString() != ""))
                    {
                        ts.tsName = tsname.text.toString()
                        ts.description = tsdesc.text.toString()
                        ts.tsCategory = cat.text.toString()
                    }

                    save = true
                }
            }

            if (save == true)
            {

            }

            Toast.makeText(this, "Entry changes have been saved", Toast.LENGTH_SHORT).show()
            val int = Intent(this, TimesheetList::class.java)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){

        }
        return true
    }

    override fun onBackPressed() {

    }
}