package com.example.teachmint.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teachmint.data.model.Contributor
import com.example.teachmint.data.model.Repository
import com.example.teachmint.repository.RepositoryRepository
import kotlinx.coroutines.launch

class RepositoryViewModel : ViewModel() {
    private val repository = RepositoryRepository()
    var repositories: List<Repository> by mutableStateOf(emptyList())
        private set

    var contributors: List<Contributor> by mutableStateOf(emptyList())
        private set

    fun searchRepositories(query: String) {
        viewModelScope.launch {
            val response = repository.getRepositories(query)
            if (response.isSuccessful) {
                repositories = response.body()?.items ?: emptyList()
            }
        }
    }

    fun getContributors(ownerName: String, repoName: String) {
        viewModelScope.launch {
            contributors = repository.getContributors(ownerName = ownerName, repoName = repoName)
        }
    }
}
