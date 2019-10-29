package com.stayfit.app

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer

class ActivityViewPager : AppCompatActivity() {

    internal lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private var myActionBar: ActionBar? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_viewpager)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        myActionBar = supportActionBar
        myActionBar!!.setDisplayHomeAsUpEnabled(true)

        tabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab1Heading))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab2Heading))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab3Heading))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        viewPager = findViewById<View>(R.id.pager) as ViewPager
        val adapter = PagerAdapter(supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter

        customizeViewPager()
        //Using 3rd party library here, toxicbakery. List of transformers in
        //https://github.com/ToxicBakery/ViewPagerTransforms/tree/master/library/src/main/java/com/ToxicBakery/viewpager/transforms
        viewPager.setPageTransformer(true, CubeOutTransformer())


        val tab = tabLayout.getTabAt(1)
        tab!!.select()

        viewPager.currentItem = 1
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}

        })

    }

    private fun customizeViewPager() {
        viewPager.setPageTransformer(false) { view, position ->
            val np = Math.abs(Math.abs(position) - 1)
            view.scaleX = np / 2 + 0.5f
            view.scaleY = np / 2 + 0.5f
        }

    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }


}
