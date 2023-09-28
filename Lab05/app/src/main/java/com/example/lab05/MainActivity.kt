package com.example.lab05

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.dp
import com.example.lab05.ui.theme.Lab05Theme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.content.Context;
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

//Video of app running can be viewed at: https://utah.zoom.us/rec/share/K9pK9jqlrhiLKv2cPlBmcjwob6NOkHLRP1MZsMrniBUKLIZ8qBS8BR9gp7_zvPI3.heLm4KkoLEwYNQ4G
//Passcode: s1=i%Yg5


//Updated Main Activity from Ben's compose example
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //We need a sensor manager to be able to access the gravity sensor
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        val gravFlow : Flow<FloatArray> = getGravData(sensor, sensorManager)

        setContent {
            Lab05Theme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary
                            ),
                            title = {
                                Text("MARBLE MAYHEM - CS6018 Lab05")
                            }
                        )
                    })

                { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {

                        //This x and y will be used to store collected gravity values
                        var x by remember { mutableStateOf(0.0f) }
                        var y by remember { mutableStateOf(0.0f) }

                        LaunchedEffect(key1 = gravFlow ) {
                            gravFlow.collect { gravReading ->
                                x = gravReading[0]
                                y = gravReading[1]
                            }
                        }
                        moveMarble(x,y)
                        }
                    }
                }
            }
        }
    }

//Updated method from Ben's gyroscope example
fun getGravData(sensor: Sensor, sensorManager: SensorManager): Flow<FloatArray>{
    return channelFlow {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event !== null) {
                    Log.e("Sensor event!", event.values.toString())
                    var success = channel.trySend(event.values.copyOf()).isSuccess
                    Log.e("success?", success.toString())
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                //If you care :shrug:
            }
        }
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }
}

//Updated method from Ben's gyroscope example
@Composable
fun moveMarble(floatX: Float, floatY: Float, modifier: Modifier = Modifier) {
    //loop the animations forever
    val outerInfinite = rememberInfiniteTransition(label = "outerInfinite")
    //this is the fraction of the screen that the box will cover, we'll just interpolate between
    // .2 and 1.0 of the screen size forever

    //This x and y will the the coordinates of the marble
    var x by remember { mutableStateOf(0.0f) }
    var y by remember { mutableStateOf(0.0f) }

    //Compare the gravity and marble coordinates
    if( floatX > 0){
        x+=3
    }
    if( floatX < 0){
        x-=3
    }
    if( floatY > 0){
        y+=3
    }
    if( floatY < 0){
        y-=3
    }

    if(x<0)
        x=0f
    if(y<0)
        y=0f


    //hold the internal box
    Box(modifier = modifier.fillMaxSize()) {
        //this is the blue box
        Box(modifier = modifier
            //what we're animating
            .fillMaxSize(fraction = 1.0f)
            .align(Alignment.Center)
            //make the background blue, we could animate the color if we wanted
            .drawBehind {
                drawRect(androidx.compose.ui.graphics.Color.Magenta)
            }
        ) {
            //box with constraints lets us access the maxWidth of our parent which is helpful here
            BoxWithConstraints {
                //Keeps the marble within the screen
                if(x>(maxWidth.value -80f))
                    x=maxWidth.value -80f
                if(y>(maxHeight.value -80f))
                    y=maxHeight.value -80f
                Box(
                    modifier = modifier
                        .offset( //Place the marble
                            x.dp,
                            y.dp
                        )
                        .size(80.dp) //Diameter of marble
                        .clip(CircleShape) //only draw in a circle
                        .drawBehind {
                            drawRect(androidx.compose.ui.graphics.Color.Green)
                        },
                    contentAlignment = Alignment.Center //put the text in the middle of the circle
                ) {
                }
            }

        }
    }
}




