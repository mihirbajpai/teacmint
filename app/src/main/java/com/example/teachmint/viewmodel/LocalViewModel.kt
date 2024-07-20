package com.example.teachmint.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teachmint.data.model.LocalModel
import com.example.teachmint.repository.LocalRepository
import kotlinx.coroutines.launch

class LocalViewModel(private val repository: LocalRepository) : ViewModel() {

    var localData: List<LocalModel> by mutableStateOf(emptyList())
        private set

    fun insertData(repo: LocalModel){
        viewModelScope.launch {
            repository.insertData(repo = repo)
        }
    }

    fun getSize(onComplete: (Int)->Unit){
        viewModelScope.launch {
            onComplete(repository.getSize())
        }
    }

    fun getAllData(){
        viewModelScope.launch {
            localData = repository.getAllData()
        }
    }
}
