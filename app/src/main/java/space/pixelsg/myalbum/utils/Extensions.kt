package space.pixelsg.myalbum.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text
import space.pixelsg.myalbum.data.models.Album
import java.util.regex.Pattern


object Extensions {
    fun String.isPhoneCorrect() : Boolean {
        val number = this.replace(Regex("[\\D]"),"")
        return number.length == 11
    }
    fun String.isEmailCorrect(): Boolean {
        val regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" +
                "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"
        return Pattern.compile(regexPattern)
            .matcher(this)
            .matches()
    }

    @JvmStatic
    fun dateTextWatcher() : TextWatcher = object : TextWatcher {
        private var removing: Boolean = false
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            if (s == null)return
            if(after > 0) {
                removing = false
            }
            if(count > 0) {
                removing = true
            }
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(it: Editable?) {
            if (it != null) {
                if (it.length == 2 && it[0].isDigit() && it[1].isDigit() && !removing)
                    it.append(".")
                if(it.length == 5 && it[3].isDigit() && it[4].isDigit() && !removing)
                    it.append(".")
                if(it.length > 10)
                    it.delete(10,12)
            }
        }
    }

    @JvmStatic
    fun makeSnackOnTop(view: View, text: Int, duration: Int) : Snackbar {
        val snackbar = Snackbar.make(view, text, duration)

        val params = snackbar.view.layoutParams
        if (params is CoordinatorLayout.LayoutParams) {
            params.gravity = Gravity.TOP
        } else {
            (params as FrameLayout.LayoutParams).gravity = Gravity.TOP
        }
        snackbar.view.layoutParams = params

        return snackbar
    }

    fun List<Album>.diffCallback(newAlbums: List<Album>) : DiffUtil.Callback {
        return object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = this@diffCallback.size
            override fun getNewListSize(): Int = newAlbums.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                this@diffCallback[oldItemPosition].albumId == newAlbums[newItemPosition].albumId

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                this@diffCallback[oldItemPosition] == newAlbums[newItemPosition]
        }
    }
}