package com.stayfit.app

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.location.Address
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

/**
 * Created by maddi on 4/11/2016.
 */
class LoadMapActivity : AppCompatActivity() {

    val PICK_CONTACT = 2015
    private val DEBUG_TAG = "SMS Manager"
    var phoneno = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        int permissionCheck = ContextCompat.checkSelfPermission(LoadMapActivity.this,
        //                Manifest.permission.LOCATION_HARDWARE);
        //        ActivityCompat.requestPermissions(LoadMapActivity.this,
        //                new String[]{Manifest.permission.LOCATION_HARDWARE},);
        setContentView(R.layout.activity_mapload)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, MapLoadFragment())
                    .commit()
        }

        val contact = findViewById<View>(R.id.contact_picker) as ImageView
        contact.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
            startActivityForResult(i, PICK_CONTACT)
        }

        val invite = findViewById<View>(R.id.invite) as Button
        invite.setOnClickListener { sendSMSMessage() }

    }

    // Contacts picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_CONTACT && resultCode == Activity.RESULT_OK) {
            val contactUri = data!!.data
            val cursor = contentResolver.query(contactUri!!, null, null, null, null)
            cursor!!.moveToFirst()
            val column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            Log.d("phone number", cursor.getString(column))
            val dummy = findViewById<View>(R.id.dummy) as TextView
            dummy.text = cursor.getString(column)
            phoneno = cursor.getString(column)
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

    // Send SMS
    protected fun sendSMSMessage() {
        Log.i("Send SMS", "")
        val phoneNo = phoneno
        //String lat = getIntent().getExtras().getString("Latitude");
        //String lng = getIntent().getExtras().getString("Longitude");
        // Replace latitude and longitude values
        val msgaddress = AskLocationActivity.address1
        val message = "Shall we run together, Location:" + "http://maps.google.com/?q=" + msgaddress.latitude + "," + msgaddress.longitude

        Log.d("Message", message)
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNo, null, message, null, null)
            Toast.makeText(applicationContext, "Invitation sent.", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "SMS faild, please try again.", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

    }
}
