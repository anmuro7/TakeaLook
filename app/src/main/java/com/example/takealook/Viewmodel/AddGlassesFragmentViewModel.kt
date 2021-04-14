package com.example.takealook.Viewmodel

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.takealook.data.Repository
import com.example.takealook.data.model.Gafas

class AddGlassesFragmentViewModel(private val repository: Repository):ViewModel() {

    public fun addDataOfGlasses(gafas: Gafas,bitmap: Bitmap):MutableLiveData<Boolean>{
       return repository.addNewGlasses(gafas,bitmap)
    }


}