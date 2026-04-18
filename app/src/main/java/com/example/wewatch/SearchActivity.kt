package com.example.wewatch

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wewatch.data.AppDatabase
import com.example.wewatch.data.MovieRepository
import com.example.wewatch.data.omdb.OmdbMovie
import com.example.wewatch.databinding.ActivitySearchBinding
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: SearchAdapter
    private lateinit var repository: MovieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getInstance(this)
        repository = MovieRepository(database.movieDao())

        setupRecyclerView()

        // Получаем параметры поиска из Intent
        val query = intent.getStringExtra("query") ?: ""
        val year = intent.getStringExtra("year")
        if (query.isNotEmpty()) {
            performSearch(query, year)
        } else {
            binding.tvEmpty.visibility = android.view.View.VISIBLE
            binding.tvEmpty.text = "Нет поискового запроса"
        }
    }

    private fun setupRecyclerView() {
        adapter = SearchAdapter { movie ->
            // Возвращаем выбранный фильм в AddActivity
            val intent = Intent().apply {
                putExtra("imdbID", movie.imdbID)
                putExtra("title", movie.title)
                putExtra("year", movie.year)
                putExtra("posterUrl", movie.posterUrl)
                putExtra("type", movie.type)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.rvSearchResults.layoutManager = LinearLayoutManager(this)
        binding.rvSearchResults.adapter = adapter
    }

    private fun performSearch(query: String, year: String?) {
        binding.progressBar.visibility = android.view.View.VISIBLE
        lifecycleScope.launch {
            try {
                val results = repository.searchMovies(query, year)
                adapter.submitList(results)
                if (results.isEmpty()) {
                    binding.tvEmpty.visibility = android.view.View.VISIBLE
                    binding.tvEmpty.text = "Ничего не найдено"
                } else {
                    binding.tvEmpty.visibility = android.view.View.GONE
                }
            } catch (e: Exception) {
                binding.tvEmpty.visibility = android.view.View.VISIBLE
                binding.tvEmpty.text = "Ошибка: ${e.message}"
                Toast.makeText(this@SearchActivity, "Ошибка сети", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = android.view.View.GONE
            }
        }
    }
}