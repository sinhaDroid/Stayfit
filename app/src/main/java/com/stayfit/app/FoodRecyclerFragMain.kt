package com.stayfit.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by maddi on 3/21/2016.
 */
class FoodRecyclerFragMain : AppCompatActivity() {
    //private boolean mSidePanel;
    private var mToolbar: Toolbar? = null
    private var mActionBar: androidx.appcompat.app.ActionBar? = null

    private var mDatabase: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

    private fun getCaloriesRef(ref: String): DatabaseReference {
        val user = mAuth!!.currentUser
        val userId = user!!.uid
        return mDatabase!!.child("Calories").child(userId).child(ref)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_frag_change_main)
        //Load common fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.rcfrag_main, FoodRecyclerViewMain.newInstance()).commit()
        }

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        mToolbar = findViewById(R.id.recycler_toolbar)
        setSupportActionBar(mToolbar)
        mActionBar = supportActionBar
        mActionBar!!.setDisplayHomeAsUpEnabled(true)
        mActionBar!!.setHomeButtonEnabled(true)

        getCaloriesRef("totalcalories").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                calRef1 = java.lang.Float.parseFloat(dataSnapshot.value.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        getCaloriesRef("totalfat").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user_fat1 = java.lang.Float.parseFloat(dataSnapshot.value.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        getCaloriesRef("totalcarbs").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user_carbs1 = java.lang.Float.parseFloat(dataSnapshot.value.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        getCaloriesRef("totalprotein").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user_protein1 = java.lang.Float.parseFloat(dataSnapshot.value.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    companion object {

        var calRef1 = 0f
        var user_fat1 = 0f
        var user_carbs1 = 0f
        var user_protein1 = 0f
    }

}
