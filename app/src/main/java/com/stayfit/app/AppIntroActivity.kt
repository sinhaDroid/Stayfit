package com.stayfit.app

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast

import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment

/**
 * Created by maddi on 4/23/2016.
 */
class AppIntroActivity : AppIntro() {


    // Please DO NOT override onCreate. Use init.
    override fun init(savedInstanceState: Bundle?) {
        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance("Eye Catching Visuals", "Your Daily Statistics", R.drawable.appintro1, resources.getColor(R.color.appintro1)))
        addSlide(AppIntroFragment.newInstance("Invite Friends", "Share A Run with them", R.drawable.appintro3, resources.getColor(R.color.appintro3)))
        addSlide(AppIntroFragment.newInstance("", "Get Started", R.drawable.appintro4, resources.getColor(R.color.appintro4)))

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#F44336"))
        setSeparatorColor(Color.parseColor("#2196F3"))

        // Hide Skip/Done button.
        showSkipButton(true)
        isProgressButtonEnabled = true

        // Turn vibration on and set intensity.
        setVibrate(true)
        setVibrateIntensity(30)
    }

    override fun onSkipPressed() {
        // Do something when users tap on Skip button.
        val i = Intent(this@AppIntroActivity, EnterInfoActivity::class.java)
        startActivity(i)
    }

    override fun onDonePressed() {
        // Do something when users tap on Done button.
        val i = Intent(this@AppIntroActivity, EnterInfoActivity::class.java)
        startActivity(i)

        Toast.makeText(applicationContext, "Finished", Toast.LENGTH_SHORT).show()
    }

    override fun onSlideChanged() {
        // Do something when the slide changes.
    }

    override fun onNextPressed() {
        // Do something when users tap on Next button.
        Toast.makeText(applicationContext, "Cannot Skip", Toast.LENGTH_SHORT).show()
    }

}