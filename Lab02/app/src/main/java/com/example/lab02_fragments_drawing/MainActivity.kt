package com.example.lab02_fragments_drawing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.lab02_fragments_drawing.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        binding.fragmentContainerView.getFragment<ClickFragment>().setButtonFunction {
            val drawFragment = DrawFragment()
            val transaction = this.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, drawFragment, "draw_tag")
            transaction.addToBackStack(null)
            transaction.commit()
        }
        setContentView(binding.root)
    }
}