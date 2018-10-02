package com.example.tanmay.rentbaazvehicleadministration.Entity.Rent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import com.example.tanmay.rentbaazvehicleadministration.R

class RentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent)
        supportActionBar!!.title= Html.fromHtml("<font color=\"#a9a9a9\">Rent Out</font>")
    }
}
