package com.stayfit.app

import android.content.Intent
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.TextView

class AccountActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AccountFragment.OnChangeGoalListener {

    private var navigationView: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_activity)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, AccountFragment())
                .commit()

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

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
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

    override fun onChangeGoalClicked() {
        val myIntent = Intent(this, SetGoalActivity::class.java)
        startActivity(myIntent)
    }

}

