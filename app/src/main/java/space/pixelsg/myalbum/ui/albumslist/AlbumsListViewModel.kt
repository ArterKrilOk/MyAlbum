package space.pixelsg.myalbum.ui.albumslist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import space.pixelsg.myalbum.data.models.Album
import space.pixelsg.myalbum.data.models.AlbumRequest
import space.pixelsg.myalbum.data.models.exceptions.EmptyQueryException
import space.pixelsg.myalbum.utils.ConnectionLiveData
import space.pixelsg.myalbum.utils.Resource
import space.pixelsg.myalbum.vm.AppViewModel

class AlbumsListViewModel(application: Application) : AppViewModel(application) {
    private var albumRequest: AlbumRequest = AlbumRequest(String(), 1)
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var isLoading: Boolean = false

    val searchQueryText: BehaviorSubject<String> = BehaviorSubject.createDefault(String())
    val connectionLiveData: ConnectionLiveData
    val albumsLiveData: MutableLiveData<Resource<List<Album>>> = MutableLiveData()

    init {
        connectionLiveData = ConnectionLiveData(application)

        compositeDisposable.add(
            searchQueryText.subscribe {
                albumRequest = AlbumRequest(it, 1)
                getAlbums()
            }
        )
    }

    fun getAlbums() {
        //return if already loading
        if (isLoading)
            return
        //return if album request is invalid
        if (albumRequest.isNotValid()) {
            isLoading = false
            albumsLiveData.postValue(Resource.error(EmptyQueryException(), null))
            return
        }

        musicRepository
            .getAlbums(albumRequest)
            .subscribe(
                object : Observer<List<Album>> {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                        albumsLiveData.postValue(Resource.loading(null))
                        isLoading = true
                    }

                    override fun onNext(t: List<Album>) {
                        albumsLiveData.postValue(Resource.success(t))
                    }

                    override fun onError(e: Throwable) {
                        albumsLiveData.postValue(Resource.error(e, null))
                        isLoading = false
                    }

                    override fun onComplete() {
                        albumRequest.page++
                        isLoading = false
                    }

                }
            )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}