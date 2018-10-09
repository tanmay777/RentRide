package com.example.tanmay.rentbaazvehicleadministration.Entity.OnRent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.example.tanmay.rentbaazvehicleadministration.R
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_on_rent.*

class OnRentActivity : AppCompatActivity() {

    var hoursFlag:Boolean=true
    var extendTimeViewVisibile:Boolean=false
    lateinit var itemId:String
    lateinit var onRentVehicleDocumentReference: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_rent)

        itemId=intent.getStringExtra("item_id")
        Log.v("Item ID",itemId)
        onRentVehicleDocumentReference= FirebaseFirestore.getInstance().collection("rented_vehicle").document(itemId)
        onRentVehicleDocumentReference.get().addOnSuccessListener {
            bike_name.text=it.get("vehicle_name").toString()
            Glide.with(this).load(it.get("vehicle_image_url")).into(bike_img)
            bike_number.text=it.get("vehicle_number").toString()
            bike_organization.text=it.get("vendor_organization").toString()
            cost_weekday.text=it.get("weekday_cost").toString()
            cost_weekend.text=it.get("weekend_cost").toString()
        }
    }

    override fun onStart() {
        super.onStart()
        extend_time.setOnClickListener {
            if(extendTimeViewVisibile) {
                extend_time_minus_sign.visibility = View.GONE
                extend_time_hours.visibility = View.GONE
                extend_time_colon.visibility = View.GONE
                extend_time_minutes.visibility = View.GONE
                extend_time_plus_sign.visibility = View.GONE
                extend_time_hours_text.visibility = View.GONE
                extendTimeViewVisibile=false
            }
            else {
                extend_time_minus_sign.visibility = View.VISIBLE
                extend_time_hours.visibility = View.VISIBLE
                extend_time_colon.visibility = View.VISIBLE
                extend_time_minutes.visibility = View.VISIBLE
                extend_time_plus_sign.visibility = View.VISIBLE
                extend_time_hours_text.visibility = View.VISIBLE
                extendTimeViewVisibile=true
            }
        }


        extend_time_plus_sign.setOnClickListener {
            if(hoursFlag) {
                if((extend_time_hours.text.toString().toInt() + 1)>=24){
                    extend_time_hours.text ="0"
                }
                extend_time_hours.text = (extend_time_hours.text.toString().toInt() + 1).toString()
            }
            else {
                if((extend_time_minutes.text.toString().toInt() + 15)>=60)
                {
                    if((extend_time_hours.text.toString().toInt() + 1)>=24){
                        extend_time_hours.text ="0"
                    }
                    else
                        extend_time_hours.text = (extend_time_hours.text.toString().toInt() + 1).toString()
                    extend_time_minutes.text = "0"
                }
                else
                    extend_time_minutes.text = (extend_time_minutes.text.toString().toInt() + 15).toString()
            }
        }

        extend_time_minus_sign.setOnClickListener {
            if(hoursFlag) {
                if ((extend_time_hours.text.toString().toInt() - 1) >= 0)
                    extend_time_hours.text = (extend_time_hours.text.toString().toInt() - 1).toString()
            }
            else {
                if ((extend_time_minutes.text.toString().toInt() - 15) < 0 && ((extend_time_hours.text.toString().toInt() - 1) >= 0))
                {

                    extend_time_hours.text = (extend_time_hours.text.toString().toInt() - 1).toString()
                    extend_time_minutes.text = "45"
                }
                else if((extend_time_minutes.text.toString().toInt() - 15) < 0 && ((extend_time_hours.text.toString().toInt() - 1) < 0))
                {
                    extend_time_minutes.text = "0"
                }
                else {
                    extend_time_minutes.text = (extend_time_minutes.text.toString().toInt() - 15).toString()
                }
            }
        }

        extend_time_hours.setOnClickListener {
            hoursFlag=true
        }
        extend_time_minutes.setOnClickListener {
            hoursFlag=false
        }
    }
}

//TODO: Add prebookings in the xml file