package space.pixelsg.myalbum

import android.app.Application
import com.google.android.material.color.DynamicColors
import space.pixelsg.myalbum.data.DataModule

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        DynamicColors.applyToActivitiesIfAvailable(this)

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .dataModule(DataModule())
            .build()
    }
}