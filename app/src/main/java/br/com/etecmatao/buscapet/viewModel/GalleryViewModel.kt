package br.com.etecmatao.buscapet.viewModel

import android.app.Application
import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.etecmatao.buscapet.dto.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryViewModel(application: Application): AndroidViewModel(application){
    val images: MutableLiveData<List<Image>> = MutableLiveData()

    fun loadImages() = viewModelScope.launch(Dispatchers.IO) {
        val projection =  arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA
        )

        val selection = "";
        val selectionArgs = arrayOf<String>()
        val sortOder = "${MediaStore.Images.Media.DATE_ADDED} ASC"

        val contentResolver = getApplication<Application>().contentResolver

        val imagesFound = mutableListOf<Image>()

        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

            while(cursor.moveToNext()){
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(displayNameColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                imagesFound.add(Image(contentUri, id.toString(), name))
            }
        }

        images.postValue(imagesFound)
    }

}