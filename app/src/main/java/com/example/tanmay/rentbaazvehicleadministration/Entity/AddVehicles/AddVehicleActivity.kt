package com.example.tanmay.rentbaazvehicleadministration.Entity.AddVehicles

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.VehicleModel
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.bookingModel
import com.example.tanmay.rentbaazvehicleadministration.R
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_vehicle.*
import java.sql.Timestamp
import java.util.Date

class AddVehicleActivity : AppCompatActivity() {

    lateinit var vehicleCollectionReference : CollectionReference
    lateinit var userDocumentReference: DocumentReference
    lateinit var pref: SharedPreferences
    var documentId: String = ""
    var fullName = ""
    var organizationName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)
        supportActionBar!!.title = Html.fromHtml("<font color=\"#a9a9a9\">Add Vehicle</font>")

        pref = this.getSharedPreferences("user_detail", 0)

        vehicleCollectionReference = FirebaseFirestore.getInstance().collection("vehicle")

        userDocumentReference = FirebaseFirestore.getInstance()
                .document("users/" + pref.getString("phone_number", "0"))
        userDocumentReference.get().addOnSuccessListener {
            fullName = it.get("first_name").toString() + it.get("last_name").toString()
            organizationName = it.get("organization").toString()
        }
    }

    override fun onStart() {
        super.onStart()
        button_submit.setOnClickListener {
            documentId=System.currentTimeMillis().toString()
            val addVehicleAvailableModel = VehicleModel(
                    fullName,
                    vehicle_name.text.toString(),
                    vehicle_number.text.toString(),
                    organizationName,
                    "https://firebasestorage.googleapis.com/v0/b/rentbaaz-administration.appspot.com/o/Screenshot%202018-10-04%20at%2012.02.54%20PM%20(1).png?alt=media&token=821c1bf5-faa2-4590-8a71-906f53cd0067",
                    weekday_cost.text.toString(),
                    weekend_cost.text.toString(), mutableListOf(bookingModel("979004821", Date(0), Date(0))),documentId)

            vehicleCollectionReference.document(documentId).set(addVehicleAvailableModel).addOnSuccessListener {
                Toast.makeText(this@AddVehicleActivity,"Added to database",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
