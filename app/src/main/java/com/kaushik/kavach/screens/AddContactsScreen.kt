package com.kaushik.kavach.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.kaushik.kavach.db.Contact
import com.kaushik.kavach.db.ContactDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun AddContactsScreen(db: ContactDatabase) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("Add contact") }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text, fontSize = 30.sp, textAlign = TextAlign.Center)
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            placeholder = { Text("Enter Name") }
        )
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Number") },
            placeholder = { Text("Enter Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
        Button(
            onClick = {
                if (name.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(context, "enter all details", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                GlobalScope.launch {
                    db.contactDao().upsertAll(
                        Contact(
                            name = name,
                            phoneNumber = phoneNumber
                        )
                    )
                    contactsList.value = db.contactDao().getAll()
                    name = ""
                    phoneNumber = ""
                    text = "contact added\nyou can add more"
                }
            }
        ) {
            Text(text = "Add")
        }
    }
}