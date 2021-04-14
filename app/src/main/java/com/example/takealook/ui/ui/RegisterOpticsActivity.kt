package com.example.takealook.ui.ui

import android.app.Application
import android.content.DialogInterface
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.takealook.R
import com.example.takealook.Viewmodel.LoginViewModel
import com.example.takealook.Viewmodel.MapaOpticasViewModel
import com.example.takealook.Viewmodel.RegisterOpticsViewModel
import com.example.takealook.Viewmodel.ViewModelFactory
import com.google.firebase.firestore.GeoPoint
import java.io.IOException
import java.util.*

class RegisterOpticsActivity : AppCompatActivity() {

    private lateinit var registerOpticsViewModel: RegisterOpticsViewModel
    private lateinit var nameEditText:EditText
    private lateinit var cifEditText:EditText
    private lateinit var direccionEditText:EditText
    private lateinit var cpEditText:EditText
    private lateinit var localidadEditText:EditText
    private lateinit var razonSocialEditText:EditText
    private lateinit var webEditText:EditText
    private lateinit var telefonoEditText:EditText
    private lateinit var emailEditText:EditText
    private lateinit var editTextPassword:EditText
    private lateinit var progressBarRegister: ProgressBar
    private lateinit var listAddress:List<Address>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setTitle("Registro nueva óptica")
        //change theme to Apptheme
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_register_optics)

        //provider viewmodel
        registerOpticsViewModel = ViewModelProvider(this, ViewModelFactory(this))
                .get(RegisterOpticsViewModel::class.java)

        //find views
         nameEditText=findViewById(R.id.editTextNombreComercial)
         razonSocialEditText=findViewById(R.id.editTextRazonSocial)
         cifEditText=findViewById(R.id.editTextCif)
         direccionEditText=findViewById(R.id.editTextDireccionOptica)
         cpEditText=findViewById(R.id.editTextCP)
         localidadEditText=findViewById(R.id.editTextLocalidadOptica)
         webEditText=findViewById(R.id.editTextWeb)
         telefonoEditText=findViewById(R.id.editTextPhone)
         emailEditText=findViewById(R.id.editTextEmailAddress)
         editTextPassword=findViewById(R.id.editTextPassword)
        progressBarRegister=findViewById(R.id.progressBarRegister)
        //hide progressbar
        progressBarRegister.visibility= View.INVISIBLE

        //enable register button based on the state of checkbox
        var checkBox:CheckBox=findViewById(R.id.checkBoxPrivacidad)
        var btnRegister:Button=findViewById(R.id.buttonRegisterOptics)

        //alert dialogh with privacy policy
        var privacypolicy:TextView=findViewById(R.id.privacitypolicy)
        privacypolicy.setOnClickListener({
             val alertDialogBuilder = AlertDialog.Builder(this)
             alertDialogBuilder.setTitle("POLITICA DE PRIVACIDAD")
             alertDialogBuilder.setPositiveButton("OK", null)
             alertDialogBuilder.setMessage(R.string.politicaprivacidad)
             alertDialogBuilder.show()
         })

        //button disabled until the user marks checkbox
        btnRegister.isEnabled=false
        checkBox.setOnClickListener({
            if(checkBox.isChecked){
                btnRegister.isEnabled=true
            }else{
                btnRegister.isEnabled=false
            }
        })

        btnRegister.setOnClickListener({
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            if (checkEditText()) {
                progressBarRegister.visibility = View.VISIBLE
                //convert the address into a geopoint
                var geoPointoptic: GeoPoint? = geoLocateAdress(direccionEditText.text.toString(), cpEditText.text.toString(),
                        localidadEditText.text.toString())
                if (geoPointoptic != null) {
                    //pass data to backend
                    registerOpticsViewModel.createNewUser(
                            emailEditText.text.toString(),
                            editTextPassword.text.toString(),
                            nameEditText.text.toString(),
                            razonSocialEditText.text.toString(),
                            cifEditText.text.toString(),
                            direccionEditText.text.toString(),
                            cpEditText.text.toString(),
                            localidadEditText.text.toString(),
                            telefonoEditText.text.toString(),
                            webEditText.text.toString(), geoPointoptic
                    ).observe(
                            this, Observer {register->
                                if (register) {
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                                    progressBarRegister.visibility = View.INVISIBLE
                                    //go to login activity
                                    var intent: Intent = Intent(this, LoginOpticsActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    //register error
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                                    progressBarRegister.visibility = View.INVISIBLE
                                    Toast.makeText(this,
                                            "Se ha producido un error, intente de nuevo el registro",
                                            Toast.LENGTH_SHORT).show()
                                }

                            })
                }
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })



    }
    //convert optics direction to coordinates
   fun checkEditText():Boolean{


       var verify:Boolean=false

       if(nameEditText.text.isEmpty()){
           nameEditText.setError("Por favor introduzca el nombre de su establecimiento")
           nameEditText.requestFocus()
       }else if( razonSocialEditText.text.isEmpty()){
           razonSocialEditText.setError("Por favor introduzca su Razón Social")
           razonSocialEditText.requestFocus()
       }else if(cifEditText.text.isEmpty()) {
           cifEditText.setError("Por favor introduzca su CIF")
           cifEditText.requestFocus()
       }else if(direccionEditText.text.isEmpty()){
           direccionEditText.setError("Por favor, introduzca la dirección de su establecimiento")
           direccionEditText.requestFocus()
       }else if(cpEditText.text.isEmpty()){
           cpEditText.setError("Por favor, introduzca el Código Postal")
           cpEditText.requestFocus()
       }else if(localidadEditText.text.isEmpty()){
           localidadEditText.setError("Por favor, introduzca la localidad")
           localidadEditText.requestFocus()
       }else if(telefonoEditText.text.isEmpty()){
           telefonoEditText.setError("Pr favor, introduzca un teléfono de contacto")
           telefonoEditText.requestFocus()
       }else if(webEditText.text.isEmpty()){
           webEditText.setError("Por favor, introduzca su página web")
           webEditText.requestFocus()
       }else if(emailEditText.text.isEmpty()){
           emailEditText.setError("Por favor, introduzca su email")
           emailEditText.requestFocus()
       }else if(editTextPassword.text.isEmpty()){
           editTextPassword.setError("Por favor introduzca su contraseña de acceso")
           editTextPassword.requestFocus()
       }else{
           verify=true
       }

        return verify
   }

    //convert optics direction to coordinates
    fun geoLocateAdress(direccion: String, cp: String, localidad: String): GeoPoint? {
        var geoPoint: GeoPoint? =null
        var geocoder= Geocoder(this, Locale.getDefault())
        try {
            listAddress=geocoder.getFromLocationName(
                    "$direccion, $cp,$localidad", 1)
        }
        catch (e: IOException) {Log.e("error", "error function")}
        if (listAddress.size >0){
            Log.d("coordenadas", "${listAddress.get(0).latitude},${listAddress.get(0).longitude}")
            geoPoint= GeoPoint(listAddress.get(0).latitude, listAddress.get(0).longitude)

        }

        return geoPoint
    }

}

