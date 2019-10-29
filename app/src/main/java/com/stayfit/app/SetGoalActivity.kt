package com.stayfit.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SetGoalActivity : AppCompatActivity() {

    private var mDatabase: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setgoal)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        val user = mAuth!!.currentUser
        val userId = user!!.uid

        val stepGoal = findViewById<EditText>(R.id.et1)
        val calorieGoal = findViewById<EditText>(R.id.et2)

        val setgoal = findViewById<Button>(R.id.setgoal)
        setgoal.setOnClickListener(View.OnClickListener {
            if (stepGoal.text.toString().length == 0) {
                stepGoal.error = "Set Steps Goal"
                return@OnClickListener
            } else if (calorieGoal.text.toString().length == 0) {
                calorieGoal.error = "Set Calorie Goal!"
                return@OnClickListener
            }

            mDatabase!!.child("Users").child(userId).child("stepgoal").setValue(stepGoal.text.toString())
            mDatabase!!.child("Users").child(userId).child("caloriegoal").setValue(calorieGoal.text.toString())

            mDatabase!!.child("Users").child(userId).child("stepgoal").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    mSeries = java.lang.Float.parseFloat(dataSnapshot.value.toString())
                    Log.d("mSeries", mSeries.toString())
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

            mDatabase!!.child("Users").child(userId).child("caloriegoal").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    mSeries1 = java.lang.Float.parseFloat(dataSnapshot.value.toString())
                    Log.d("mSeries1", mSeries1.toString())
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

            val myIntent = Intent(this@SetGoalActivity, MainActivity::class.java)
            startActivity(myIntent)
        })
    }

    companion object {
        var mSeries = 0f
        var mSeries1 = 0f
    }
}
