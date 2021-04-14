package com.example.takealook.Viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.takealook.data.Repository

class RecoverPasswordViewModel(private val repository: Repository): ViewModel() {

    fun recoverPass(email:String):MutableLiveData<Boolean>{
        return repository.recoverPassword(email)
    }
}