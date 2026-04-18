package com.example.wewatch

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wewatch.data.AppDatabase
import com.example.wewatch.data.MovieRepository
import com.example.wewatch.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MovieAdapter
    private lateinit var repository: MovieRepository

    private val selectedIds = mutableSetOf<String>()

    private val addMovieLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { /* данные обновятся автоматически через Flow */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getInstance(this)
        repository = MovieRepository(database.movieDao())

        setupRecyclerView()
        setupListeners()

        // Восстанавливаем выбранные ID после поворота
        savedInstanceState?.let {
            val savedIds = it.getStringArrayList("selectedIds")
            if (savedIds != null) {
                selectedIds.clear()
                selectedIds.addAll(savedIds)
                adapter.updateSelectedIds(selectedIds)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = MovieAdapter { imdbID, isChecked ->
            if (isChecked) selectedIds.add(imdbID)
            else selectedIds.remove(imdbID)
        }
        binding.rvWatchlist.layoutManager = LinearLayoutManager(this)
        binding.rvWatchlist.adapter = adapter
    }

    private fun setupListeners() {
        binding.fabAdd.setOnClickListener {
            addMovieLauncher.launch(Intent(this, AddActivity::class.java))
        }
    }

    private fun deleteSelected() {
        if (selectedIds.isEmpty()) {
            Toast.makeText(this, "Нет выбранных фильмов", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            repository.deleteMovies(selectedIds.toList())
            selectedIds.clear()
            adapter.updateSelectedIds(emptySet())
            Toast.makeText(this@MainActivity, "Фильмы удалены", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_selected -> {
                deleteSelected()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("selectedIds", ArrayList(selectedIds))
    }
}