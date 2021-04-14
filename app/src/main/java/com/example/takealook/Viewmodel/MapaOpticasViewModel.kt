package com.example.takealook.Viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.takealook.data.Repository
import com.example.takealook.data.model.Optica


class MapaOpticasViewModel(private val repository: Repository): ViewModel() {

    fun loadOptics(): LiveData<List<Optica>?> {
        return repository.getOpticsData()
    }
}