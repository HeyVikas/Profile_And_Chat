package com.vikas.profile_and_chat

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun fillDetails(
    viewModel: MainViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()){
        imageUri = it
    }
    Column (horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxWidth()) {
        TopAppBar(
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,),
            title = { Text(text = "Edit Profile",color = Color.Magenta,) })
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center

        ) {
            Column {
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp), horizontalArrangement = Arrangement.Center){
                    Button(onClick = { launcher.launch("image/*") }) {
                        Text(text = "+",
                            fontWeight = FontWeight.Bold)
                    }
                    imageUri?.let {
                        bitmap = if (Build.VERSION.SDK_INT < 28){
                            MediaStore.Images.Media.getBitmap(context.contentResolver,it)
                        } else {
                            val source = ImageDecoder.createSource(context.contentResolver,it)
                            ImageDecoder.decodeBitmap(source)
                        }
                        Image(bitmap = bitmap?.asImageBitmap()!!, contentDescription = "",
                            modifier = Modifier.size(200.dp))

                    }
                }
                OutlinedTextField(modifier = Modifier.fillMaxWidth(10f),
                    value = viewModel.profile.value.name ,
                    onValueChange = {
                        viewModel.profile.value = viewModel.profile.value.copy(
                            name = it
                        ) },
                    label = { Text("Name") },

                    )
                OutlinedTextField(modifier = Modifier.fillMaxWidth(10f),
                    value = viewModel.profile.value.dob,
                    onValueChange = {
                        viewModel.profile.value = viewModel.profile.value.copy(
                            dob = it)},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text("Date of Birth") }
                )
                OutlinedTextField(modifier = Modifier.fillMaxWidth(10f),
                    value = viewModel.profile.value.address,
                    onValueChange = {viewModel.profile.value = viewModel.profile.value.copy(
                        address = it)},
                    label = { Text("Address") }
                )
                OutlinedTextField(modifier = Modifier.fillMaxWidth(10f),
                    value = viewModel.profile.value.gmailId,
                    onValueChange = {viewModel.profile.value = viewModel.profile.value.copy(
                        gmailId = it)},
                    label = { Text("Gmail Id") }
                )
                OutlinedTextField(modifier = Modifier.fillMaxWidth(10f),
                    value = viewModel.profile.value.mobile,
                    onValueChange = { viewModel.profile.value = viewModel.profile.value.copy(
                        mobile = it)},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text("Mobile") }
                )
            }
        }
        Spacer(modifier = Modifier.heightIn(10.dp))
        Button(onClick = {  viewModel.submit.value = true
            viewModel.fetchData()
            viewModel.send()
            navController.navigate("home_screen")},

            modifier = Modifier
                .height(50.dp)
                .width(150.dp)
        ) {
            Text(text = "Save", color = Color.White )
        }
        if(viewModel.submit.value){
            AlertDialog(
                onDismissRequest = { viewModel.submit.value = false },
                confirmButton = {
                    Text(text = "OK",
                        modifier = Modifier.clickable {
                            viewModel.submit.value = false
                        })
                }
                , text =  { Text(text = "Submitted successfully!!") }
            )
        }
    }
}