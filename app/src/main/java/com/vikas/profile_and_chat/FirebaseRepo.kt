package com.vikas.profile_and_chat

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class FirebaseRepo {
    val db = Firebase.firestore
fun sendData(name: String, dob: String, mobile: String, address: String, gmailId: String){
    Log.e("SEND", "Inside Firebase Repo Function ${mobile}")
    val data =Profile(name, dob, mobile, address, gmailId)
    db.collection("Users")
        .document(mobile).set(data)
        .addOnSuccessListener { Log.e("ABCD", "DocumentSnapshot successfully written!") }
        .addOnFailureListener { e -> Log.e("ABCDE", "Error writing document", e) }

}
    suspend fun getData (): List<Profile>{
        return db.collection("Users")
            .get()
            .addOnSuccessListener {
                Log.d("ABC", "DocumentSnapshot Fetch successfully ! ${it.documents.size}")

            }
            .addOnFailureListener { e -> Log.w("ABCD", "Error writing document", e) }
            .await()
            .toObjects(Profile::class.java)

    }

}