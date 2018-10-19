package com.example.tanmay.rentbaazvehicleadministration.Entity.OnRent

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.HomeActivity
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.VehicleModel
import com.example.tanmay.rentbaazvehicleadministration.R
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_on_rent.*
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.*


class OnRentActivity : AppCompatActivity() {

    var hoursFlag: Boolean = true
    var extendTimeViewVisibile: Boolean = false
    lateinit var itemId: String
    lateinit var renteePhoneNum: String
    lateinit var onRentVehicleDocumentReference: DocumentReference
    var vehicle: VehicleModel? = null
    var bookingIndex: Int = 0
    var cost=0
    var weekday_cost=0
    var weekend_cost=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_rent)
        supportActionBar!!.title = Html.fromHtml("<font color=\"#a9a9a9\">On Rent</font>")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (intent.getStringExtra("item_id") == null) {
            renteePhoneNum = intent.getStringExtra("phone_number")
            FirebaseFirestore.getInstance().collection("rentee_details").document(renteePhoneNum).get().addOnSuccessListener {
                itemId = it.get("vehicle_id").toString()
                vehicleUIInitiate()
            }
        } else {
            itemId = intent.getStringExtra("item_id")
            vehicleUIInitiate()
        }

        extend_time_done.setOnClickListener {
            val cal = Calendar.getInstance() // creates calendar
            cal.time = vehicle!!.booking[bookingIndex].drop_details// sets calendar time/date
            cal.add(Calendar.HOUR_OF_DAY, extend_time_hours.text.toString().toInt()) // adds one hour
            vehicle!!.booking[bookingIndex].drop_details = cal.time

            cal.time = vehicle!!.booking[bookingIndex].drop_details// sets calendar time/date
            cal.add(Calendar.MINUTE, extend_time_minutes.text.toString().toInt()) // adds one hour
            vehicle!!.booking[bookingIndex].drop_details = cal.time

            FirebaseFirestore.getInstance().collection("vehicle").document(itemId).set(vehicle!!).addOnSuccessListener {
                Toast.makeText(applicationContext, "Updated", Toast.LENGTH_SHORT).show()
                layout_return_time.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
        }
    }

    fun vehicleUIInitiate() {

        progressBar.visibility = View.VISIBLE
        onRentVehicleDocumentReference = FirebaseFirestore.getInstance().collection("vehicle").document(itemId)
        onRentVehicleDocumentReference.get().addOnSuccessListener { it: DocumentSnapshot ->
            vehicle = it.toObject(VehicleModel::class.java)
            bike_name.text = vehicle?.vehicle_name
            Glide.with(this).load(vehicle?.vehicle_image_url).into(bike_img)
            bike_number.text = vehicle?.vehicle_number
            bike_organization.text = vehicle?.vendor_organization
            weekday_cost=vehicle?.weekday_cost.toString().toInt()
            weekend_cost=vehicle?.weekend_cost.toString().toInt()
            cost_weekday.text = "Rs "+vehicle?.weekday_cost+"\\hr"
            cost_weekend.text = "Rs "+vehicle?.weekend_cost+"\\hr"
            Log.v("Vehicle_model", vehicle.toString())
            if (intent.getStringExtra("item_id") != null) {
                for (bookingModel in vehicle!!.booking) {
                    if ((bookingModel.pickup_details.compareTo(Date()) < 0) &&
                            (bookingModel.drop_details.compareTo(Date()) > 0)) {
                        bookingIndex++
                        val myFormat = "dd MMM" // mention the format you need
                        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
                        layout_pickup_date.text = simpleDateFormat.format(bookingModel.pickup_details)
                        layout_pickup_time.text = SimpleDateFormat("HH:mm").format(bookingModel.pickup_details)
                        layout_return_date.text = simpleDateFormat.format(bookingModel.drop_details)
                        layout_return_time.text = SimpleDateFormat("HH:mm").format(bookingModel.drop_details)
                        FirebaseFirestore.getInstance().collection("rentee_details").document(bookingModel.phone_num).get().addOnSuccessListener {
                            val fullName = it.get("first_name").toString() + " " + it.get("last_name").toString()
                            layout_first_name.text = fullName
                            layout_registration_number.text = it.get("registration_number").toString()
                            layout_contact_number.text = it.get("phone_number").toString()
                        }
                    }
                }
            } else {
                layout_pickup_date.text = intent.getStringExtra("pick_up_date")
                layout_pickup_time.text = intent.getStringExtra("pick_up_time")
                layout_return_date.text = intent.getStringExtra("drop_date")
                layout_return_time.text = intent.getStringExtra("drop_time")
                FirebaseFirestore.getInstance().collection("rentee_details").document(renteePhoneNum).get().addOnSuccessListener {
                    val fullName = it.get("first_name").toString() + " " + it.get("last_name").toString()
                    layout_first_name.text = fullName
                    layout_registration_number.text = it.get("registration_number").toString()
                    layout_contact_number.text = it.get("phone_number").toString()
                }
            }
            progressBar.visibility = View.GONE
        }


    }


    override fun onStart() {
        super.onStart()
        end_session_button.setOnClickListener {
            onRentVehicleDocumentReference.get().addOnSuccessListener { it: DocumentSnapshot ->
                vehicle = it.toObject(VehicleModel::class.java)
                for (bookingModel in vehicle!!.booking) {
                    if ((bookingModel.pickup_details.compareTo(Date()) < 0) &&
                            (bookingModel.drop_details.compareTo(Date()) > 0)) {
                        vehicle!!.booking.remove(bookingModel)
                        onRentVehicleDocumentReference.set(vehicle!!)
                        val minsDiff = abs(bookingModel.drop_details.minutes - bookingModel.pickup_details.minutes)
                        val hoursDiff = abs(bookingModel.drop_details.hours - bookingModel.pickup_details.hours)
                        val daysDiff=abs(bookingModel.drop_details.date- bookingModel.pickup_details.date)
                        Log.v("OnRentActivity ",bookingModel.drop_details.minutes.toString() )
                        Log.v("OnRentActivity ",hoursDiff.toString())
                        if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                                || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                            Log.v("OnRentActivity","Weekend")
                            cost = daysDiff*24*weekend_cost+hoursDiff * weekend_cost + (minsDiff * weekend_cost/ 60)
                        }
                        else {
                            cost = daysDiff*24*weekday_cost+hoursDiff * weekday_cost + (minsDiff * weekday_cost / 60)
                            Log.v("OnRentActivity","WeekDay")
                        }
                        Toast.makeText(applicationContext,"Your Total Bill is "+cost.toString(),Toast.LENGTH_SHORT).show()
                        FirebaseFirestore.getInstance().collection("rentee_details").document(bookingModel.phone_num).update("bill", cost.toString())
                    }
                    startActivity(Intent(this@OnRentActivity, HomeActivity::class.java))
                }
            }
        }


        extend_time.setOnClickListener {
            if (extendTimeViewVisibile) {
                extend_time_minus_sign.visibility = View.GONE
                extend_time_hours.visibility = View.GONE
                extend_time_colon.visibility = View.GONE
                extend_time_minutes.visibility = View.GONE
                extend_time_plus_sign.visibility = View.GONE
                extend_time_hours_text.visibility = View.GONE
                extend_time_done.visibility = View.GONE
                extendTimeViewVisibile = false
            } else {
                extend_time_minus_sign.visibility = View.VISIBLE
                extend_time_hours.visibility = View.VISIBLE
                extend_time_colon.visibility = View.VISIBLE
                extend_time_minutes.visibility = View.VISIBLE
                extend_time_plus_sign.visibility = View.VISIBLE
                extend_time_hours_text.visibility = View.VISIBLE
                extend_time_done.visibility = View.VISIBLE
                extendTimeViewVisibile = true
            }
        }

        //TODO: Solve Done Button flickering

        extend_time_plus_sign.setOnClickListener {
            if (hoursFlag) {
                if ((extend_time_hours.text.toString().toInt() + 1) >= 24) {
                    extend_time_hours.text = "0"
                }
                extend_time_hours.text = (extend_time_hours.text.toString().toInt() + 1).toString()
            } else {
                if ((extend_time_minutes.text.toString().toInt() + 15) >= 60) {
                    if ((extend_time_hours.text.toString().toInt() + 1) >= 24) {
                        extend_time_hours.text = "0"
                    } else
                        extend_time_hours.text = (extend_time_hours.text.toString().toInt() + 1).toString()
                    extend_time_minutes.text = "0"
                } else
                    extend_time_minutes.text = (extend_time_minutes.text.toString().toInt() + 15).toString()
            }
        }

        extend_time_minus_sign.setOnClickListener {
            if (hoursFlag) {
                if ((extend_time_hours.text.toString().toInt() - 1) >= 0)
                    extend_time_hours.text = (extend_time_hours.text.toString().toInt() - 1).toString()
            } else {
                if ((extend_time_minutes.text.toString().toInt() - 15) < 0 && ((extend_time_hours.text.toString().toInt() - 1) >= 0)) {
                    extend_time_hours.text = (extend_time_hours.text.toString().toInt() - 1).toString()
                    extend_time_minutes.text = "45"
                } else if ((extend_time_minutes.text.toString().toInt() - 15) < 0 && ((extend_time_hours.text.toString().toInt() - 1) < 0)) {
                    extend_time_minutes.text = "0"
                } else {
                    extend_time_minutes.text = (extend_time_minutes.text.toString().toInt() - 15).toString()
                }
            }
        }

        extend_time_hours.setOnClickListener {
            hoursFlag = true
        }
        extend_time_minutes.setOnClickListener {
            hoursFlag = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> {
                this.finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

//TODO: Add prebookings in the xml file