package es.architectcoders.spaceexplorer.ui.common

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import es.architectcoders.domain.Apod
import es.architectcoders.spaceexplorer.R
import es.architectcoders.spaceexplorer.ui.model.ApodObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
fun MenuItem.onHomeSelected(navController: NavController) {
    this.setOnMenuItemClickListener { menuItem ->
        navController.navigate(menuItem.itemId)
        true
    }
}

fun ImageView.loadUrl(url: String?) {
    val circularProgressDrawable = CircularProgressDrawable(context).apply {
        strokeWidth = 5f
        centerRadius = 30f
        setColorSchemeColors(resources.getColor(R.color.blue_dark, null))
    }
    circularProgressDrawable.start()
    Glide.with(context).load(url)
        .error(R.mipmap.ic_launcher)
        .placeholder(circularProgressDrawable)
        .into(this)
}

fun saveImageFromUrlToGallery(imageUrl: String, context: Context) {
    Glide.with(context)
        .asBitmap()
        .load(imageUrl)
        .into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                saveBitmapToGallery(resource, context)
            }
        })
}

private fun saveBitmapToGallery(bitmap: Bitmap, context: Context) {
    val fileName = "${System.currentTimeMillis()}.jpg"
    var outputStream: OutputStream? = null

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        outputStream = imageUri?.let { resolver.openOutputStream(it) }
    } else {
        val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, fileName)
        outputStream = FileOutputStream(image)
    }

    outputStream?.use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        Toast.makeText(context, R.string.save_image, Toast.LENGTH_SHORT).show()
    }
}

fun LinearLayout.toggleVisibilityWithAnimation(imageButton: ImageButton, duration: Long = 200) {
    if (visibility == View.VISIBLE) {
        animateVisibilityWithAnimation(false, duration, imageButton)
    } else {
        animateVisibilityWithAnimation(true, duration, imageButton)
    }
}

private fun LinearLayout.animateVisibilityWithAnimation(
    show: Boolean,
    duration: Long,
    imageButton: ImageButton
) {
    val targetAlpha = if (show) 1f else 0f

    animate()
        .alpha(targetAlpha)
        .setDuration(duration)
        .withEndAction {
            visibility = if (show) View.VISIBLE else View.GONE
            alpha = 1f
            val imageResource = if (show) R.drawable.ic_expand_less else R.drawable.ic_expand_more
            imageButton.setImageResource(imageResource)
        }
        .start()
}

inline fun <T> basicDiffUtil(
    crossinline areItemsTheSame: (T, T) -> Boolean = { old, new -> old == new },
    crossinline areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new }
) = object : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean =
        areItemsTheSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any): Boolean =
        areContentsTheSame(oldItem, newItem)
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = true): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

fun LifecycleOwner.launchAndCollect(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend CoroutineScope.() -> Unit
) = lifecycleScope.launch {
    lifecycle.repeatOnLifecycle(state, block)
}

fun <T> LifecycleOwner.launchAndCollectT(
    flow: Flow<T>,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    body: (T) -> Unit
) {
    lifecycleScope.launch {
        this@launchAndCollectT.repeatOnLifecycle(state) {
            flow.collect(body)
        }
    }
}

fun ApodObject.toDomain() = Apod(
    id = id.toString(),
    copyright = copyright,
    date = date,
    explanation = explanation,
    hdurl = hdurl,
    mediaType = mediaType,
    serviceVersion = serviceVersion,
    title = title,
    url = url,
    favorite = favorite
)

fun Apod.toViewObject() = ApodObject(
    id = id.toInt(),
    copyright = copyright,
    date = date,
    explanation = explanation,
    hdurl = hdurl,
    mediaType = mediaType,
    serviceVersion = serviceVersion,
    title = title,
    url = url,
    favorite = favorite
)
