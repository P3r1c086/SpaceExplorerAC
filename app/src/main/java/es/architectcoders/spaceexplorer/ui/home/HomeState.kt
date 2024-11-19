package es.architectcoders.spaceexplorer.ui.home

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import es.architectcoders.spaceexplorer.R
import es.architectcoders.spaceexplorer.databinding.FragmentHomeBinding
import es.architectcoders.spaceexplorer.ui.common.Constants
import es.architectcoders.spaceexplorer.ui.common.loadUrl
import es.architectcoders.spaceexplorer.ui.model.ApodObject


fun Fragment.buildHomeState(
    binding: FragmentHomeBinding,
    navController: NavController = findNavController(),
) = HomeState(fragment = this, binding = binding, navController = navController)

class HomeState(
    private val fragment: Fragment,
    private val binding: FragmentHomeBinding,
    private val navController: NavController
) {
    fun navigateToDetail(apodArgs: ApodObject, afterNavigate: () -> Unit) {
        val navAction = HomeFragmentDirections.actionHomeFragmentToHomeDetailFragment(apodArgs)
        navController.navigate(navAction)
        afterNavigate()
    }

    fun onBackPressed() { fragment.requireActivity().finish() }

    fun updateUi(apod: ApodObject) {
        binding.cvApod.visibility = View.VISIBLE
        binding.ivApodPhoto.loadUrl(apod.url)
        binding.ivApodPhoto.ratio = 1.5f
        binding.tvTitle.text = apod.title
        binding.tvDate.text = apod.date
        binding.tvExplanation.text = apod.explanation
        if (apod.favorite) {
            binding.ivApodFav.setImageResource(R.drawable.ic_favorite_fill)
            binding.ivApodFav.tag = Constants.FAVORITE_TAG
        } else {
            binding.ivApodFav.setImageResource(R.drawable.ic_favorite_empty)
            binding.ivApodFav.tag = Constants.NOT_FAVORITE_TAG
        }
        if (apod.copyright.isNotEmpty()) {
            binding.tvCopyright.text = fragment.getString(R.string.copyright, apod.copyright)
        } else {
            binding.tvCopyright.visibility = View.INVISIBLE
        }
    }

}