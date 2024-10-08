package com.kaushik.kavach.screens

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.kaushik.kavach.db.ContactDatabase

@Composable
fun ContactsScreen(navController: NavHostController, db: ContactDatabase) {
    val context = LocalContext.current
    val cardElevation = CardDefaults.cardElevation(
        defaultElevation = 10.dp,
        pressedElevation = 2.dp
    )
    val cardModifier = Modifier
        .fillMaxWidth(.7f)
        .height(200.dp)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Card(
            modifier = cardModifier,
            elevation = cardElevation,
            onClick = {
                navController.navigate("add-contact-manual")
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Add Contact Manually",
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp
                )
            }
        }
        Card(
            modifier = cardModifier,
            elevation = cardElevation,
            onClick = {
                var hasContactPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED
                if(!hasContactPermission) {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_PERMISSIONS_CODE
                    )
                    hasContactPermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_CONTACTS
                    ) == PackageManager.PERMISSION_GRANTED
                }
                if(!hasContactPermission) {
                    navController.navigate("alert-contacts")
                } else {
                    navController.navigate("import-contacts")
                }
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "import contacts",
                    fontSize = 30.sp
                )
            }
        }
        Card(
            modifier = cardModifier,
            elevation = cardElevation,
            onClick = {
                navController.navigate("view-contacts")
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "View Contacts",
                    fontSize = 30.sp
                )
            }
        }

    }
}