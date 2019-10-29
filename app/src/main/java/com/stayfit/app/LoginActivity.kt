package com.stayfit.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast

import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import java.util.Arrays

class LoginActivity : AppCompatActivity() {
    // Authentication providers
    private var providers: List<AuthUI.IdpConfig> = listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build())
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        for (provider in AuthUI.SUPPORTED_PROVIDERS) {
            Log.v(this.javaClass.name, provider)
        }

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mAuthListener = FirebaseAuth.AuthStateListener { updateInfo() }

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.stayfit)
                        .build(),
                RC_SIGN_IN)
    }


    private fun startUpTasks() {
        val isFirstRun = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .getBoolean("isFirstRun", true)

        //  If the activity has never started before...
        if (isFirstRun) {
            //  Launch app intro
            val i = Intent(this@LoginActivity, AppIntroActivity::class.java)
            startActivity(i)

            getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).edit()
                    .putBoolean("isFirstRun", false).apply()

            initializeUserInfo()
        } else {
            getUserInfo()
            val myIntent = Intent(this@LoginActivity, MainActivity::class.java)
            this@LoginActivity.startActivity(myIntent)
        }
    }

    private fun initializeUserInfo() {
        val user = mAuth!!.currentUser
        val userId = user!!.uid

        val usersRef = mDatabase!!.child("Users")
        val stepsRef = mDatabase!!.child("Steps")
        val caloriesRef = mDatabase!!.child("Calories")

        val newUser = User("", "", "", 0, "", 0f, 0, 0)
        usersRef.child(userId).setValue(newUser)

        val steps = Steps(0)
        stepsRef.child(userId).setValue(steps)

        val calories = Calories(0f, 0f, 0f, 0f)
        caloriesRef.child(userId).setValue(calories)
    }

    private fun getUsersRef(ref: String): DatabaseReference {
        val user = mAuth!!.currentUser
        val userId = user!!.uid
        return mDatabase!!.child("Users").child(userId).child(ref)
    }

    private fun getCaloriesRef(ref: String): DatabaseReference {
        val user = mAuth!!.currentUser
        val userId = user!!.uid
        return mDatabase!!.child("Calories").child(userId).child(ref)
    }

    private fun getUserInfo() {
        getUsersRef("stepgoal").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mSeries1 = java.lang.Float.parseFloat(dataSnapshot.value.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })

        getUsersRef("caloriegoal").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mSeries2 = java.lang.Float.parseFloat(dataSnapshot.value.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })

        getUsersRef("name").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                USER_NAME = dataSnapshot.value.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })

        getCaloriesRef("totalcalories").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                calRef = java.lang.Float.parseFloat(dataSnapshot.value.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })

        getCaloriesRef("totalfat").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user_fat = java.lang.Float.parseFloat(dataSnapshot.value.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })

        getCaloriesRef("totalcarbs").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user_carbs = java.lang.Float.parseFloat(dataSnapshot.value.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })

        getCaloriesRef("totalprotein").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user_protein = java.lang.Float.parseFloat(dataSnapshot.value.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    private fun updateInfo() {
        val user = mAuth!!.currentUser
        if (user != null) {
            USER_ID = user.uid
            USER_EMAIL = user.email
            // Picasso.with(ActivityFUIAuth.this).load(user.getPhotoUrl()).into(imgProfile);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                Log.d(this.javaClass.name, "This user signed in with " + response!!.providerType)
                startUpTasks()
                updateInfo()
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, "Signin cancelled", Toast.LENGTH_SHORT).show()
                    return
                }

                if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "Check network connection and try again", Toast.LENGTH_LONG).show()
                    return
                }

                Toast.makeText(this, "Unexpected Error, we are trying to resolve the issue. Please check back soon", Toast.LENGTH_LONG).show()
                Log.e(TAG, "Sign-in error: ", response.error)
            }
        }
    }

    companion object {

        val RC_SIGN_IN = 0
        private val TAG = "LoginActivity"
        var USER_ID = ""
        var USER_EMAIL: String? = ""
        var USER_NAME = ""
        var mSeries1 = 0f
        var mSeries2 = 0f
        var calRef = 0f
        var user_fat = 0f
        var user_carbs = 0f
        var user_protein = 0f
    }
}
