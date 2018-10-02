package com.example.tanmay.rentbaazvehicleadministration.Entity.Login

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.Snackbar
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.HomeActivity
import com.example.tanmay.rentbaazvehicleadministration.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private val KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress"

    lateinit var mAuth: FirebaseAuth
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    //mCallbacks are callbacks for the result of verifying the phone number.

    private var mResendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var mVerificationId: String? = "default"
    private var mVerificationInProgress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Start Initialize Auth
        mAuth = FirebaseAuth.getInstance()
        //End Initialize Auth

        supportActionBar!!.title= Html.fromHtml("<font color=\"#a9a9a9\">Log In</font>")

        initUI()
        initCallback()

    }

    override fun onStart() {
        super.onStart()
        //Check if user is already signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if(currentUser!=null) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        //[Start_Exclude]
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification("+91"+login_phone_number.text.toString())
        }
        //[End_Exclude]
    }
    //[End on_start_check_user]

    private fun initUI() {
        login_button.setOnClickListener {
            if (!validatePhoneNumber()) {
                return@setOnClickListener
            }
            startPhoneNumberVerification("+91"+login_phone_number.text.toString())
        }

        verify_button.setOnClickListener {
            val code = getVerficationCode()
            if (TextUtils.isEmpty(code)) {
                showSnackbar("Phone number cannot be empty")
                return@setOnClickListener
            }
            verifyPhoneNumberWithCode(mVerificationId!!.toString(), code)
        }
        resend_button.setOnClickListener {
            resendVerificationCode("+91"+login_phone_number.text.toString(), mResendToken)
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
                showSnackbar("onVerificationCompleted:" + credential)
                // [START_EXCLUDE silent]
                mVerificationInProgress = false
                //[END_EXCLUDE]
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                // [START_EXCLUDE silent]
                showSnackbar("onVerificationFailed")
                mVerificationInProgress = false
                //[END_EXCLUDE]

                if (e is FirebaseAuthInvalidCredentialsException) {
                    //Invalid Request
                    showSnackbar("Invalid Phone Number")
                } else if (e is FirebaseTooManyRequestsException) {
                    //The SMS quote for the project has exceeded
                    showSnackbar("Quota Exceeded")
                }
                showSnackbar("Verification Failed")
            }

            override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken?) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                showSnackbar("Code Sent:" + verificationId)
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId
                mResendToken = token
            }

            override fun onCodeAutoRetrievalTimeOut(verificationId: String?) {
                super.onCodeAutoRetrievalTimeOut(verificationId)
                showSnackbar("Code TimeOut")
            }
        }
        // [END phone_auth_callbacks]
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        showSnackbar("Sign in with Credentials: successful")
                        val user = task.result.user
                        startActivity(Intent(this, HomeActivity::class.java))
                    } else {
                        // Sign in failed, display a message and update the UI
                        showSnackbar("Sign in with Credentials: failed")
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            showSnackbar("Invalid Code.")
                        }
                        showSnackbar("Sign in Failed")
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
        val code=verificartion_code.text.toString()
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


    private fun validatePhoneNumber(): Boolean {
        val phoneNumber = "+91"+login_phone_number.text.toString()
        if (TextUtils.isEmpty(phoneNumber)) {
            showSnackbar("Invalid Phone Number")
            return false
        }
        //TODO: Have better validation of phone algo and also use text layout to show error
        return true
    }

    companion object {
        const val RC_SIGN_IN = 123
    }

    private fun signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    showSnackbar("Sign Out Successful")
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            when {
                resultCode == Activity.RESULT_OK -> {
                    showSnackbar("Sign In Successful")
                    return
                }
                response == null -> {
                    showSnackbar("Sign In Cancelled")
                    return
                }
                response.errorCode == ErrorCodes.NO_NETWORK -> {
                    showSnackbar("No Network")
                    return
                }

                else -> {
                    showSnackbar("Unknown Response")
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
    
    fun showSnackbar(message:String){
        Log.e("LoginActivity",message)
        //Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }
}
