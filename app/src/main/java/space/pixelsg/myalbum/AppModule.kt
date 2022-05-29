package space.pixelsg.myalbum

import android.content.Context
import androidx.annotation.NonNull
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(
    private val context: Context
) {
    @Singleton
    @Provides
    @NonNull
    fun provideAppContext() : Context = context
}