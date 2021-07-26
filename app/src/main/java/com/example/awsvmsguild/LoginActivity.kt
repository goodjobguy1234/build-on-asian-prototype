package com.example.awsvmsguild

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.amazonaws.mobile.auth.core.signin.AuthException
import com.amazonaws.services.cognitoidentityprovider.model.NotAuthorizedException
import com.amazonaws.services.cognitoidentityprovider.model.UserNotFoundException
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.options.AuthSignOutOptions
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.kotlin.core.Amplify
import com.example.awsvmsguild.extension.loadingDialog
import com.example.awsvmsguild.extension.snackBarShow
import com.example.awsvmsguild.homeActivity.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginActivity : AppCompatActivity() {
    lateinit var loadingDialog: AlertDialog
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.getBooleanExtra("isSuccess", false)?.let {
                if (it) Toast.makeText(this, "Email Verified", Toast.LENGTH_SHORT).show()
                else Toast.makeText(this, "Please Verify your email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loadingDialog = loadingDialog(R.layout.view_loading_dialog, "Logging in", "Loading")
        GlobalScope.launch(Dispatchers.IO) {
            checkIsSigningIn()
        }

    }

    fun onLoginCliked(view: View) {
        val username = user_input.text.toString()
        val password = password_input.text.toString()

        if (password.trim() != "" && username.trim() != "") {
            loadingDialog.show()
            GlobalScope.launch(Dispatchers.IO) {
                auth(username, password)
            }
        } else {
            user_input.error = "require input"
            password_input.error = "require input"
        }
    }

    private suspend fun auth(username: String, password: String) {
        try {
            val result = Amplify.Auth.signIn(username, password)
            if (result.isSignInComplete) {
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Login success", Toast.LENGTH_SHORT).show()
                    loadingDialog.hide()
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                }

            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                    loadingDialog.hide()
                }
            }
        }
        catch (error: com.amplifyframework.auth.AuthException.UserNotFoundException) {
            GlobalScope.launch(Dispatchers.Main) {
                loadingDialog.hide()
                cl_login.snackBarShow("User not Exists")
            }
        }

        catch (error: com.amplifyframework.auth.AuthException) {
            GlobalScope.launch(Dispatchers.Main) {
                loadingDialog.hide()
                cl_login.snackBarShow("Login Failed, Incorrect Username or Password")
                user_input.error = "Username maybe wrong"
                password_input.error = "Password maybe wrong"
            }
        }
    }

    fun onRegisterClicked(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        resultLauncher.launch(intent)
    }

    suspend fun checkIsSigningIn() {
        try {
            val session = Amplify.Auth.fetchAuthSession()
            if (session.isSignedIn) {
                val options = AuthSignOutOptions.builder()
                    .globalSignOut(true)
                    .build()

                Amplify.Auth.signOut(options)
            }
            Log.i("AmplifyQuickstart", "Auth session = $session")
        } catch (error: AuthException) {
            Log.e("AmplifyQuickstart", "Failed to fetch auth session", error)
        }
    }
}