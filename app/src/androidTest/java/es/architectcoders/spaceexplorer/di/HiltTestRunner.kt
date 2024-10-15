package es.architectcoders.spaceexplorer.di

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class HiltTestRunner: AndroidJUnitRunner() {

    //De esta forma le estamos pasando la clase HiltTestApplication::class.java.name y sera capaz de
    // usarla en lugar de la que se le da por defecto en la app, que seria la hayamos configurado en el manifest.
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}