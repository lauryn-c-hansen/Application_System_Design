package com.example.lab02_fragments_drawing

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random
class SimpleViewModel :ViewModel() {

    //Model
    @RequiresApi(Build.VERSION_CODES.O)
    private val _color : MutableLiveData<Color> = MutableLiveData(Color.valueOf(1f, 1f, 0f))

    @RequiresApi(Build.VERSION_CODES.O)
    val color  = _color as LiveData<Color>

    private val displayMetrics = DisplayMetrics()
    //private var width = displayMetrics.widthPixels
    //private var height = displayMetrics.heightPixels
    //I can pas width/height instead of 800/800

    //Store the bitmap here - we want our custom view to use this bitmap rather than drawing a new one everytime
    val _bitmap: MutableLiveData<Bitmap> = MutableLiveData(Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888))
    val bitmap = _bitmap as LiveData<Bitmap>

    @RequiresApi(Build.VERSION_CODES.O)
    fun pickColor(){
        with(Random.Default) {
            _color.value = Color.valueOf(nextFloat(), nextFloat(), nextFloat())
        }
    }
}