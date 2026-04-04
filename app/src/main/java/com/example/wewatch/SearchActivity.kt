package com.example.wewatch

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wewatch.data.omdb.RetrofitClient
import com.example.wewatch.databinding.ActivitySearchBinding
import kotlinx.coroutines.launch
import com.example.wewatch.BuildConfig

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: SearchAdapter

    companion object {
        const val EXTRA_QUERY = "query"
        const val EXTRA_YEAR = "year"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val query = intent.getStringExtra(EXTRA_QUERY) ?: ""
        val year = intent.getStringExtra(EXTRA_YEAR)

        setupRecyclerView()
        performSearch(query, year)
    }

    private fun setupRecyclerView() {
        adapter = SearchAdapter { selectedMovie ->
            // Возвращаем выбранный фильм в AddActivity
            val resultIntent = Intent().apply {
                putExtra(AddActivity.EXTRA_IMDB_ID, selectedMovie.imdbID)
                putExtra(AddActivity.EXTRA_TITLE, selectedMovie.Title)
                putExtra(AddActivity.EXTRA_YEAR, selectedMovie.Year)
                putExtra(AddActivity.EXTRA_POSTER_URL, selectedMovie.Poster)
                putExtra(AddActivity.EXTRA_GENRE, selectedMovie.Genre)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        binding.rvSearchResults.layoutManager = LinearLayoutManager(this)
        binding.rvSearchResults.adapter = adapter
    }

    private fun performSearch(query: String, year: String?) {
        if (query.isEmpty()) {
            Toast.makeText(this, "Пустой запрос", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.progressBar.visibility = android.view.View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.searchMovies(query, year, BuildConfig.OMDB_API_KEY)
                binding.progressBar.visibility = android.view.View.GONE
                if (response.Response == "True") {
                    val movies = response.Search ?: emptyList()
                    if (movies.isNotEmpty()) {
                        adapter.submitList(movies)
                        binding.tvEmpty.visibility = android.view.View.GONE
                    } else {
                        showEmpty()
                    }
                } else {
                    showEmpty(response.Error ?: "Ошибка")
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = android.view.View.GONE
                showEmpty("Ошибка сети: ${e.message}")
            }
        }
    }

    private fun showEmpty(message: String = "Ничего не найдено") {
        binding.tvEmpty.text = message
        binding.tvEmpty.visibility = android.view.View.VISIBLE
        adapter.submitList(emptyList())
    }
}
