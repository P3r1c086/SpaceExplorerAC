package es.architectcoders.spaceexplorer.ui.rovers

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.architectcoders.domain.Photo
import es.architectcoders.spaceexplorer.R
import es.architectcoders.spaceexplorer.databinding.RoversItemBinding
import es.architectcoders.spaceexplorer.ui.common.basicDiffUtil
import es.architectcoders.spaceexplorer.ui.common.inflate
import es.architectcoders.spaceexplorer.ui.common.toggleVisibilityWithAnimation

class RoversAdapter(
    private val onDownloadImageOnClick: (url: String, context: Context) -> Unit,
    private val onFavoriteClick: (Photo) -> Unit) :
    ListAdapter<Photo, RoversAdapter.ViewHolder>(basicDiffUtil { old, new -> old.id == new.id }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.rovers_item, false)
        return ViewHolder(view, onDownloadImageOnClick, onFavoriteClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(photo)
    }

    class ViewHolder(
        view: View,
        private val onDownloadImageOnClick: (url: String, context: Context) -> Unit,
        private val onFavoriteClick: (Photo) -> Unit) :
        RecyclerView.ViewHolder(view) {

        private val binding = RoversItemBinding.bind(view)

        fun bind(photo: Photo) {
            binding.photo = photo
            binding.ibExpand.setOnClickListener {
                binding.llData.toggleVisibilityWithAnimation(binding.ibExpand)
            }
            binding.ibDownload.setOnClickListener {
                onDownloadImageOnClick(photo.imgSrc, it.context)
            }
            binding.ibFavorite.setOnClickListener {
                onFavoriteClick(photo)
            }
        }
    }
}