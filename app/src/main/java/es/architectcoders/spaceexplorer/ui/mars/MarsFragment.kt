package es.architectcoders.spaceexplorer.ui.mars

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import es.architectcoders.spaceexplorer.R

@AndroidEntryPoint
class MarsFragment : Fragment() {

    companion object {
        fun newInstance() = MarsFragment()
    }

    private lateinit var viewModel: MarsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mars, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MarsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}