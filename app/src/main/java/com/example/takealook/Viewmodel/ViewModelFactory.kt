package com.example.takealook.Viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.takealook.data.Repository


class ViewModelFactory(private val context: Context) :
        ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MapaOpticasViewModel::class.java -> MapaOpticasViewModel(
                Repository(context)
        ) as T
        AddGlassesFragmentViewModel::class.java -> AddGlassesFragmentViewModel(
                Repository(context)
        ) as T
        LoginViewModel::class.java -> LoginViewModel(
                Repository(context)
        ) as T
        OpticStockViewModel::class.java -> OpticStockViewModel(
                Repository(context)
        ) as T
        AddGlassesFragmentViewModel::class.java -> AddGlassesFragmentViewModel(
                Repository(context)
        ) as T
        VerifyUserFragmentViewModel::class.java -> VerifyUserFragmentViewModel(
                Repository(context)
        ) as T
        VerificationResultFragmentViewmodel::class.java -> VerificationResultFragmentViewmodel(
                Repository(context)
        ) as T
        DonationsOpticFragmentViewModel::class.java -> DonationsOpticFragmentViewModel(
                Repository(context)
        ) as T
        DeliveryGlassesFragmentViewModel::class.java -> DeliveryGlassesFragmentViewModel(
                Repository(context)
        ) as T
        OpticsUserActivityViewModel::class.java -> OpticsUserActivityViewModel(
                Repository(context)
        ) as T
        RecoverPasswordViewModel::class.java -> RecoverPasswordViewModel(
                Repository(context)
        ) as T
        RegisterOpticsViewModel::class.java -> RegisterOpticsViewModel(
                Repository(context)
        ) as T

        else -> throw RuntimeException("No viewModel found")
    }
}