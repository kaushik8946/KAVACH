package com.kaushik.kavach

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController

const val REQUEST_PERMISSIONS_CODE = 123

@Composable
fun HomeScreen(navController: NavHostController) {
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
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Card(
            elevation = cardElevation,
            modifier = cardModifier,
            onClick = {
                var hasMicPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
                var hasLocationPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                val permissions = mutableListOf<String>()
                if (!hasLocationPermission) {
                    permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
                }
                if (!hasMicPermission) {
                    permissions.add(Manifest.permission.RECORD_AUDIO)
                }
                if (permissions.isNotEmpty()) {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        permissions.toTypedArray(), REQUEST_PERMISSIONS_CODE
                    )
                    hasMicPermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
                    hasLocationPermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                }
                if (!hasLocationPermission || !hasMicPermission) {
                    navController.navigate("alert")
                } else {
                    navController.navigate("voice")
                }
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Activate",
                    fontSize = 30.sp
                )
            }
        }
        Card(
            elevation = cardElevation,
            modifier = cardModifier,
            onClick = {
                Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show()
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Contacts",
                    fontSize = 30.sp
                )
            }
        }
    }
}

