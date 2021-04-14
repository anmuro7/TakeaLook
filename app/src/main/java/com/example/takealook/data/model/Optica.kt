package com.example.takealook.data.model

import com.google.firebase.firestore.GeoPoint

data class Optica(var Nombre:String="",
                  var CIF:String="",
                  var RazonSocial:String="",
                  var Direccion:String="",
                  var CP:String="",
                  var Localidad:String="",
                  var Telefono:String="",
                  var Email:String="",
                  var Web:String="",
                  var verify:Boolean=false,
                  var coordenadas: GeoPoint? = null,
                  var opticaId:String?=null
                  ) {

}