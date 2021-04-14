package com.example.takealook.Viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.takealook.data.Repository
import com.example.takealook.data.model.Gafas

class DeliveryGlassesFragmentViewModel(private val repository: Repository): ViewModel() {

    fun addDonateToFirestore(verifyCode:String, idOptica: String?, idGlasses: String?,price:Int):MutableLiveData<Boolean>{
        return repository.addDonationToFirestore(verifyCode,idOptica,idGlasses,price)
    }
    fun verifyReceptor(code:String): MutableLiveData<Boolean> {
        return repository.verifyUser(code)
    }

}