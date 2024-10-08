package com.kaushik.kavach.screens

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaushik.kavach.db.Contact
import com.kaushik.kavach.db.ContactDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

var phoneContactsMap = mutableMapOf<String, String>()

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun ImportContactsScreen(db: ContactDatabase) {
    val phoneContactsList = phoneContactsMap.toSortedMap().map { (name, phoneNumber) ->
        Contact(phoneNumber, name)
    }
    val addIcon = Icons.Default.Add
    val deleteIcon = Icons.Default.Delete
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
    ) {
        items(phoneContactsList) { contact ->
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        val fontSize = 15.sp
                        Text(text = contact.name, fontSize = fontSize)
                        Text(text = contact.phoneNumber, fontSize = fontSize)
                    }
                    val icon = remember {
                        if (contactsList.value.contains(contact)) {
                            mutableStateOf(deleteIcon)
                        } else {
                            mutableStateOf(addIcon)
                        }
                    }
                    Button(
                        onClick = {
                            if (icon.value == addIcon) {
                                GlobalScope.launch {
                                    db.contactDao().upsertAll(contact)
                                    contactsList.value = db.contactDao().getAll()
                                    icon.value = deleteIcon
                                }
                            } else {
                                GlobalScope.launch {
                                    db.contactDao().delete(contact)
                                    contactsList.value = db.contactDao().getAll()
                                    icon.value = addIcon
                                }
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(imageVector = icon.value, contentDescription = null)
                    }
                }

            }
        }
    }
}

@SuppressLint("Range")
fun retrieveContacts(context: Context): MutableMap<String, String> {
    val phoneContactsMap = mutableMapOf<String, String>()
    val contentResolver = context.contentResolver
    val cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        ),
        null,
        null,
        null
    )

    if (cursor != null && cursor.moveToFirst()) {
        do {
            val displayName =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            phoneNumber.replace(" ", "")
            phoneNumber.replace("-", "")
            phoneNumber.replace("(", "")
            phoneNumber.replace(")", "")
            phoneContactsMap[displayName] = phoneNumber

        } while (cursor.moveToNext())
    }
    cursor?.close()
    return phoneContactsMap
}