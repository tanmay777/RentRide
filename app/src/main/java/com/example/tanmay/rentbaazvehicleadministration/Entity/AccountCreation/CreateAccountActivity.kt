package com.example.tanmay.rentbaazvehicleadministration.Entity.AccountCreation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Toast
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.HomeActivity
import com.example.tanmay.rentbaazvehicleadministration.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_account.*
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.FirebaseFirestore


class CreateAccountActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        supportActionBar!!.title = Html.fromHtml("<font color=\"#a9a9a9\">Create Account</font>")
        val firestore = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build()
        firestore.firestoreSettings = settings

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        login_phone_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length != 10) {
                    layout_phone_number.error = "It should be 10 digits only"
                }
                else
                    layout_phone_number.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        send_otp_button.setOnClickListener {
            if(validate()) {
                val intent: Intent = Intent(this@CreateAccountActivity, VerifyAccountActivity::class.java)
                intent.putExtra("phone_number", "+91" + login_phone_number.text.toString())
                startActivity(intent)
            }
        }
    }

    fun validate():Boolean{
        var flag=true
        if(login_phone_number.text.toString().length!=10){
            flag=false
            layout_phone_number.error="It should be 10 digits only"
        }
        return flag
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
