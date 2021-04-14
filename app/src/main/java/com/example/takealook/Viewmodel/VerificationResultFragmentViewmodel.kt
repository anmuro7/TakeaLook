package com.example.takealook.Viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.takealook.data.Repository
import com.example.takealook.data.model.Receptores

class VerificationResultFragmentViewmodel(private val repository: Repository):ViewModel() {

    fun getReceptorData(codeUser:String?):MutableLiveData<Receptores>{
        return repository.getReceptorVerified(codeUser)
    }


}