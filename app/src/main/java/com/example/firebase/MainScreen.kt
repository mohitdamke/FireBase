package com.example.firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun MainScreen(context: Context = LocalContext.current) {

    val db = FirebaseFirestore.getInstance()
    var name by rememberSaveable { mutableStateOf("") }
    var surname by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }
    val users by rememberSaveable { mutableStateOf(mutableListOf<HashMap<String, Any>>()) } // List to store user data
    val data = hashMapOf(
        "name" to name, "surname" to surname, "age" to age
    )

//    LaunchedEffect(Unit) {
//        CoroutineScope(IO).launch {
//            try {
//                val snapshot = db.collection("user").document("222").get()
//                snapshot.snapshot.addSnapshotListener { snapshot, e -> }
////                users.clear()
//                    .
//                    .addOnSuccessListener {
//                        for (document in.documents) {
//                        val userData = document.data ?: continue
//                        users.add(userData as HashMap<String, Any>)
//                    }
//                    }
//                    .addOnFailureListener {
//                        Log.d("TAG", "Error getting documents: $it")
//                    }
//            } catch (e: Exception) {
//                Log.e("TAG", "Error getting documents: $e")
//            }
//        }
//    }


    LazyColumn {

        item {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Spacer(modifier = Modifier.padding(20.dp))
                OutlinedTextField(value = name,
                    onValueChange = { name = it },
                    label = { Text(text = "Name") })
                Spacer(modifier = Modifier.padding(20.dp))
                OutlinedTextField(value = surname,
                    onValueChange = { surname = it },
                    label = { Text(text = "surname") })
                Spacer(modifier = Modifier.padding(20.dp))
                OutlinedTextField(value = age,
                    onValueChange = { age = it },
                    label = { Text(text = "age") })
                Spacer(modifier = Modifier.padding(20.dp))
                Button(onClick = {
                    db.collection("user").add(data).addOnSuccessListener { documentReference ->
                        Toast.makeText(
                            context,
                            "DocumentSnapshot added with ID: ${documentReference.id}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener { e ->
                        Toast.makeText(context, "Error adding document $e", Toast.LENGTH_SHORT)
                            .show()
                    }

                    Toast.makeText(context, "Text is been sent", Toast.LENGTH_SHORT).show()
                    surname = ""
                    name = ""
                    age = ""


                }) {
                    Text(text = "Submit")
                }

                Button(onClick = {
                    db.collection("user").document().get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                Log.d("TAG", "DocumentSnapshot data: ${document.data}")
                                Toast.makeText(
                                    context,
                                    "Text is been ${document.data.toString()}sent",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.w("TAG", "Error getting documents: ", exception)
                        }

                }) {
                    Text(text = "Get Data")
                }
                Spacer(modifier = Modifier.padding(20.dp))

            }
        }
        items(users) {
            ListItems(userData = it)
//            Text(text = "Name: ${it["name"] ?: ""}, Surname: ${it["surname"] ?: ""}, Age: ${it["age"] ?: ""}")
        }
    }
}
