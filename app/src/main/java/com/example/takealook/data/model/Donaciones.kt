package com.example.takealook.data.model

import java.util.*

data class Donaciones (
        var nombreDonante:String="",
        var apellidosDonante:String="",
        var nombreReceptor:String="",
        var apellidosReceptor:String="",
        var telefonoDonante:String="",
        var telefonoReceptor:String="",
        var emailDonante:String="",
        var emailReceptor:String="",
        var idGafas:String="",
        var fechaEntrega:String="",
        var importeAcordado:Int=0,
        var id_donacion:String=""
        ) {
}