package com.example.tanmay.rentbaazvehicleadministration.Entity.Home

import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.text.Html
import android.view.Menu
import android.view.MenuItem

import com.example.tanmay.rentbaazvehicleadministration.R
import kotlinx.android.synthetic.main.activity_home.*

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
        /*
        val tab = LayoutInflater.from(this).inflate(R.layout.nav_tab,null) as CoordinatorLayout
        tab.nav_label.text="All Vehicles"
        tab.nav_icon.setImageResource(R.drawable.ic_car)
        tab.nav_icon.alpha=0.5f
         */
        tab_layout.addTab(tab_layout.newTab().setText("All Vehicles").setIcon(R.drawable.ic_car))
        val tab2=tab_layout.newTab().setText("Available").setIcon(R.drawable.ic_car)
        tab2.icon?.alpha=128
        tab_layout.addTab(tab2)
        val tab3=tab_layout.newTab().setText("On Rent").setIcon(R.drawable.ic_car)
        tab3.icon?.alpha=128
        tab_layout.addTab(tab3)

        val adapter=HomeTabPagerAdapter(supportFragmentManager,tab_layout.tabCount)

        pager.adapter=adapter

        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))

        tab_layout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem=tab.position
                tab.icon?.alpha=255
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.icon?.alpha=128
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
