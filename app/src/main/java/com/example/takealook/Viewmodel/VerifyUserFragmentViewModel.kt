package com.example.takealook.Viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.takealook.data.Repository

class VerifyUserFragmentViewModel(private val repository: Repository): ViewModel() {

    fun verifyReceptor(code:String): MutableLiveData<Boolean> {
        return repository.verifyUser(code)
    }
}