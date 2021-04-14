package com.example.takealook.Viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.takealook.data.Repository
import com.google.firebase.auth.FirebaseUser

class LoginViewModel(private val repository: Repository): ViewModel() {

    fun currentUser(): String?{
        return repository.currentUser()
    }

    fun login(user:String,password:String):LiveData<Boolean>{
        return repository.loginFirebase(user,password)
    }
}