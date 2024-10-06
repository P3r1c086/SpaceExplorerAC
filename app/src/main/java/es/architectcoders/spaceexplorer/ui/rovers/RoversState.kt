package es.architectcoders.spaceexplorer.ui.rovers

import android.Manifest
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import es.architectcoders.spaceexplorer.ui.common.PermissionRequester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.buildRoversState(
    scope: CoroutineScope = viewLifecycleOwner.lifecycleScope,
    storagePermissionRequester: PermissionRequester = PermissionRequester(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
) = RoversState(scope, storagePermissionRequester)

class RoversState(
    private val scope: CoroutineScope,
    private val storagePermissionRequester: PermissionRequester
) {

    fun requestStoragePermission(afterRequest: (Boolean) -> Unit) {
        scope.launch {
            val result = storagePermissionRequester.request()
            if (Build.VERSION.SDK_INT <= 30){
                afterRequest(result)
            }else{
                afterRequest(true)
            }
        }
    }
}