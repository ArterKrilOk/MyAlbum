package space.pixelsg.myalbum.ui.albumdetails

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import space.pixelsg.myalbum.data.models.Album
import space.pixelsg.myalbum.data.models.AlbumDetails
import space.pixelsg.myalbum.utils.ConnectionLiveData
import space.pixelsg.myalbum.utils.Resource
import space.pixelsg.myalbum.vm.AppViewModel

class AlbumDetailsViewModel(application: Application) : AppViewModel(application) {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    val connectionLiveData: ConnectionLiveData
    val albumDetailsLiveData: MutableLiveData<Resource<AlbumDetails>> = MutableLiveData()

    init {
        connectionLiveData = ConnectionLiveData(application)
    }

    fun getAlbumDetails(album: Album) {
        musicRepository
            .getAlbumDetails(album)
            .subscribe(object : Observer<AlbumDetails> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                    albumDetailsLiveData.postValue(Resource.loading(null))
                }

                override fun onNext(t: AlbumDetails) {
                    albumDetailsLiveData.postValue(Resource.success(t))
                }

                override fun onError(e: Throwable) {
                    albumDetailsLiveData.postValue(Resource.error(e, null))
                }

                override fun onComplete() {

                }
            })
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}