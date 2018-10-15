package com.example.tanmay.rentbaazvehicleadministration.Entity.AccountCreation

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.HomeActivity
import com.example.tanmay.rentbaazvehicleadministration.Entity.Register.RegisterActivity
import com.example.tanmay.rentbaazvehicleadministration.R
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_verify_account.*
import java.util.concurrent.TimeUnit

class VerifyAccountActivity : AppCompatActivity() {

    private val KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress"

    lateinit var phoneNum:String
    
    lateinit var mAuth: FirebaseAuth
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    //mCallbacks are callbacks for the result of verifying the phone number.

    private var mResendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var mVerificationId: String? = "default"
    private var mVerificationInProgress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_account)
        //Start Initialize Auth
        mAuth = FirebaseAuth.getInstance()
        phoneNum=intent.getStringExtra("phone_number")
        //End Initialize Auth
        otp_text.text=otp_text.text.toString()+phoneNum
        supportActionBar!!.title= Html.fromHtml("<font color=\"#a9a9a9\">Verify Account</font>")

        initUI()
        initCallback()

    }

    override fun onStart() {
        super.onStart()
        startPhoneNumberVerification(phoneNum)

    }
    //[End on_start_check_user]

    override fun onResume() {
        super.onResume()
        //[Start_Exclude]
        if (mVerificationInProgress) {
            startPhoneNumberVerification(phoneNum)
        }
        //[End_Exclude]
    }

    private fun initUI() {

        verify_button.setOnClickListener {
            val code = getVerficationCode()
            if (TextUtils.isEmpty(code)) {
                showLog("Phone number cannot be empty")
                return@setOnClickListener
            }
            verifyPhoneNumberWithCode(mVerificationId!!.toString(), code)
        }
        resend_text.setOnClickListener {
            resendVerificationCode(phoneNum, mResendToken)
        }
    }

    private fun initCallback() {
        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                showLog("onVerificationCompleted:" + credential)
                // [START_EXCLUDE silent]
                mVerificationInProgress = false
                //[END_EXCLUDE]
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                // [START_EXCLUDE silent]
                showLog("onVerificationFailed")
                mVerificationInProgress = false
                //[END_EXCLUDE]

                if (e is FirebaseAuthInvalidCredentialsException) {
                    //Invalid Request
                    showLog("Invalid Phone Number")
                } else if (e is FirebaseTooManyRequestsException) {
                    //The SMS quote for the project has exceeded
                    showLog("Quota Exceeded")
                }
                showLog("Verification Failed")
            }

            override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken?) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                showLog("Code Sent:" + verificationId)
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId
                mResendToken = token
            }

            override fun onCodeAutoRetrievalTimeOut(verificationId: String?) {
                super.onCodeAutoRetrievalTimeOut(verificationId)
                showLog("Code TimeOut")
            }
        }
        // [END phone_auth_callbacks]
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        showLog("Sign in with Credentials: successful")
                        //val user = task.result.user
                        val pref = this.getSharedPreferences("user_detail", 0)
                        val editor = pref!!.edit()
                        editor.putString("phone_number", phoneNum)
                        editor.apply()

                        FirebaseFirestore.getInstance().collection("users").document(phoneNum).get().addOnSuccessListener {
                            if (it.exists()) {
                                startActivity(Intent(this, HomeActivity::class.java))
                            }
                            else{
                                val intent:Intent=Intent(this, RegisterActivity::class.java)
                                Toast.makeText(applicationContext,phoneNum,Toast.LENGTH_SHORT).show()
                                intent.putExtra("phoneNum",phoneNum)
                                startActivity(intent)
                            }
                        }

                    } else {
                        // Sign in failed, display a message and update the UI
                        showLog("Sign in with Credentials: failed")
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            showLog("Invalid Code.")
                        }
                        showLog("Sign in Failed")
                    }

                }
    }

    private fun resendVerificationCode(phoneNumber: String, token: PhoneAuthProvider.ForceResendingToken?) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks,
                token
        )
    }

    private fun getVerficationCode(): String {
        val code=ver_code_one.text.toString()+ver_code_two.text.toString()+ver_code_three.text.toString()+ver_code_four.text.toString()+ver_code_five.text.toString()+ver_code_six.text.toString()
        return code
    }

    private fun verifyPhoneNumberWithCode(verificationId:String, code:String){
        //Start verify_with_code
        val credential=PhoneAuthProvider.getCredential(verificationId,code)
        //end verify with code
        signInWithPhoneAuthCredential(credential)
    }
    private fun startPhoneNumberVerification(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks)
        mVerificationInProgress = true
    }




    companion object {
        const val RC_SIGN_IN = 123
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            when {
                resultCode == Activity.RESULT_OK -> {
                    showLog("Sign In Successful")
                    return
                }
                response == null -> {
                    showLog("Sign In Cancelled")
                    return
                }
                response.error?.errorCode == ErrorCodes.NO_NETWORK -> {
                    showLog("No Network")
                    return
                }

                else -> {
                    showLog("Unknown Response")
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putBoolean(KEY_VERIFY_IN_PROGRESS,mVerificationInProgress)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mVerificationInProgress=savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS)
    }
    
    fun showLog(message:String){
        Log.e("VerifyAccountActivity",message)
        //Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
