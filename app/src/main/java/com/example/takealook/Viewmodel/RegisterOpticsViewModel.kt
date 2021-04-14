package com.example.takealook.Viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.takealook.data.Repository
import com.google.firebase.firestore.GeoPoint

class RegisterOpticsViewModel(private val repository: Repository): ViewModel() {

    fun createNewUser(user:String, password:String,nombre:String,razonSocial:String,cif:String,
                      direccion:String,cp:String,localidad:String,
                      telefono:String,web:String,geoPoint: GeoPoint):LiveData<Boolean>{
        return repository.createNewUser(user,password,nombre,razonSocial,cif,direccion,cp,localidad,telefono,web, geoPoint)
    }

}