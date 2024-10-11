package es.architectcoders.spaceexplorer.ui.rovers

import android.content.Context
import android.os.Bundle
import android.view.View
import es.architectcoders.domain.Error
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import es.architectcoders.spaceexplorer.R
import es.architectcoders.spaceexplorer.databinding.FragmentRoversBinding
import es.architectcoders.spaceexplorer.ui.common.launchAndCollectT
import es.architectcoders.spaceexplorer.ui.common.saveImageFromUrlToGallery

@AndroidEntryPoint
class RoversFragment : Fragment(R.layout.fragment_rovers) {

    companion object {
        fun newInstance() = RoversFragment()
    }

    private val viewModel: RoversViewModel by viewModels()

    private lateinit var roversState: RoversState

    private val onDownloadImageOnClick: (url: String, context: Context) -> Unit = { url, context ->
        roversState.requestStoragePermission {
            if (it) {
                saveImageFromUrlToGallery(url, context)
            }else{
                Snackbar.make(requireView(), R.string.permission_denied, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private val roversAdapter : RoversAdapter = RoversAdapter (
        onDownloadImageOnClick = onDownloadImageOnClick) {
        viewModel.saveRoversAsFavourite(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roversState = buildRoversState()

        val binding = FragmentRoversBinding.bind(view).apply {
            rvRovers.adapter = roversAdapter
        }

        viewLifecycleOwner.launchAndCollectT(viewModel.state) {
            binding.loading = it.loading
            binding.photoList = it.photoList
            binding.error = it.error.also { error ->
                if (error != null) {
                    when (error) {
                        is Error.Server -> showErrorDialog(getString(R.string.server_error))
                        is Error.Connectivity -> showErrorDialog(getString(R.string.connectivity_error))
                        is Error.Unknown -> showErrorDialog(getString(R.string.unknown_error))
                    }
                }
            }
        }
    }

    private fun showErrorDialog(error: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_title_error))
            .setMessage(error)
            //todo:Al cancelar el dialogo sin darle a retry el estado del error se pone en null y no
            // me pinta el error en textview del xml. En el caso de las notificaciones eso no me pasa
            .setNegativeButton(getString(R.string.dialog_cancel)) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(getString(R.string.dialog_retry)) { _, _ -> viewModel.retry() }
            .setCancelable(false)
            .show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadData() // Llama a la lógica que estaba en init para recargar datos
    }
}