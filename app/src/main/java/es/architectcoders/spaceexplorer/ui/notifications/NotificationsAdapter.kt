package es.architectcoders.spaceexplorer.ui.notifications

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.architectcoders.domain.NotificationsItem
import es.architectcoders.spaceexplorer.R
import es.architectcoders.spaceexplorer.databinding.NotificationItemBinding
import es.architectcoders.spaceexplorer.ui.common.basicDiffUtil
import es.architectcoders.spaceexplorer.ui.common.inflate

class NotificationsAdapter :
    ListAdapter<NotificationsItem, NotificationsAdapter.ViewHolder>(basicDiffUtil {
            old, new -> old.messageID == new.messageID }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.notification_item, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notificationsItem = getItem(position)
        holder.bind(notificationsItem)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = NotificationItemBinding.bind(view)

        fun bind(notificationsItem: NotificationsItem) {
            binding.notificationsItem = notificationsItem
            binding.ibExpand.setOnClickListener {
                expandLLData()
            }
        }

        private fun expandLLData() {
            if (binding.llData.visibility == View.VISIBLE) {
                binding.llData.visibility = View.GONE
                binding.ibExpand.setImageResource(R.drawable.ic_expand_more)
            } else {
                binding.llData.visibility = View.VISIBLE

                binding.tvBody.measure(
                    View.MeasureSpec.makeMeasureSpec(
                        binding.llData.width,
                        View.MeasureSpec.EXACTLY
                    ),
                    View.MeasureSpec.makeMeasureSpec(
                        binding.tvBody.height,
                        View.MeasureSpec.EXACTLY
                    )
                )

                val layoutParams = binding.llData.layoutParams
                layoutParams.height = binding.tvBody.measuredHeight
                binding.llData.layoutParams = layoutParams

                binding.ibExpand.setImageResource(R.drawable.ic_expand_less)
            }
        }
    }
}