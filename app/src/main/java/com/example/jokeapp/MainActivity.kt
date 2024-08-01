package com.example.jokeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.jokeapp.ui.theme.JokeAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JokeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JokeScreen()
                }
            }
        }
    }
}

@Composable
fun JokeScreen() {
    var joke by remember { mutableStateOf("Tekan tombol untuk leluconnya!") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = joke, fontSize = 20.sp)
        Button(onClick = {
            coroutineScope.launch {
                fetchJoke { newJoke ->
                    joke = newJoke
                }
            }
        }) {
            Text("Lelucon Baru")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewJokeScreen() {
    JokeScreen()
}

private fun fetchJoke(onJokeFetched: (String) -> Unit) {
    RetrofitClient.apiService.getJoke().enqueue(object : Callback<JokeResponse> {
        override fun onResponse(call: Call<JokeResponse>, response: Response<JokeResponse>) {
            if (response.isSuccessful) {
                onJokeFetched(response.body()?.joke ?: "Lelucon tidak ditemukan")
            } else {
                onJokeFetched("Lelucon gagal difetch")
            }
        }

        override fun onFailure(call: Call<JokeResponse>, t: Throwable) {
            onJokeFetched("Error: ${t.message}")
        }
    })
}