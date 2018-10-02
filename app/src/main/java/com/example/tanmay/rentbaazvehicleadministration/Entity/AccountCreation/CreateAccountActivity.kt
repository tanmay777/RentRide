package com.example.tanmay.rentbaazvehicleadministration.Entity.AccountCreation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.widget.Toast
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.HomeActivity
import com.example.tanmay.rentbaazvehicleadministration.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        supportActionBar!!.title= Html.fromHtml("<font color=\"#a9a9a9\">Create Account</font>")
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        if(currentUser!=null) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        send_otp_button.setOnClickListener{
            if(validatePhoneNumber(login_phone_number.text.toString())) {
                var intent: Intent = Intent(this, VerifyAccountActivity::class.java)
                intent.putExtra("phone_number", "+91" + login_phone_number.text.toString())
                startActivity(intent)
            }
        }
    }

    private fun validatePhoneNumber(phoneNumber:String): Boolean {
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(applicationContext,"Enter number first",Toast.LENGTH_SHORT).show()
            return false
        }
        //TODO: Have better validation of phone algo and also use text layout to show error
        return true
    }
}
