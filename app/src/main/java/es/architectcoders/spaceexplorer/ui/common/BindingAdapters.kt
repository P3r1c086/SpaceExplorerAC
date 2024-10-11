package es.architectcoders.spaceexplorer.ui.common

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import es.architectcoders.domain.NotificationsItem
import es.architectcoders.spaceexplorer.ui.notifications.NotificationsAdapter
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@BindingAdapter("url")
fun ImageView.bindUrl(url: String?) {
    if (url != null) loadUrl(url)
}

@BindingAdapter("visible")
fun View.setVisible(visible: Boolean?) {
    visibility = if (visible == true) View.VISIBLE else View.GONE
}

@BindingAdapter("notifications")
fun RecyclerView.setItems(notifications: List<NotificationsItem>?) {
    if (notifications != null) {
        (adapter as? NotificationsAdapter)?.submitList(notifications)
    }
}

@BindingAdapter("messageTypeText")
fun setMessageTypeText(textView: MaterialTextView, messageType: String?) {
    messageType?.let {
        val messageText = when (it) {
            "FLR" -> "Llamarada Solar"
            "SEP" -> "Partícula Solar Energética"
            "CME" -> "Análisis Eyecciones Masa Coronal"
            "IPS" -> "Choque Interplanetario"
            "MPC" -> "Travesía de la Magnetopausa"
            "GST" -> "Tormenta Geomagnética"
            "RBE" -> "Aumento del Cinturón de Radiación"
            "Report" -> "Informe"
            else -> ""
        }
        textView.text = messageText
    }
}


@BindingAdapter("formattedIssueTime")
fun MaterialTextView.setFormattedIssueTime(issueTime: String?) {
    issueTime?.let {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("'Fecha:' yyyy-MM-dd 'Hora:' h:mm a z", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(it)
        outputFormat.timeZone = TimeZone.getTimeZone("UTC")
        text = outputFormat.format(date)
    }
}

@BindingAdapter("errorVisible")
fun View.setErrorVisible(error: es.architectcoders.domain.Error?) {
    visibility = if (error != null) {
        when (error) {
            is es.architectcoders.domain.Error.Unknown -> View.VISIBLE
            is es.architectcoders.domain.Error.Server -> View.VISIBLE
            is es.architectcoders.domain.Error.Connectivity -> View.VISIBLE
            else -> View.GONE
        }
    } else {
        View.GONE
    }
}

@BindingAdapter("errorMessage")
fun TextView.setErrorMessage(error: es.architectcoders.domain.Error?) {
    text = when (error) {
        is es.architectcoders.domain.Error.Unknown -> error.message
        is es.architectcoders.domain.Error.Server -> "Error de servidor: ${error.code}"
        is es.architectcoders.domain.Error.Connectivity -> "Error de conectividad"
        else -> ""
    }
}