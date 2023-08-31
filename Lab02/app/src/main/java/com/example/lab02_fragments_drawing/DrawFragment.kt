package com.example.lab02_fragments_drawing

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.lab02_fragments_drawing.databinding.FragmentDrawBinding
import android.view.MotionEvent

class DrawFragment : Fragment() {

    //@RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentDrawBinding.inflate(inflater)
        val viewModel: SimpleViewModel by activityViewModels()
        viewModel.bitmap.observe(viewLifecycleOwner) {
            binding.customView.setBitMap(it)
        }
//        viewModel.color.observe(viewLifecycleOwner) {
//            binding.customView.drawCircle(it)
//        }

        //Put touch listener here

        binding.customView.setOnTouchListener{
                v: View,
                event: MotionEvent
                ->
//                val x = event.x
//                val y = event.y
//                when(event.action){
//                    MotionEvent.ACTION_DOWN ->binding.customView.drawCircle(viewModel.color.value!!,x,y)
//                    MotionEvent.ACTION_MOVE ->binding.customView.drawCircle(viewModel.color.value!!,x,y)
//                }
            binding.customView.drawCircle(viewModel.color.value!!)
                         true
            //We'll want something here that draws to our bitmap
        }



        return binding.root

    }
}
