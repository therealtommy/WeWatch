package com.example.wewatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wewatch.data.MovieRepository
import com.example.wewatch.entity.MovieEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Collections.emptyList

class MainViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _watchlist = MutableStateFlow<List<MovieEntity>>(emptyList())
    val watchlist: StateFlow<List<MovieEntity>> = _watchlist.asStateFlow()

    init {
        loadWatchlist()
    }

    fun loadWatchlist() {
        viewModelScope.launch {
            _watchlist.value = repository.getAllWatchlist()
        }
    }

    fun deleteSelected(selectedIds: List<String>) {
        viewModelScope.launch {
            repository.removeMultiple(selectedIds)
            loadWatchlist() // обновляем список
        }
    }
}