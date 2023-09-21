package com.example.lab04

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class JokeViewModel(private val repository: JokeRepository) : ViewModel() {
    val currentJoke: LiveData<JokeData> = repository.currentJoke

    val allJokes: LiveData<List<JokeData>> = repository.allJokes

    fun checkJokes(joke: String){
        repository.checkJokes(joke)
    }
    fun addData(joke: JokeResult) {
      viewModelScope.launch{
          repository.checkJokes(joke.value)
      }
    }

}

// This factory class allows us to define custom constructors for the view model
class JokeViewModelFactory(private val repository: JokeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JokeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JokeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
