package com.example.awsvmsguild

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import com.arthurivanets.arvi.PlayerProviderImpl

class MyAmplifyApp: Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.configure(applicationContext)
            Log.i("MyAmplifyApp", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        if(level >= TRIM_MEMORY_BACKGROUND) {
            PlayerProviderImpl.getInstance(this).release()
        }
    }
}