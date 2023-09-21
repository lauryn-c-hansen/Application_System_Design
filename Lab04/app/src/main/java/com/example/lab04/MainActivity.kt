package com.example.lab04

import android.graphics.drawable.shapes.Shape
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab04.ui.theme.Lab04Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.round
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Lab04Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column{
                        //Added from networking demo
                        val vm: JokeViewModel by viewModels()
                        val scope = rememberCoroutineScope()
                        var job: Job? by remember { mutableStateOf<Job?>((null)) }

                        Row {
                            Button(onClick = {
                                job = scope.launch {
                                    delay(500) //give me the chance to cancel
                                    val response = getChuckNorrisJokeCoroutine()
                                    vm.addData(response)
                                }
                            }) {
                                Text("Get a Chuck Norris joke")
                            }

                    }
                    val entries = vm.data.observeAsState()
                    LazyColumn {
                        for (entry in (entries.value ?: listOf()).asReversed()) {
                            item {
                                Text(
                                    "Joke: ${entry.value}")
                            }
                        }
                    }
                }
            }
        }
    }
}



//@Composable
//fun JokeButton(
//    onClick: () -> Unit) {
//    Button(onClick = { onClick() }) {
//        Text("Get a Chuck Norris Joke")
//    }
//}

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


data class JokeResult(var value: String)

class JokeViewModel : ViewModel() {
    private val _data = MutableLiveData<List<JokeResult>>(listOf())
    val data = _data as LiveData<List<JokeResult>>
    fun addData(newVal: JokeResult) {
        _data.value = _data.value?.plus(newVal)
        Log.e("UPDATE", "${data.value?.size}")
    }
}

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
}}
