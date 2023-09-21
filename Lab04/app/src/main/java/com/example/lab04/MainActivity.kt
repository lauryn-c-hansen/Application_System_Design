package com.example.lab04

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.lab04.ui.theme.Lab04Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm: JokeViewModel by viewModels { JokeViewModelFactory((application as JokeApplication).jokeRepository) }

        setContent {
            Lab04Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val currentJoke by vm.currentJoke.observeAsState()

                    Column {
                        val scope = rememberCoroutineScope()
                        JokeDataDisplay(currentJoke)
                        Spacer(modifier = Modifier.padding(32.dp))

                        Text(
                            "Previous Joke Data",
                            fontSize = 12.em,
                            lineHeight = 1.em
                        )

                        Row {
                            Button(onClick = {
                                scope.launch {
                                    delay(500) //give me the chance to cancel
                                    val response = getChuckNorrisJokeCoroutine()
                                    vm.addData(response)
                                }
                            }) {
                                Text("Get a Chuck Norris joke")
                            }

                        }

                        Spacer(modifier = Modifier.padding(16.dp))


                        val allJokes by vm.allJokes.observeAsState()
                        LazyColumn {
                            for (data in allJokes ?: listOf()) {
                                item {
                                    JokeDataDisplay(data = data)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JokeDataDisplay(data: JokeData?, modifier: Modifier = Modifier) {
    Surface(color=MaterialTheme.colorScheme.surface) {
        if (data != null) {
            Text(
                "Joke: ${data.value}"
            )
        }
    }
}

data class JokeResult(var value: String)

//class JokeViewModel(repository: JokeRepository) : ViewModel() {
//    private val _data = MutableLiveData<List<JokeResult>>(listOf())
//    val data = _data as LiveData<List<JokeResult>>
//    fun addData(newVal: JokeResult) {
//        _data.value = _data.value?.plus(newVal)
//        Log.e("UPDATE", "${data.value?.size}")
//    }
//}

suspend fun getChuckNorrisJokeCoroutine(): JokeResult {
    return withContext(Dispatchers.IO) {

        val url: Uri = Uri.Builder().scheme("https")
            .authority("api.chucknorris.io")
            .appendPath("jokes")
            .appendPath("random").build()

        Log.d("url!", "$url")

        val conn = URL(url.toString()).openConnection() as HttpURLConnection
        conn.connect()
        Log.i("myConnection ", "has connected")
        val gson = Gson()
        val result = gson.fromJson(
            InputStreamReader(conn.inputStream, "UTF-8"),
            JokeResult::class.java
        )
        Log.d("url!", "$url")
        result
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Lab04Theme {
        Greeting("Android")
    }
}



