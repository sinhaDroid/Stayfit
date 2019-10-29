package com.stayfit.app

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView

import com.natasa.progressviews.CircleProgressBar
import com.natasa.progressviews.utils.OnProgressViewListener

/**
 * Created by maddi on 4/20/2016.
 */
class OverviewActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var navigationView: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null
    internal var food_calories: Float = 0.toFloat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

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

        food_calories = FoodMyRecyclerViewAdapter.caloriecount
        Log.d("Calories for Overview", FoodRecyclerFragMain.calRef1.toString())
        // Setting Steps and Calories
        stepMax = SetGoalActivity.mSeries
        if (stepMax == 0f) {
            stepMax = LoginActivity.mSeries1
        }
        calorieMax = SetGoalActivity.mSeries1
        Log.d("SetGoal mseries", SetGoalActivity.mSeries.toString())
        if (calorieMax == 0f) {
            calorieMax = LoginActivity.mSeries2
        }
        val steps = findViewById<View>(R.id.step_progress) as CircleProgressBar
        val food = findViewById<View>(R.id.food_progress) as CircleProgressBar

        // Animation
        val translation: TranslateAnimation
        translation = TranslateAnimation(0f, 0f, 0f, 180f)
        translation.startOffset = 100
        translation.duration = 2000
        translation.fillAfter = true
        translation.interpolator = BounceInterpolator()

        val translation1: TranslateAnimation
        translation1 = TranslateAnimation(0f, 0f, 0f, 220f)
        translation1.startOffset = 100
        translation1.duration = 2000
        translation1.fillAfter = true
        translation1.interpolator = BounceInterpolator()

        // Steps Progress Bar
        steps.setProgress(100 * MainActivity.evsteps / stepMax)
        steps.width = 280
        steps.widthProgressBackground = 25f
        steps.widthProgressBarLine = 20f
        steps.setText(MainActivity.evsteps.toString() + "/ " + stepMax)
        steps.textSize = 40
        steps.backgroundColor = Color.LTGRAY
        steps.setRoundEdgeProgress(true)
        steps.startAnimation(translation)
        //steps.setProgressIndeterminateAnimation(1000);
        // Food Progress Bar
        if (food_calories > 0) {
            food.setProgress(100 * food_calories / calorieMax)
            food.setText("$food_calories/ $calorieMax")
        } else {
            food.setProgress(100 * LoginActivity.calRef / calorieMax)
            food.setText(LoginActivity.calRef.toString() + "/ " + calorieMax)
        }
        food.width = 200
        food.widthProgressBackground = 25f
        food.widthProgressBarLine = 40f
        food.textSize = 70
        food.backgroundColor = Color.LTGRAY
        food.setRoundEdgeProgress(true)
        food.animation = translation1
        //food.setProgressIndeterminateAnimation(2000);

        // Listeners
        steps.setOnProgressViewListener(object : OnProgressViewListener {
            internal var progress = 0f

            override fun onFinish() {
                //do something on progress finish
                //steps.setText("done!");
                // circleProgressBar.resetProgressBar();
            }

            override fun onProgressUpdate(prog: Float) {
                steps.setText("" + prog.toInt())
                setProgress(prog)
            }

            override fun setProgress(prog: Float) {
                progress = prog
            }

            override fun getprogress(): Int {
                return progress.toInt()
            }
        })

        food.setOnProgressViewListener(object : OnProgressViewListener {
            internal var progress = 0f

            override fun onFinish() {
                //do something on progress finish
                //food.setText("d");
                // circleProgressBar.resetProgressBar();
            }

            override fun onProgressUpdate(progress: Float) {
                food.setText("" + progress.toInt())
                setProgress(progress)
            }

            override fun setProgress(prog: Float) {
                progress = prog
            }

            override fun getprogress(): Int {
                return progress.toInt()
            }
        })


        // On Click Listeners for Activities
        val food_summary = findViewById<View>(R.id.food_summary) as ImageView
        food_summary.setOnClickListener {
            val intent = Intent(this@OverviewActivity, FoodSummaryActivity::class.java)
            startActivity(intent)
        }

        val share_a_run = findViewById<View>(R.id.share_a_run) as ImageView
        share_a_run.setOnClickListener {
            val intent = Intent(this@OverviewActivity, AskLocationActivity::class.java)
            startActivity(intent)
        }

        // On Touch Listeners for Image animations
        /*
        food_summary.setOnTouchListener(new View.OnTouchListener() {
            private Rect rect;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    food_summary.setColorFilter(Color.argb(31, 58, 147, 0));
                    rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    food_summary.setColorFilter(Color.argb(0, 0, 0, 0));
                }
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                        food_summary.setColorFilter(Color.argb(0, 0, 0, 0));
                    }
                }
                return false;
            }
        });

        share_a_run.setOnTouchListener(new View.OnTouchListener() {
            private Rect rect;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    share_a_run.setColorFilter(Color.argb(31, 58, 147, 0));
                    rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    share_a_run.setColorFilter(Color.argb(0, 0, 0, 0));
                }
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                        share_a_run.setColorFilter(Color.argb(0, 0, 0, 0));
                    }
                }
                return false;
            }
        });
*/
        // Add Calories
        val addcal = findViewById<View>(R.id.addcalories) as ImageView
        addcal.setOnClickListener {
            val intent1 = Intent(this@OverviewActivity, FoodRecyclerFragMain::class.java)
            startActivity(intent1)
        }

        //        Firebase ref = new Firebase("https://healthkit.firebaseio.com/Calories");
        //        Query queryRef = ref.child(LoginActivity.USER_ID).orderByChild("steps");
        //
        //        queryRef.addChildEventListener(new ChildEventListener() {
        //            @Override
        //            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
        //                String facts = (String) snapshot.getValue();
        //                System.out.println(snapshot.getKey() + " was " + facts + " meters tall");
        //            }
        //
        //            @Override
        //            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        //
        //            }
        //
        //            @Override
        //            public void onChildRemoved(DataSnapshot dataSnapshot) {
        //
        //            }
        //
        //            @Override
        //            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        //
        //            }
        //
        //            @Override
        //            public void onCancelled(FirebaseError firebaseError) {
        //
        //            }
        //        });
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        when (id) {
            R.id.item1 -> {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.item2 -> {
                intent = Intent(this, OverviewActivity::class.java)
                startActivity(intent)
            }
            R.id.item3 -> {
                intent = Intent(this, AccountActivity::class.java)
                startActivity(intent)
            }
            R.id.item4 -> {
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
        var stepMax = 0f
        var calorieMax = 0f
    }


}
