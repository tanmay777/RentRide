package com.example.tanmay.rentbaazvehicleadministration.Entity.Rent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.tanmay.rentbaazvehicleadministration.R
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_rent.*

class RentActivity : AppCompatActivity() {

    lateinit var itemId:String
    lateinit var availableVehicleDocumentReference:DocumentReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent)
        supportActionBar!!.title= Html.fromHtml("<font color=\"#a9a9a9\">Rent Out</font>")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //TODO: Change color of up enabled button
    }

    override fun onStart() {
        super.onStart()
        itemId=intent.getStringExtra("item_id")
        Log.v("Item ID",itemId)
        availableVehicleDocumentReference=FirebaseFirestore.getInstance().collection("available_vehicle").document(itemId)
        availableVehicleDocumentReference.get().addOnSuccessListener {
            bike_name.text=it.get("vehicle_name").toString()
            Glide.with(this).load(it.get("vehicle_image_url")).into(bike_img)
            bike_number.text=it.get("vehicle_number").toString()
            bike_organization.text=it.get("vendor_organization").toString()
            cost_weekday.text=it.get("weekday_cost").toString()
            cost_weekend.text=it.get("weekend_cost").toString()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId) {
            android.R.id.home -> {
                this.finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
