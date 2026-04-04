package com.example.wewatch

// ui/search/SearchAdapter.kt
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wewatch.data.omdb.OmdbMovie

class SearchAdapter(
    private val onItemLongClick: (OmdbMovie) -> Unit
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var items: List<OmdbMovie> = emptyList()

    fun submitList(list: List<OmdbMovie>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        private val ivPoster = itemView.findViewById<ImageView>(R.id.ivPoster)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvYear = itemView.findViewById<TextView>(R.id.tvYear)
        private val tvGenre = itemView.findViewById<TextView>(R.id.tvGenre)

        fun bind(movie: OmdbMovie) {
            tvTitle.text = movie.Title
            tvYear.text = movie.Year
            tvGenre.text = movie.Genre ?: "Жанр не указан"
            Glide.with(itemView).load(movie.Poster).placeholder(R.drawable.ic_placeholder).into(ivPoster)

            itemView.setOnLongClickListener {
                onItemLongClick(movie)
                true
            }
        }
    }
}