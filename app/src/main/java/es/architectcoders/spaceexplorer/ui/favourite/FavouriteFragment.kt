package es.architectcoders.spaceexplorer.ui.favourite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import es.architectcoders.spaceexplorer.R
import es.architectcoders.spaceexplorer.databinding.FragmentFavouriteBinding
import es.architectcoders.spaceexplorer.ui.common.launchAndCollectT

@AndroidEntryPoint
class FavouriteFragment : Fragment(R.layout.fragment_favourite) {

    companion object {
        fun newInstance() = FavouriteFragment()
    }

    private val viewModel: FavouriteViewModel by viewModels()

    private val favouriteAdapter : FavouriteAdapter = FavouriteAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFavouriteBinding.bind(view).apply {
            rvFavorites.adapter = favouriteAdapter
        }

        viewLifecycleOwner.launchAndCollectT(viewModel.state) {
            binding.loading = it.loading
            binding.listitems = it.items
            binding.error = it.error
            favouriteAdapter.submitList(it.items)
        }

    }
    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }
}