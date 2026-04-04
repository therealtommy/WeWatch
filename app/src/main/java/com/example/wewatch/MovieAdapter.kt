package com.example.wewatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wewatch.entity.MovieEntity

class MovieAdapter(
    private val onItemCheck: (String, Boolean) -> Unit
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private var movies = listOf<MovieEntity>()
    private val selectedIds = mutableSetOf<String>()

    fun submitList(list: List<MovieEntity>) {
        movies = list
        notifyDataSetChanged()
        selectedIds.clear()
    }

    fun getSelectedIds(): List<String> = selectedIds.toList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPoster: ImageView = itemView.findViewById(R.id.ivPoster)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvYear: TextView = itemView.findViewById(R.id.tvYear)
        private val cbSelect: CheckBox = itemView.findViewById(R.id.cbSelect)

        fun bind(movie: MovieEntity) {
            tvTitle.text = movie.title
            tvYear.text = movie.year
            Glide.with(itemView).load(movie.posterUrl).into(ivPoster)

            cbSelect.setOnCheckedChangeListener(null)
            cbSelect.isChecked = selectedIds.contains(movie.imdbID)
            cbSelect.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) selectedIds.add(movie.imdbID)
                else selectedIds.remove(movie.imdbID)
                onItemCheck(movie.imdbID, isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size
}