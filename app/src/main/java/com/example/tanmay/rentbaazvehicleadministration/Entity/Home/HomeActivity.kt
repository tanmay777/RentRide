package com.example.tanmay.rentbaazvehicleadministration.Entity.Home

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.example.tanmay.rentbaazvehicleadministration.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_all_vehicles-> {
                message.setText(R.string.title_all_vehicles)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_available -> {
                message.setText(R.string.title_manage_sessions)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_on_rent-> {
                message.setText(R.string.title_manage_sessions)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
