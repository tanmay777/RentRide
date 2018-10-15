package com.example.tanmay.rentbaazvehicleadministration.Entity.Register

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.HomeActivity
import com.example.tanmay.rentbaazvehicleadministration.R
import kotlinx.android.synthetic.main.activity_register.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    lateinit var userCollectionReference: CollectionReference
    val TAG:String ="RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar!!.title = Html.fromHtml("<font color=\"#a9a9a9\">Register</font>")

        userCollectionReference = FirebaseFirestore.getInstance().collection("users")
        userCollectionReference.document(intent.getStringExtra("phoneNum")).get().addOnSuccessListener {
            if(it.exists()){
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }


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

        organization_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    layout_organization_name.error = "Organization Name cannot be Empty"
                } else
                    layout_organization_name.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        address.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    layout_address.error = "Address cannot be Empty"
                } else
                    layout_address.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        //TODO: This activity will be skipped if the user is already registered
    }

    override fun onStart() {
        super.onStart()
        completed_button.setOnClickListener {
            if (layout_first_name.error == null &&
                    layout_last_name.error == null
                    && layout_organization_name.error == null
                    && layout_address.error == null) {
                val user = User(first_name.text.toString(), last_name.text.toString(), intent.getStringExtra("phoneNum"), organization_name.text.toString(), address.text.toString())
                try {
                    userCollectionReference.document(intent.getStringExtra("phoneNum")).set(user).addOnSuccessListener { void: Void? ->
                        Log.v(TAG,"Successfully uploaded to DB")
                        startActivity(Intent(this, HomeActivity::class.java))
                    }.addOnFailureListener { exception: java.lang.Exception ->
                        Log.e(TAG,exception.toString())
                    }
                } catch (e: Exception) {
                    Log.e(TAG,e.toString())
                }
            }

        }
    }

    fun validate():Boolean{
        var flag=true
        if(first_name.text.toString().isEmpty()){
            layout_first_name.error="First Name cannot be empty"
            flag=false
        }
        if(last_name.text.toString().isEmpty()){
            layout_last_name.error="Last Name cannot be empty"
            flag=false
        }
        if(organization_name.text.toString().isEmpty()){
            layout_organization_name.error="Organization Name cannot be empty"
            flag=false
        }
        if(address.text.toString().isEmpty()){
            layout_address.error="Address cannot be empty"
            flag=false
        }
        return flag
    }
}