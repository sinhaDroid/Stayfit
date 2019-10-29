package com.stayfit.app

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.hookedonplay.decoviewlib.DecoView
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect
import com.hookedonplay.decoviewlib.charts.SeriesItem
import com.hookedonplay.decoviewlib.events.DecoEvent

class MainActivity : AppCompatActivity(), SensorEventListener, NavigationView.OnNavigationItemSelectedListener {
    internal var activityRunning: Boolean = false
    /**
     * DecoView animated arc based chart
     */
    private var mDecoView: DecoView? = null
    /**
     * Data series index used for controlling animation of [DecoView]. These are set when
     * the data series is created then used in [.createEvents] to specify what series to
     * apply a given event to
     */
    private var mBackIndex: Int = 0
    private var mSeries1Index: Int = 0
    private val mSeries2Index: Int = 0
    private val mSeries3Index: Int = 0
    // Sensor data
    private var textView: TextView? = null
    private val msensorManager: SensorManager? = null
    private var sensorManager: SensorManager? = null
    private var navigationView: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        navigationView = findViewById<View>(R.id.navigation_view) as NavigationView
        navigationView!!.setNavigationItemSelectedListener(this)

        val mHeaderView = navigationView!!.getHeaderView(0)

        val nameId = mHeaderView.findViewById<View>(R.id.txt1) as TextView
        nameId.text = LoginActivity.USER_NAME
        val emailId = mHeaderView.findViewById<View>(R.id.txt2) as TextView
        emailId.text = LoginActivity.USER_EMAIL
        drawerLayout = findViewById<View>(R.id.drawer) as DrawerLayout
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }

        drawerLayout!!.setDrawerListener(actionBarDrawerToggle)

        actionBarDrawerToggle.syncState()

        mSeriesMax = SetGoalActivity.mSeries
        Log.d("SetGoal mseries", SetGoalActivity.mSeries.toString())
        if (mSeriesMax == 0f) {
            mSeriesMax = LoginActivity.mSeries1
        }
        val cap1: String
        val m = FloatArray(1)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Go to data image button
        val dn = findViewById<View>(R.id.datanext) as ImageView
        // Go to Chart Data page
        dn.setOnClickListener { v ->
            val intent1 = Intent(this@MainActivity, ActivityViewPager::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity, v, "testAnimation")
            this@MainActivity.startActivity(intent1, options.toBundle())
        }

        mDecoView = findViewById<View>(R.id.dynamicArcView) as DecoView

        /* if(mSeriesMax == 0)
        {
            Log.d("COming","in");
            final Firebase[] cref = {ref.child("stepgoal")};
            cref[0].addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println(dataSnapshot.getValue());
                    Log.d("COming", "in in");
                    mSeriesMax = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
                    Log.d("mSeries", (String.valueOf(mSeriesMax)));
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        } */
        Log.d("mSeries out", mSeriesMax.toString())
        if (mSeriesMax > 0) {
            Log.d("mSeries out in", mSeriesMax.toString())
            // Create required data series on the DecoView
            createBackSeries()
            createDataSeries1()

            // Setup events to be fired on a schedule
            createEvents()
        }
    }

    // Step Counter

    override fun onResume() {
        super.onResume()
        activityRunning = true
        val countSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (countSensor != null) {
            sensorManager!!.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show()
        }

    }

    override fun onPause() {
        super.onPause()
        activityRunning = false
        //         if you unregister the last listener, the hardware will stop detecting step events
        //        sensorManager.unregisterListener(this);
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (activityRunning) {
            textView = findViewById<View>(R.id.textRemaining) as TextView
            textView!!.text = event.values[0].toString()
            evsteps = event.values[0]
        }

    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    private fun createBackSeries() {
        val seriesItem = SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0f, mSeriesMax, 0f)
                .setInitialVisibility(true)
                .build()

        mBackIndex = mDecoView!!.addSeries(seriesItem)
    }

    private fun createDataSeries1() {
        val seriesItem = SeriesItem.Builder(Color.parseColor("#FFFF8800"))
                .setRange(0f, mSeriesMax, 0f)
                .setInitialVisibility(false)
                .build()

        Log.d("mSeries Data1", mSeriesMax.toString())

        val textPercentage = findViewById<View>(R.id.textPercentage) as TextView
        seriesItem.addArcSeriesItemListener(object : SeriesItem.SeriesItemListener {
            override fun onSeriesItemAnimationProgress(percentComplete: Float, currentPosition: Float) {
                val percentFilled = (currentPosition - seriesItem.minValue) / (seriesItem.maxValue - seriesItem.minValue)
                textPercentage.setText(String.format("%.0f%%", percentFilled * 100f))
            }

            override fun onSeriesItemDisplayProgress(percentComplete: Float) {

            }
        })


        val textToGo = findViewById<View>(R.id.textRemaining) as TextView
        seriesItem.addArcSeriesItemListener(object : SeriesItem.SeriesItemListener {
            override fun onSeriesItemAnimationProgress(percentComplete: Float, currentPosition: Float) {
                textToGo.setText(String.format("%d Steps to goal", (seriesItem.maxValue - currentPosition).toInt()))

            }

            override fun onSeriesItemDisplayProgress(percentComplete: Float) {

            }
        })

        val textActivity1 = findViewById<View>(R.id.textActivity1) as TextView
        seriesItem.addArcSeriesItemListener(object : SeriesItem.SeriesItemListener {
            override fun onSeriesItemAnimationProgress(percentComplete: Float, currentPosition: Float) {
                textActivity1.setText(String.format("%.0f Steps", currentPosition))
            }

            override fun onSeriesItemDisplayProgress(percentComplete: Float) {

            }
        })

        mSeries1Index = mDecoView!!.addSeries(seriesItem)
    }

    private fun createEvents() {
        cont++
        mDecoView!!.executeReset()

        if (cont == 1) {
            resetText()
            mDecoView!!.addEvent(DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                    .setIndex(mSeries1Index)
                    .setDelay(0)
                    .setDuration(1000)
                    .setDisplayText("")
                    .setListener(object : DecoEvent.ExecuteEventListener {
                        override fun onEventStart(decoEvent: DecoEvent) {

                        }

                        override fun onEventEnd(decoEvent: DecoEvent) {
                            createEvents()
                        }
                    })
                    .build())
        }
        mDecoView!!.addEvent(DecoEvent.Builder(mSeriesMax)
                .setIndex(mBackIndex)
                .setDuration(3000)
                .setDelay(100)
                .build())

        mDecoView!!.addEvent(DecoEvent.Builder(evsteps)
                .setIndex(mSeries1Index)
                .setDelay(3250)
                .build())

        mDecoView!!.addEvent(DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                .setIndex(mSeries1Index)
                .setDelay(20000)
                .setDuration(3000)
                .setDisplayText("")
                .setListener(object : DecoEvent.ExecuteEventListener {
                    override fun onEventStart(decoEvent: DecoEvent) {

                    }

                    override fun onEventEnd(decoEvent: DecoEvent) {
                        createEvents()
                    }
                })
                .build())

    }

    private fun resetText() {
        (findViewById<View>(R.id.textPercentage) as TextView).text = ""
        (findViewById<View>(R.id.textRemaining) as TextView).text = ""
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        when (id) {
            R.id.item1 -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.item2 -> {
                val intent = Intent(this, OverviewActivity::class.java)
                startActivity(intent)
            }
            R.id.item3 -> {
                val intent = Intent(this, AccountActivity::class.java)
                startActivity(intent)
            }
            R.id.item4 -> {
                AuthUI.getInstance().signOut(this).addOnCompleteListener { Toast.makeText(this@MainActivity, "Signed out successfully", Toast.LENGTH_SHORT).show() }
                val myIntent = Intent(this, LoginActivity::class.java)
                myIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP// clear back stack
                startActivity(myIntent)
                finish()
            }
            else -> {
            }
        }
        drawerLayout!!.closeDrawer(GravityCompat.START)
        return true
    }

    companion object {

        var evsteps: Float = 0.toFloat()
        var cont = 0
        /**
         * Maximum value for each data series in the [DecoView]. This can be different for each
         * data series, in this example we are applying the same all data series
         */
        var mSeriesMax = 0f
    }


}
