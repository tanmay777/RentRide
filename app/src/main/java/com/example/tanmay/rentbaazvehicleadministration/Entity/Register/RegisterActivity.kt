package com.example.tanmay.rentbaazvehicleadministration.Entity.Register

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.HomeActivity
import com.example.tanmay.rentbaazvehicleadministration.R
import kotlinx.android.synthetic.main.activity_register.*
import com.example.tanmay.rentbaazvehicleadministration.Entity.Register.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    lateinit var userDocumentReference: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar!!.title= Html.fromHtml("<font color=\"#a9a9a9\">Register</font>")
        userDocumentReference = FirebaseFirestore.getInstance().collection("users")
    }

    override fun onStart() {
        super.onStart()
        completed_button.setOnClickListener {
            val user = User(first_name.text.toString(), last_name.text.toString(), intent.getStringExtra("phoneNum"), organization_name.text.toString(), address.text.toString())
            try {
                userDocumentReference.document(intent.getStringExtra("phoneNum")).set(user).addOnSuccessListener {
                    void:Void?->Toast.makeText(this, "Successfully uploaded to the database", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,HomeActivity::class.java))
                }.addOnFailureListener { exception: java.lang.Exception ->
                    Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }

        }
    }
}
