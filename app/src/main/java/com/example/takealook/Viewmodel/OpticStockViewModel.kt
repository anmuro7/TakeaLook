package com.example.takealook.Viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.takealook.data.Repository
import com.example.takealook.data.model.Gafas

class OpticStockViewModel(private val repository: Repository):ViewModel() {

    fun opticGlasses(optic_id:String):MutableLiveData<List<Gafas>>{
        return repository.getGlassesData(optic_id)
    }
    fun currentUser(): String?{
        return repository.currentUser()
    }
}