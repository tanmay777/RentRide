package com.example.tanmay.rentbaazvehicleadministration.Entity.Home

import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.TabLayout
import android.text.Html
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout

import com.example.tanmay.rentbaazvehicleadministration.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_tab.view.*

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)
        toolbar.title = Html.fromHtml("<font color=\"#a9a9a9\">Home</font>")


        configureTabLayout()
    }

    private fun configureTabLayout() {
        //This creates four tabs and assigns text to them

        tab_layout.addTab(tab_layout.newTab())
        tab_layout.addTab(tab_layout.newTab())
        tab_layout.addTab(tab_layout.newTab())

        val tab = LayoutInflater.from(this).inflate(R.layout.nav_tab, null) as LinearLayout
        tab.nav_label.text = "All"
        tab.nav_label.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_car,0,0,0)
        tab.nav_label.scaleY=-1f;
        tab_layout.getTabAt(0)!!.setCustomView(tab)

        val tab1 = LayoutInflater.from(this).inflate(R.layout.nav_tab, null) as LinearLayout
        tab1.nav_label.text = "Available"
        tab1.nav_label.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_car,0,0,0)
        tab1.nav_label.scaleY=-1f;
        //tab1.nav_label.alpha=.5f
        tab_layout.getTabAt(1)!!.setCustomView(tab1)
        tab_layout.getTabAt(1)!!.customView?.alpha=.5f

        val tab2 = LayoutInflater.from(this).inflate(R.layout.nav_tab, null) as LinearLayout
        tab2.nav_label.text = "On Rent"
        tab2.nav_label.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_car,0,0,0)
        //tab2.nav_label.alpha=.5f
        tab2.nav_label.scaleY=-1f;
        tab_layout.getTabAt(2)!!.setCustomView(tab2)
        tab_layout.getTabAt(2)!!.customView?.alpha=.5f

        /*tab_layout.addTab(tab_layout.newTab().setText("All Vehicles").setIcon(R.drawable.ic_car))
        val tab2=tab_layout.newTab().setText("Available").setIcon(R.drawable.ic_car)
        tab2.icon?.alpha=128
        tab_layout.addTab(tab2)
        val tab3=tab_layout.newTab().setText("On Rent").setIcon(R.drawable.ic_car)
        tab3.icon?.alpha=128
        tab_layout.addTab(tab3)
       */

        val adapter = HomeTabPagerAdapter(supportFragmentManager, tab_layout.tabCount)

        pager.adapter = adapter

        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
                tab.customView?.alpha=1.0f
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.alpha=0.5f
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
// Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home_activityone, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

//TODO: Add perimission mode for firestore database
}
