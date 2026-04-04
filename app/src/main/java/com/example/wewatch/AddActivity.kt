package com.example.wewatch

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.wewatch.data.AppDatabase
import com.example.wewatch.data.MovieRepository
import com.example.wewatch.databinding.ActivityAddBinding
import com.example.wewatch.entity.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private lateinit var repository: MovieRepository
    private var selectedMovie: MovieEntity? = null

    // Регистрируем callback для получения результата из SearchActivity
    private val searchLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val imdbID = result.data?.getStringExtra(EXTRA_IMDB_ID)
            val title = result.data?.getStringExtra(EXTRA_TITLE)
            val year = result.data?.getStringExtra(EXTRA_YEAR)
            val posterUrl = result.data?.getStringExtra(EXTRA_POSTER_URL)
            val genre = result.data?.getStringExtra(EXTRA_GENRE)

            if (imdbID != null && title != null) {
                selectedMovie = MovieEntity(
                    imdbID = imdbID,
                    title = title,
                    year = year ?: "N/A",
                    posterUrl = posterUrl,
                    genre = genre
                )
                displaySelectedMovie(selectedMovie!!)
                binding.btnAddMovie.isEnabled = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализируем репозиторий (через синглтон БД)
        val database = AppDatabase.getInstance(this)
        repository = MovieRepository(database.movieDao())

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnSearch.setOnClickListener {
            val query = binding.etQuery.text.toString().trim()
            if (query.isEmpty()) {
                binding.etQuery.error = "Введите название фильма"
                return@setOnClickListener
            }
            val year = binding.etYear.text.toString().trim().takeIf { it.isNotEmpty() }
            // Запускаем SearchActivity с параметрами поиска
            val intent = Intent(this, SearchActivity::class.java).apply {
                putExtra(SearchActivity.EXTRA_QUERY, query)
                putExtra(SearchActivity.EXTRA_YEAR, year)
            }
            searchLauncher.launch(intent)
        }

        binding.btnAddMovie.setOnClickListener {
            selectedMovie?.let { movie ->
                lifecycleScope.launch {
                    try {
                        withContext(Dispatchers.IO) {
                            repository.addToWatchlist(movie)
                        }
                        Toast.makeText(this@AddActivity, "Фильм добавлен", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@AddActivity, "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun displaySelectedMovie(movie: MovieEntity) {
        binding.cardSelectedMovie.visibility = android.view.View.VISIBLE
        binding.tvTitle.text = movie.title
        binding.tvYear.text = movie.year
        Glide.with(this)
            .load(movie.posterUrl)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.ivPoster)
    }

    companion object {
        const val EXTRA_IMDB_ID = "imdb_id"
        const val EXTRA_TITLE = "title"
        const val EXTRA_YEAR = "year"
        const val EXTRA_POSTER_URL = "poster_url"
        const val EXTRA_GENRE = "genre"
    }
}