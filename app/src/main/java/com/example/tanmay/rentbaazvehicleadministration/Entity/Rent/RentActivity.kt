package com.example.tanmay.rentbaazvehicleadministration.Entity.Rent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import com.bumptech.glide.Glide
import com.example.tanmay.rentbaazvehicleadministration.R
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_rent.*

class RentActivity : AppCompatActivity() {

    lateinit var itemId:String
    lateinit var availableVehicleDocumentReference:DocumentReference
    var hoursFlag:Boolean=true
    var extendTimeViewVisibile:Boolean=false

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

        /*extend_time_plus_sign.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->{
                        if(hoursFlag) {
                            var i:Int=extend_time_hours.text.toString().toInt()
                            for(i in i..24) {
                                Thread.sleep(500)
                                if ((extend_time_hours.text.toString().toInt() + 1) >= 24) {
                                    extend_time_hours.text = "00"
                                }
                                extend_time_hours.text = (extend_time_hours.text.toString().toInt() + 1).toString()
                            }
                        }
                        else {
                            var i:Int=extend_time_minutes.text.toString().toInt()
                            for(i in i..60) {
                                Thread.sleep(500)
                                if ((extend_time_minutes.text.toString().toInt() + 1) >= 60) {
                                    extend_time_hours.text = (extend_time_hours.text.toString().toInt() + 1).toString()
                                    extend_time_minutes.text = "00"
                                } else
                                    extend_time_minutes.text = (extend_time_minutes.text.toString().toInt() + 1).toString()
                            }
                        }
                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })*/

        //TODO: We have to implement long press on plus and minus signs so users doesn't has to continously tap to increase time one by one


        extend_time_plus_sign.setOnClickListener {
            if(hoursFlag) {
                if((extend_time_hours.text.toString().toInt() + 1)>24){
                    extend_time_hours.text ="00"
                }
                extend_time_hours.text = (extend_time_hours.text.toString().toInt() + 1).toString()
            }
            else {
                if((extend_time_minutes.text.toString().toInt() + 1)>=60)
                {
                    if((extend_time_hours.text.toString().toInt() + 1)>=24){
                        extend_time_hours.text ="00"
                    }
                    else
                        extend_time_hours.text = (extend_time_hours.text.toString().toInt() + 1).toString()
                    extend_time_minutes.text = "00"
                }
                else
                    extend_time_minutes.text = (extend_time_minutes.text.toString().toInt() + 1).toString()
            }
        }

        extend_time_minus_sign.setOnClickListener {
            if(hoursFlag) {
                if ((extend_time_hours.text.toString().toInt() - 1) >= 0)
                    extend_time_hours.text = (extend_time_hours.text.toString().toInt() - 1).toString()
            }
            else {
                if ((extend_time_minutes.text.toString().toInt() - 1) >= 0)
                    extend_time_minutes.text = (extend_time_minutes.text.toString().toInt() - 1).toString()
            }
        }

        extend_time_hours.setOnClickListener {
            hoursFlag=true
        }
        extend_time_minutes.setOnClickListener {
            hoursFlag=false
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
