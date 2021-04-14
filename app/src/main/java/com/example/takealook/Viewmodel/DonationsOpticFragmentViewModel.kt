package com.example.takealook.Viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.takealook.data.Repository
import com.example.takealook.data.model.Donaciones

class DonationsOpticFragmentViewModel(private val repository: Repository):ViewModel() {

    fun currentUser(): String? {
        return repository.currentUser()
    }
    fun getOpticDonations(opticId:String):MutableLiveData<List<Donaciones>>{
        return repository.getOpticDonations(opticId)
    }

}