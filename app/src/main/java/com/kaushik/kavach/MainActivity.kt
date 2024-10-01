package com.kaushik.kavach

import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.google.android.gms.location.LocationServices
import com.kaushik.kavach.db.ContactDatabase
import com.kaushik.kavach.screens.AddContactsScreen
import com.kaushik.kavach.screens.ContactsScreen
import com.kaushik.kavach.screens.HomeScreen
import com.kaushik.kavach.screens.REQUEST_PERMISSIONS_CODE
import com.kaushik.kavach.screens.ShowAlert
import com.kaushik.kavach.screens.ViewContactsScreen
import com.kaushik.kavach.screens.VoiceScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val smsManager = getSystemService(SmsManager::class.java)!!
        super.onCreate(savedInstanceState)
        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(applicationContext)
        val db by lazy {
            Room.databaseBuilder(
                applicationContext,
                ContactDatabase::class.java, "contacts"
            ).build()
        }
        setContent {
            val navController = rememberNavController()
            Box {
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(navController)
                    }
                    composable("voice") {
                        VoiceScreen(navController, fusedLocationClient, smsManager)
                    }
                    composable("alert-voice") {
                        val iconsList = listOf(
                            painterResource(id = R.drawable.location),
                            painterResource(id = R.drawable.mic),
                            painterResource(id = R.drawable.sms)
                        )
                        ShowAlert(navController, iconsList)
                    }
                    composable("contacts") {
                        ContactsScreen(navController, db)
                    }
                    composable("alert-contacts") {
                        val iconsList = listOf(
                            painterResource(id = R.drawable.contact)
                        )
                        ShowAlert(navController, iconsList)
                    }
                    composable("add-contact") {
                        AddContactsScreen(db)
                    }
                    composable("view-contacts") {
                        ViewContactsScreen(db)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val permissions = arrayOf(
            android.Manifest.permission.RECORD_AUDIO,
//            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.SEND_SMS
        )

        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_CODE)
        }
    }
}
