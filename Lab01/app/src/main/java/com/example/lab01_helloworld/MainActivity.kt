package com.example.lab01_helloworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.lab01_helloworld.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            //inflate ~ constructor
            val binding = ActivityMainBinding.inflate(layoutInflater)
            binding.button1.setOnClickListener(clickListener)
            binding.button2.setOnClickListener(clickListener)
            binding.button3.setOnClickListener(clickListener)
            binding.button4.setOnClickListener(clickListener)
            binding.button5.setOnClickListener(clickListener)
            setContentView(binding.root)
        }

        private val clickListener = View.OnClickListener {
            val button = it as Button
            val intent = Intent(this, MainActivity2::class.java)
            val argsBundle = Bundle()
            argsBundle.putString("textForActivity2", button.text.toString())
            intent.putExtras(argsBundle)
            startActivity(intent)
        }

    }