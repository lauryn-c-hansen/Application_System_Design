package com.example.lab01_helloworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.lab01_helloworld.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMain2Binding.inflate(layoutInflater)

        val args = intent.extras!! //crash if it's null
        val output = args.getString("textForActivity2")

        //Toast.makeText(this, output, Toast.LENGTH_SHORT).show()
        binding.buttonOutputView.text = output
        setContentView(binding.root)


    }
}