package com.vikas.profile_and_chat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.firebase.ui.auth.AuthUI
import com.vikas.profile_and_chat.ui.theme.Profile_And_ChatTheme

class MainActivity : ComponentActivity() {

    val mainViewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerLoginLauncher()


        setContent {
            val navcontroller = rememberNavController()
            Profile_And_ChatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                NavHost(navController = navcontroller, startDestination = "fill_Details" ){
                composable("fill_Details"){
                    loginScreen(
                        launcherLoginFlow = ::launchLoginFlow,
                        navController = navcontroller,
                        mainViewModel = mainViewModel
                    )
                }
                }
                }
            }
        }
    }


private lateinit var loginLauncher: ActivityResultLauncher<Intent>
private fun registerLoginLauncher() {
    Log.d("TAG", "Inside setupLoginLauncher")
    loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        Log.d("TAG", "Inside ActivityResult $result")
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("TAG", "Inside ResultLambda ")
            loginHandler()
        } else Toast.makeText(this, "Not able to Login, Try Again", Toast.LENGTH_SHORT).show()
    }
}

private fun launchLoginFlow(loginHandler: (() -> Unit)) {
    this.loginHandler = loginHandler

    val intent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(
            listOf(
                AuthUI.IdpConfig.GoogleBuilder().build()
            )
        )
        .build()

    loginLauncher.launch(intent)
}

private lateinit var loginHandler: (() -> Unit)
}


