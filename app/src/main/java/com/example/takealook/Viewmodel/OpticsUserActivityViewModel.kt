package com.example.takealook.Viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.takealook.data.Repository
import com.google.firebase.auth.FirebaseUser

class OpticsUserActivityViewModel(private val repository: Repository):ViewModel() {

    fun currentUser():String?{
        return repository.currentUser()
    }
    fun logout() {
        return repository.logout()
    }
}