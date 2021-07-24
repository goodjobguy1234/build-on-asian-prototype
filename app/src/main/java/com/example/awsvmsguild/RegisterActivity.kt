package com.example.awsvmsguild


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify
import com.example.awsvmsguild.data.SignUPData
import com.example.awsvmsguild.extension.loadingDialog
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    lateinit var loadingDialog: AlertDialog
    private var signUpResult: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        loadingDialog = loadingDialog(R.layout.view_loading_dialog, "Logging in", "Loading")
    }

//    fun onNextClicked(view: View) {
//        val password = password_input.text.toString()
//        val re_password = repassword_input.text.toString()
//
//        if (password == re_password) {
//            loadingDialog.show()
//            GlobalScope.launch(Dispatchers.IO) {
//                signingUp()
//            }
//        } else {
//            password_input.error = "Different password"
//            repassword_input.error = "Different password"
//        }
//    }
//    fun onCancleClicked(view: View) {
//        onBackPressed()
//    }
//
    suspend fun signingUp(data: SignUPData) {
//        val email = email_input.text.toString()
//        val password = password_input.text.toString()
//        val username = user_input.text.toString()
        val options = AuthSignUpOptions.builder()
            .userAttributes(listOf(
                AuthUserAttribute(AuthUserAttributeKey.email(), data.email),
                AuthUserAttribute(AuthUserAttributeKey.gender(), data.userInfo!!.gender),
                AuthUserAttribute(AuthUserAttributeKey.name(), "${data.userInfo!!.firstname} ${data.userInfo!!.lastname}"),
                AuthUserAttribute(AuthUserAttributeKey.birthdate(), data.userInfo!!.birthdate)
            ))
            .build()
        try {
            val result = Amplify.Auth.signUp(data.username, data.password, options)
            signUpResult = result.isSignUpComplete
            if (signUpResult) {
                GlobalScope.launch(Dispatchers.Main) {
                    loadingDialog.hide()
                    val output = Intent().also {
                        it.putExtra("result", signUpResult)
                    }
                    setResult(RESULT_OK, output)
                    finish()
                }
            }
            Log.i("RegisterResultS", "Result: $result")
        } catch (error: com.amplifyframework.auth.AuthException.InvalidPasswordException) {
//            GlobalScope.launch(Dispatchers.Main) {
//                password_input.error = "Password did not conform with policy"
//                repassword_input.error = "Password did not conform with policy"
//            }
            Log.e("RegisterResultF", "Sign up failed", error)
        }
    }

    override fun onDestroy() {
        loadingDialog.dismiss()
        super.onDestroy()
    }
}