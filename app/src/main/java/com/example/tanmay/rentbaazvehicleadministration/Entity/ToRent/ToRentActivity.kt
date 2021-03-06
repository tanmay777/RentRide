package com.example.tanmay.rentbaazvehicleadministration.Entity.ToRent

import android.app.ActionBar
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.HomeActivity
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.VehicleModel
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.bookingModel
import com.example.tanmay.rentbaazvehicleadministration.R
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_to_rent.*
import java.text.SimpleDateFormat
import java.util.*


class ToRentActivity : AppCompatActivity() {

    lateinit var itemId: String
    var rootRef: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var pickUpDateTime: Date
    lateinit var returnDateTime: Date
    var vehicle: VehicleModel? = null
    lateinit var vehicleUpdated: VehicleModel
    var cal = Calendar.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_rent)
        prebook_recycler_view.layoutManager = LinearLayoutManager(this)
        supportActionBar!!.title = Html.fromHtml("<font color=\"#a9a9a9\">On Rent</font>")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        expand_text.setOnClickListener {
            if (prebook_recycler_view.layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                prebook_recycler_view.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                prebook_recycler_view.requestLayout()
                // Call requestLayout() for redraw your TextView when your TextView is already drawn (laid out) (eg: you update TextView width when click a Button).
                expand_text.text = "Collapse"
            } else {
                var pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 245f, getResources().getDisplayMetrics());
                prebook_recycler_view.layoutParams.height = pixels.toInt()
                prebook_recycler_view.requestLayout()
                expand_text.text = "Expand"
            }
        }

        edit_text_pick_up_date.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    layout_pickup_date.error = "Pick Up Date cannot be Empty"
                } else
                    layout_pickup_date.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        edit_text_pick_up_time.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    layout_pickup_time.error = "Pick Up Time cannot be Empty"
                } else
                    layout_pickup_time.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        edit_text_return_date.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    layout_return_date.error = "Return Date cannot be Empty"
                } else
                    layout_return_date.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        edit_text_return_time.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    layout_return_time.error = "Return Time cannot be Empty"
                } else
                    layout_return_time.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        registration_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    layout_registration_number.error = "Registration Number cannot be Empty"
                } else
                    layout_registration_number.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        first_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    layout_first_name.error = "First Name cannot be Empty"
                } else
                    layout_first_name.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        last_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    layout_last_name.error = "Last Name cannot be Empty"
                } else
                    layout_last_name.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    layout_contact_number.error = "Contact Number cannot be Empty"
                } else
                    layout_contact_number.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        val pickUpDateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "dd/MM/yyyy" // mention the format you need
                val simpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
                edit_text_pick_up_date!!.text = Editable.Factory.getInstance().newEditable(simpleDateFormat.format(cal.getTime()))
                pickUpDateTime = cal.time
            }
        }

        val returnDateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "dd/MM/yyyy" // mention the format you need
                val simpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
                edit_text_return_date!!.text = Editable.Factory.getInstance().newEditable(simpleDateFormat.format(cal.getTime()))
                returnDateTime = cal.time
            }
        }

        edit_text_pick_up_date.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        DatePickerDialog(this@ToRentActivity,
                                pickUpDateSetListener,
                                // se DatePickerDialog to point to today's date when it loads up
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH),
                                cal.get(Calendar.DAY_OF_MONTH)).show()
                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        edit_text_return_date.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        DatePickerDialog(this@ToRentActivity,
                                returnDateSetListener,
                                // set DatePickerDialog to point to today's date when it loads up
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH),
                                cal.get(Calendar.DAY_OF_MONTH)).show()
                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        val pickUpTimeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            edit_text_pick_up_time.text = Editable.Factory.getInstance().newEditable(SimpleDateFormat("HH:mm").format(cal.time))
            pickUpDateTime = cal.time
        }

        val returnTimeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            edit_text_return_time.text = Editable.Factory.getInstance().newEditable(SimpleDateFormat("HH:mm").format(cal.time))
            returnDateTime = cal.time
        }

        edit_text_pick_up_time.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        TimePickerDialog(this@ToRentActivity, pickUpTimeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        edit_text_return_time.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        TimePickerDialog(this@ToRentActivity, returnTimeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

    }

    override fun onStart() {
        super.onStart()
        itemId = intent.getStringExtra("item_id")
        Log.v("Item ID", itemId)
        progressBar.visibility = View.VISIBLE
        val vehicleDocumentReference = FirebaseFirestore.getInstance().collection("vehicle").document(itemId)
        vehicleDocumentReference.get().addOnSuccessListener { it: DocumentSnapshot ->
            vehicle = it.toObject(VehicleModel::class.java)
            bike_name.text = it.get("vehicle_name").toString()
            Glide.with(this).load(it.get("vehicle_image_url")).into(bike_img)
            bike_number.text = it.get("vehicle_number").toString()
            bike_organization.text = it.get("vendor_organization").toString()
            cost_weekday.text = "Rs "+it.get("weekday_cost").toString()+"\\hr"
            cost_weekend.text = "Rs "+it.get("weekend_cost").toString()+"\\hr"
            prebook_recycler_view.adapter = PrebookingAdapter(vehicle!!.booking, this)
            if(vehicle!!.booking.isNotEmpty())
                vehicle!!.booking.removeAt(0)
            if (vehicle!!.booking.size > 3) {
                var pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 245f, getResources().getDisplayMetrics());
                prebook_recycler_view.layoutParams.height = pixels.toInt()
                prebook_recycler_view.requestLayout()
                expand_text.visibility = View.VISIBLE
            }
            if(vehicle!!.booking.isEmpty())
            {
                prebooking_text.text="No Pre-Booking"
            }
            progressBar.visibility = View.GONE
        }

        submit.setOnClickListener {
            var flag = true
            if (validate()) {
                if (pickUpDateTime.compareTo(returnDateTime) > 0) {
                    Toast.makeText(applicationContext, "Pick Up cannot be after Return of Vehicle", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                for (bookingModel in vehicle!!.booking) {
                    if (!(((bookingModel.pickup_details.compareTo(returnDateTime) > 0) &&
                                    (bookingModel.pickup_details.compareTo(pickUpDateTime) > 0)) ||
                            ((bookingModel.drop_details.compareTo(returnDateTime) < 0) &&
                                    (bookingModel.drop_details.compareTo(pickUpDateTime) < 0)))
                    ) {
                        flag = false
                    }
                }
                if (flag) {
                    vehicle?.booking?.add(bookingModel(number.text.toString(), Date(pickUpDateTime.time), Date(returnDateTime.time)))
                    rootRef.collection("vehicle").document(itemId).set(vehicle!!)

                    rootRef.collection("rentee_details").document(number.text.toString()).set(RenteeModel(first_name.text.toString(),
                            last_name.text.toString(),
                            number.text.toString(),
                            registration_number.text.toString(),itemId))

                    startActivity(Intent(this@ToRentActivity, HomeActivity::class.java))
                } else {
                    Toast.makeText(applicationContext, "Already Prebooked During this time", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun validate(): Boolean {
        var flag = true
        if (first_name.text.toString().isEmpty()) {
            layout_first_name.error = "First Name cannot be Empty"
            flag = false
        }
        if (last_name.text.toString().isEmpty()) {
            layout_last_name.error = "Last Name cannot be Empty"
            flag = false
        }
        if (number.text.toString().isEmpty()) {
            layout_contact_number.error = "Contact Number cannot be Empty"
            flag = false
        }
        if (registration_number.text.toString().isEmpty()) {
            layout_registration_number.error = "Registration Number cannot be Empty"
            flag = false
        }
        if (edit_text_pick_up_date.text.toString().isEmpty()) {
            layout_pickup_date.error = "Pick Up Date cannot be Empty"
            flag = false
        }
        if (edit_text_pick_up_time.text.toString().isEmpty()) {
            layout_pickup_time.error = "Pick Up Time cannot be Empty"
            flag = false
        }
        if (edit_text_return_date.text.toString().isEmpty()) {
            layout_return_date.error = "Return Date cannot be Empty"
            flag = false
        }
        if (edit_text_return_time.text.toString().isEmpty()) {
            layout_return_time.error = "Return Time cannot be Empty"
            flag = false
        }
        return flag
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
