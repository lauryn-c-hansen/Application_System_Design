package com.example.lab02_fragments_drawing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi

class CustomView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    //private val bitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
    private var bitmap = Bitmap.createBitmap(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels, Bitmap.Config.ARGB_8888)
    private var bitmapCanvas = Canvas(bitmap)
    private val paint = Paint()
    private val rect: Rect by lazy {Rect(0,0,width, height)}

    //Write a setter method to pass bitmap from SimpleViewModel
    fun setBitMap(bitmap: Bitmap){
        this.bitmap  = bitmap
        this.bitmapCanvas = Canvas(bitmap)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(bitmap, null, rect, paint)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public fun drawCircle(color: Color){

        //public fun drawCircle(color: Color, left:Float, top:Float){
        paint.color = Color.WHITE
        //bitmapCanvas.drawCircle(left,top,50f,paint)

        bitmapCanvas.drawRect(0f,0f, bitmap.width.toFloat(), bitmap.height.toFloat(), paint)
        paint.color = color.toArgb()
        bitmapCanvas.drawCircle(0.5f*bitmap.width, 0.5f*bitmap.height,
            0.25f*bitmap.width, paint)
        //call invalidate method somewhere
        invalidate()
    }

}
