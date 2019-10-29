package com.stayfit.app

import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import com.natasa.progressviews.CircleProgressBar

/**
 * Created by maddi on 4/20/2016.
 */
class FoodSummaryActivity : AppCompatActivity() {
    private var food_fat: Float = 0.toFloat()
    private var food_carbs: Float = 0.toFloat()
    private var food_protein: Float = 0.toFloat()
    private var max_fat = 70f
    private var max_carbs = 300f
    private var max_protein = 180f

    private val displayHeight: Int
        get() {
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            return metrics.widthPixels
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foodsummary)
        val fats = findViewById<View>(R.id.fats_progress) as CircleProgressBar
        val carbs = findViewById<View>(R.id.carbs_progress) as CircleProgressBar
        val protein = findViewById<View>(R.id.protein_progress) as CircleProgressBar

        food_carbs = FoodMyRecyclerViewAdapter.totalcarbs
        food_fat = FoodMyRecyclerViewAdapter.totalfat
        food_protein = FoodMyRecyclerViewAdapter.totalprotein
        Log.d("Food Summary", food_carbs.toString() + food_fat.toString() + food_protein.toString())

        // Animation
        val translation = TranslateAnimation(0f, 0f, 0f, 180f)
        translation.startOffset = 100
        translation.duration = 2000
        translation.fillAfter = true
        translation.interpolator = BounceInterpolator()

        val translation1 = TranslateAnimation(0f, 0f, 0f, 370f)
        translation1.startOffset = 100
        translation1.duration = 2000
        translation1.fillAfter = true
        translation1.interpolator = BounceInterpolator()

        // Fats Progress Bar
        if (food_fat > 0) {
            fats.setProgress(100 * food_fat / max_fat)
        } else
            fats.setProgress(100 * LoginActivity.user_fat / max_fat)
        fats.widthProgressBackground = 25f
        fats.widthProgressBarLine = 25f
        if (food_fat > 0) {
            fats.setText(food_fat.toString())
        } else {
            fats.setText(LoginActivity.user_fat.toString())
        }
        fats.textSize = 35
        fats.backgroundColor = Color.LTGRAY
        fats.setRoundEdgeProgress(true)
        fats.startAnimation(translation)

        // Carbs Progress Bar
        if (food_carbs > 0) {
            carbs.setProgress(100 * food_carbs / max_carbs)
        } else
            carbs.setProgress(100 * LoginActivity.user_carbs / max_carbs)
        carbs.startAnimation(translation)
        carbs.widthProgressBackground = 25f
        carbs.widthProgressBarLine = 25f
        if (food_carbs > 0) {
            carbs.setText(food_carbs.toString())
        } else {
            carbs.setText(LoginActivity.user_carbs.toString())
        }
        carbs.textSize = 35
        carbs.backgroundColor = Color.LTGRAY
        carbs.setRoundEdgeProgress(true)

        // protein Progress Bar
        if (food_protein > 0) {
            protein.setProgress(100 * food_protein / max_protein)
        } else
            protein.setProgress(100 * LoginActivity.user_protein / max_protein)
        protein.widthProgressBackground = 25f
        protein.widthProgressBarLine = 25f
        if (food_protein > 0) {
            protein.setText(food_protein.toString())
        } else {
            protein.setText(LoginActivity.user_protein.toString())
        }
        protein.textSize = 35
        protein.backgroundColor = Color.LTGRAY
        protein.setRoundEdgeProgress(true)
        protein.animation = translation1

    }
}
