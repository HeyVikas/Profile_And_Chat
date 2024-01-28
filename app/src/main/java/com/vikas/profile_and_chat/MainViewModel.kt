package com.vikas.profile_and_chat

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    var profile = mutableStateOf(Profile())
    var submit = mutableStateOf(false)
    private  var sendData = FirebaseRepo()
    var userList = mutableStateOf(listOf(Profile()))

    fun send() {
        Log.e("ABC", "Inside View Model Function")
    sendData.sendData(
        name = profile.value.name,
        dob = profile.value.dob,
        mobile = profile.value.mobile,
        address = profile.value.address,
        gmailId = profile.value.gmailId
    )

    }
            
            
fun fetchData (){
    viewModelScope.launch {
      Log.e("LIST", userList.value.toString())
        sendData.getData().also {
            userList.value = it
            Log.e("LIST", userList.value.toString())

        }

    }
}
}