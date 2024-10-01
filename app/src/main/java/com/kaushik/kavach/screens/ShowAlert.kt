package com.kaushik.kavach.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowAlert(navController: NavHostController, iconsList: List<Painter>) {
    val context = LocalContext.current

    BasicAlertDialog(
        onDismissRequest = {
            Toast.makeText(context, "App can't run without permissions", Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier
            .fillMaxWidth(.7f)
            .fillMaxHeight(.3f)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                iconsList.forEach {
                    Icon(
                        painter = it,
                        contentDescription = "icon"
                    )
                }
            }
            Text(
                text = "Please grant permissions to continue\n" +
                        "Go to settings and grant permissions",
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = {
                        ContextCompat.startActivity(
                            context, Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:com.kaushik.kavach")
                            ), null
                        )
                    }
                ) {
                    Text(text = "Settings")
                }
                Button(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}