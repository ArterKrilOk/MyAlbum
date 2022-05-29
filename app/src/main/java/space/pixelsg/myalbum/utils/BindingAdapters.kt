package space.pixelsg.myalbum.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.subjects.BehaviorSubject
import space.pixelsg.myalbum.data.models.Image
import space.pixelsg.myalbum.ui.view.SquareImageView

object BindingAdapters {

    @BindingAdapter("rxEditText")
    @JvmStatic
    fun bindTextObserver(textInputEditText: TextInputEditText, behaviorSubject: BehaviorSubject<String>) {
        textInputEditText.setText(behaviorSubject.value)

        textInputEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                behaviorSubject.onNext(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    @BindingAdapter("rxSearchView")
    @JvmStatic
    fun bindSearchObserver(searchView: SearchView, behaviorSubject: BehaviorSubject<String>) {
        searchView.setQuery(behaviorSubject.value, false)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                behaviorSubject.onNext(query ?: String())

                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if(query.isNullOrEmpty() || query.isBlank())
                    behaviorSubject.onNext(query ?: String())
                return true
            }

        })
    }

    @BindingAdapter("android:visibility")
    @JvmStatic
    fun bindVisibility(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    @BindingAdapter("imageData")
    @JvmStatic
    fun bindImage(imageView: SquareImageView, image: Image) {
        Glide
            .with(imageView)
            .load(image.full)
            .thumbnail(
                Glide
                    .with(imageView)
                    .load(image.thumb)
                    .centerCrop()
            )
            .centerCrop()
            .into(imageView)
    }
}