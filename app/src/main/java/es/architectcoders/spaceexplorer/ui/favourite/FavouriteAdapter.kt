package es.architectcoders.spaceexplorer.ui.favourite

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.architectcoders.domain.Apod
import es.architectcoders.domain.Photo
import es.architectcoders.spaceexplorer.R
import es.architectcoders.spaceexplorer.databinding.FavoriteItemBinding
import es.architectcoders.spaceexplorer.ui.common.inflate
import es.architectcoders.spaceexplorer.ui.common.toggleVisibilityWithAnimation

class FavouriteAdapter :
    ListAdapter<Any, FavouriteAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.favorite_item, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        when (item) {
            is Photo -> holder.bindPhoto(item)
            is Apod -> holder.bindApod(item)
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = FavoriteItemBinding.bind(view)

        fun bindPhoto(photo: Photo) {
            binding.item = photo
            binding.ibExpand.setOnClickListener {
                binding.llData.toggleVisibilityWithAnimation(binding.ibExpand)
            }
        }

        fun bindApod(apod: Apod) {
            binding.item = apod
            binding.ibExpand.setOnClickListener {
                binding.llData.toggleVisibilityWithAnimation(binding.ibExpand)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is Photo && newItem is Photo -> oldItem.id == newItem.id
                oldItem is Apod && newItem is Apod -> oldItem.id == newItem.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is Photo && newItem is Photo -> {
                    oldItem.id == newItem.id && oldItem.imgSrc == newItem.imgSrc
                }
                oldItem is Apod && newItem is Apod -> {
                    oldItem.id == newItem.id && oldItem.title == newItem.title
                }
                else -> false
            }
        }
    }
}