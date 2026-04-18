package com.example.wewatch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wewatch.databinding.ItemMovieBinding
import com.example.wewatch.entity.MovieEntity

class MovieAdapter(
    private val onCheckChanged: (imdbID: String, isChecked: Boolean) -> Unit
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private var items = listOf<MovieEntity>()
    private val selectedIds = mutableSetOf<String>()

    fun submitList(list: List<MovieEntity>) {
        items = list
        notifyDataSetChanged()
    }

    fun updateSelectedIds(ids: Set<String>) {
        selectedIds.clear()
        selectedIds.addAll(ids)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onCheckChanged)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = items[position]
        holder.bind(movie, selectedIds.contains(movie.imdbID))
    }

    override fun getItemCount() = items.size

    class ViewHolder(
        private val binding: ItemMovieBinding,
        private val onCheckChanged: (String, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieEntity, isChecked: Boolean) {
            binding.tvTitle.text = movie.title
            binding.tvYear.text = movie.year
            binding.cbSelect.setOnCheckedChangeListener(null)
            binding.cbSelect.isChecked = isChecked
            binding.cbSelect.setOnCheckedChangeListener { _, checked ->
                onCheckChanged(movie.imdbID, checked)
            }
            Glide.with(binding.root).load(movie.posterUrl).into(binding.ivPoster)
        }
    }
}