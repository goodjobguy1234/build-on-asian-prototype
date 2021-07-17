package com.example.awsvmsguild


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.amazonaws.mobile.auth.core.signin.AuthException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private var signUpResult: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun onNextClicked(view: View) {
        val password = password_input.text.toString()
        val re_password = repassword_input.text.toString()

        if (password == re_password) {
            GlobalScope.launch(Dispatchers.IO) {
                signingUp()
            }
        } else {
            password_input.error = "Different password"
            repassword_input.error = "Different password"
        }
    }
    fun onCancleClicked(view: View) {
        onBackPressed()
    }

    private suspend fun signingUp() {
        val email = email_input.text.toString()
        val password = password_input.text.toString()
        val username = user_input.text.toString()
        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), email)
            .build()
        try {
            val result = Amplify.Auth.signUp(username, password, options)
            signUpResult = result.isSignUpComplete
            if (signUpResult) {
                val output = Intent().also {
                    it.putExtra("result", signUpResult)
                }
                setResult(RESULT_OK, output)
                finish()
            }
            Log.i("RegisterResultS", "Result: $result")
        } catch (error: com.amplifyframework.auth.AuthException.InvalidPasswordException) {
            GlobalScope.launch(Dispatchers.Main) {
                password_input.error = "Password did not conform with policy"
                repassword_input.error = "Password did not conform with policy"
            }
            Log.e("RegisterResultF", "Sign up failed", error)
        }
    }
}