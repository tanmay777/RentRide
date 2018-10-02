package com.example.tanmay.rentbaazvehicleadministration.Entity.Home

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.widget.Toast
import com.example.tanmay.rentbaazvehicleadministration.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_all_vehicles-> {
                message.setText(R.string.title_all_vehicles)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_available -> {
                message.setText(R.string.title_available)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_on_rent-> {
                message.setText(R.string.title_on_rent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    var x:String="abc";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar!!.title= Html.fromHtml("<font color=\"#a9a9a9\">Home</font>")
        //mAuth = FirebaseAuth.getInstance()
        //val currentUser = mAuth.currentUser
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}

//TODO: Add perimission mode for firestore database