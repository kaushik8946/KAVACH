package com.kaushik.kavach.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaushik.kavach.db.Contact
import com.kaushik.kavach.db.ContactDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

var contactsList = mutableStateOf(listOf<Contact>())

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun ViewContactsScreen(db: ContactDatabase) {
    GlobalScope.launch {
        contactsList.value = db.contactDao().getAll()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (contactsList.value.isEmpty()) {
            Text(text = "No contacts found", fontSize = 20.sp)
        } else {
            LazyColumn {
                item {
                    Text("Saved Contacts are:", fontSize = 15.sp)
                }
                items(contactsList.value.size) { index ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Button(
                            onClick = {
                                GlobalScope.launch {
                                    db.contactDao().delete(contactsList.value[index])
                                    contactsList.value = db.contactDao().getAll()
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                        }

                        Column(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Text(text = contactsList.value[index].name, fontSize = 20.sp)
                            Text(text = contactsList.value[index].phoneNumber, fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }
}