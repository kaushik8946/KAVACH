package com.kaushik.kavach

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import java.util.Locale

@Composable
fun VoiceScreen(navController: NavHostController) {
    var text by remember { mutableStateOf("Press to say Kavach") }
    val context = LocalContext.current
    var hasMicPermission = ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED
    var hasLocationPermission = ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val permissions = mutableListOf<String>()
    if (!hasLocationPermission) {
        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }
    if (!hasMicPermission) {
        permissions.add(android.Manifest.permission.RECORD_AUDIO)
    }
    if (permissions.isNotEmpty()) {
        ActivityCompat.requestPermissions(
            context as Activity,
            permissions.toTypedArray(), REQUEST_PERMISSIONS_CODE
        )
        hasMicPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        hasLocationPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasLocationPermission || !hasMicPermission) {
            navController.popBackStack()
        }
    }
    val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {

        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)

        putExtra(RecognizerIntent.EXTRA_PROMPT, "Please speak now") // Optional prompt

    }

    speechRecognizerIntent.putExtra(
        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
    )
    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().language)
    speechRecognizer.setRecognitionListener(object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            // Speech is ready to be recognized
        }

        override fun onBeginningOfSpeech() {
            // Speech has started
        }

        override fun onRmsChanged(rmsdB: Float) {
            // RMS (Root Mean Square) of the audio has changed
        }

        override fun onBufferReceived(p0: ByteArray?) {

        }


        override fun onEndOfSpeech() {
            // Speech has ended
        }

        override fun onError(error: Int) {
            // An error occurred
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            matches?.forEach { result ->
                if (result.contains("kavach", ignoreCase = true)) {
                    Toast.makeText(context, "kavach detected", Toast.LENGTH_SHORT).show()
                }
            }
            text = "kavach detected"
            speechRecognizer.stopListening()
        }

        override fun onPartialResults(partialResults: Bundle?) {
            // Partial results available
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
            // Event occurred
        }
    })
    speechRecognizer.startListening(speechRecognizerIntent)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = text, fontSize = 50.sp, textAlign = TextAlign.Center
        )
        Button(
            onClick = {
                speechRecognizer.startListening(speechRecognizerIntent)
                text = "Listening..."
            }
        ) {
            Text(text = "Start Listening")
        }
    }
}