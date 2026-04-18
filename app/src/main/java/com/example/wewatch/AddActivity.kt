package com.example.wewatch

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.wewatch.data.AppDatabase
import com.example.wewatch.data.MovieRepository
import com.example.wewatch.databinding.ActivityAddBinding
import com.example.wewatch.entity.MovieEntity
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private lateinit var repository: MovieRepository

    // Данные выбранного фильма (из SearchActivity)
    private var selectedImdbID: String? = null
    private var selectedTitle: String? = null
    private var selectedYear: String? = null
    private var selectedPosterUrl: String? = null
    private var selectedType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getInstance(this)
        repository = MovieRepository(database.movieDao())

        // Получаем данные из Intent (если вернулись из SearchActivity)
        selectedImdbID = intent.getStringExtra("imdbID")
        selectedTitle = intent.getStringExtra("title")
        selectedYear = intent.getStringExtra("year")
        selectedPosterUrl = intent.getStringExtra("posterUrl")
        selectedType = intent.getStringExtra("type")

        if (selectedImdbID != null && selectedTitle != null) {
            // Показать карточку с выбранным фильмом
            binding.cardSelectedMovie.visibility = android.view.View.VISIBLE
            binding.tvTitle.text = selectedTitle
            binding.tvYear.text = selectedYear ?: ""
            Glide.with(this).load(selectedPosterUrl).into(binding.ivPoster)
            binding.btnAddMovie.isEnabled = true
        } else {
            binding.cardSelectedMovie.visibility = android.view.View.GONE
            binding.btnAddMovie.isEnabled = false
        }

        // Кнопка поиска – открыть SearchActivity с введёнными данными
        binding.btnSearch.setOnClickListener {
            val query = binding.etQuery.text.toString().trim()
            val year = binding.etYear.text.toString().trim()
            if (query.isEmpty()) {
                Toast.makeText(this, "Введите название фильма", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, SearchActivity::class.java).apply {
                putExtra("query", query)
                putExtra("year", year.ifEmpty { null })
            }
            startActivity(intent)
        }

        // Кнопка добавления в избранное
        binding.btnAddMovie.setOnClickListener {
            if (selectedImdbID == null || selectedTitle == null) {
                Toast.makeText(this, "Сначала выберите фильм", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val movie = MovieEntity(
                imdbID = selectedImdbID!!,
                title = selectedTitle!!,
                year = selectedYear ?: "",
                posterUrl = selectedPosterUrl ?: "",
                type = selectedType
            )
            lifecycleScope.launch {
                repository.addMovie(movie)
                Toast.makeText(this@AddActivity, "Фильм добавлен", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }
        }
    }
}