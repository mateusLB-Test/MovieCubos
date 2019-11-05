package com.mateus.batista.feature_movie.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mateus.batista.base_feature.listeners.OnItemClickListener
import com.mateus.batista.base_feature.util.setImgByUrl
import com.mateus.batista.feature_movie.R
import com.mateus.batista.feature_movie.model.MovieFeature
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter(
    val list: MutableList<MovieFeature>,
    private val listener: OnItemClickListener<MovieFeature>
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_movie,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bindItemView(item) { listener.onItemClick(item, position) }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bindItemView(item: MovieFeature, onItemClick: () -> Unit) {
            view.apply {
                imgList.setImgByUrl(item.posterPath)
                title.text = item.title
                setOnClickListener { onItemClick() }
            }
        }
    }
}