package com.example.takealook.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.takealook.data.model.Donaciones
import com.example.takealook.data.model.Gafas
import com.example.takealook.data.model.Optica
import com.example.takealook.data.model.Receptores
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class Repository (context:Context) {

     //instance of Firestore database
    val db:FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storageReferenence = FirebaseStorage.getInstance().reference

    private val opticList = MutableLiveData<List<Optica>>()
    private val receptorFounded = MutableLiveData<Receptores>()
     //auth Firebase
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
     //newoptics register
    private val newRegistOptics = MutableLiveData<Boolean>()
    private val addedGlassestoStorage:MutableLiveData<Boolean> = MutableLiveData<Boolean>()



    //list of optics
        fun getOpticsData(): MutableLiveData<List<Optica>> {
            val opticListDownloaded= mutableListOf<Optica>()
            db.collection("opticas").get().addOnSuccessListener { results->
            for(documentos in results){
                val optica=documentos.toObject(Optica::class.java)
                opticListDownloaded.add(optica)
            }
                opticList.value=opticListDownloaded
        }
            return opticList
        }



        //return data of the verified receptor
        fun getReceptorVerified(codeUser: String?):MutableLiveData<Receptores>{
            var receptorVerified:Receptores=Receptores()
            db.collection("receptores").get().addOnSuccessListener { receptores->
                for(receptor in receptores){
                    if(receptor.id==codeUser){
                        val receptor:Receptores=receptor.toObject(Receptores::class.java)
                        receptorVerified=receptor
                    }
                }
                receptorFounded.postValue(receptorVerified)
            }
            return receptorFounded
        }


        //
        //verifies that a receptor is in  database
        fun verifyUser(code: String):MutableLiveData<Boolean>{
            var userVerify=MutableLiveData<Boolean>()
            db.collection("receptores").get().addOnSuccessListener { receptores->
                for(receptor in receptores){
                    if(receptor.id==code){
                        userVerify.postValue(true)
                    }else{
                        userVerify.postValue(false)
                    }
                }
            }
            return  userVerify
        }

     //recover password of optics user
    fun recoverPassword(email: String):MutableLiveData<Boolean>{
            mAuth.setLanguageCode("es")
            var recoverPass:MutableLiveData<Boolean> = MutableLiveData()
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener({ task ->
                if (task.isSuccessful) {
                    recoverPass.postValue(true)
                } else {
                    recoverPass.postValue(false)
                }
            })
        return recoverPass
    }

    //Login Firebase user
        fun loginFirebase(user: String, password: String): LiveData<Boolean>{
            val auth = MutableLiveData<Boolean>()
                //pass user and password to Firebase
                mAuth.signInWithEmailAndPassword(user, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                auth.postValue(true)
                            } else {
                                auth.postValue(false)
                            }
                        }
        return auth
    }

    //close Firebase session
    fun logout() = mAuth.signOut()

    //obtains current user connected
    fun currentUser() = mAuth.currentUser?.uid

    //create new optic user in authentication and save data on Firestore
    fun createNewUser(email: String, password: String, nombre: String, razonSocial: String, cif: String,
                      direccion: String, cp: String, localidad: String,
                      telefono: String, web: String, geoPoint: GeoPoint):MutableLiveData<Boolean>
    {
        //create user Authentication
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAuth.currentUser
                    //logout user for 1rst login
                     logout()
                    //add optics data to Firestore
                    if (user != null) {
                        newOpticsRegister(user, nombre, razonSocial, cif, direccion, cp, localidad,
                                telefono, email, web,geoPoint, user.uid)
                    }
                   newRegistOptics.postValue(true)

                } else {
                    // If sign in fails, display a message to the user.
                    newRegistOptics.postValue(false)
                }

                // ...
            }
        return newRegistOptics
    }
     //save data of optics on firestore document
     fun newOpticsRegister(idCollection: FirebaseUser, nombre: String, razonSocial: String, cif: String,
                           direccion: String, cp: String, localidad: String, telefono: String,
                           email: String, web: String, geoPoint: GeoPoint, opticId: String){
         //create hasmap to add data on CloudFirestore
         val optic= hashMapOf(
                 "Nombre" to nombre,
                 "RazonSocial" to razonSocial,
                 "CIF" to cif,
                 "Direccion" to direccion,
                 "CP" to cp,
                 "Localidad" to localidad,
                 "Telefono" to telefono,
                 "Email" to email,
                 "verify" to false,
                 "Web" to web,
                 "coordenadas" to geoPoint,
                 "opticaId" to opticId
         )
         //we use the authentication id to relate the user to their data
         db.collection("opticas").document(idCollection.uid).set(optic)
                 .addOnSuccessListener {
                     Log.d("correcto", "añadido registro a Firebase")
                 }
                 .addOnFailureListener({ e -> Log.e("error", e.toString()) })
     }

     //add new glasses donation to collection and image to Storage
    fun addNewGlasses(gafas: Gafas, bitmap: Bitmap): MutableLiveData<Boolean>{
        //create hasmap to add data on CloudFirestore
        val gafas= hashMapOf(
                "nombreDonante" to gafas.nombreDonante,
                "apellidosDonante" to gafas.apellidosDonante,
                "dniDonante" to gafas.dniDonante,
                "telefonoDonante" to gafas.telefonoDonante,
                "emailDonante" to gafas.emailDonante,
                "id_Gafas" to null
        )
        //path to directory in Firebase Storage
        val ref = storageReferenence.child("gafas/${currentUser()}")

        //we use the authentication id to relate the user to their data
        db.collection("opticas").document(mAuth.currentUser?.uid.toString())
                .collection("gafas").add(gafas).addOnSuccessListener {
                //create file in storage
                val imageRef=ref.child(it.id)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
                val datas: ByteArray = baos.toByteArray()
                //Upload file in  storage
                val uploadTask: UploadTask = imageRef.putBytes(datas)
                uploadTask
                        .addOnFailureListener { exception ->
                    addedGlassestoStorage.postValue(false)
                }
                        .addOnSuccessListener { taskSnapshot ->
                    //if file added to storage, save id in collection Firesotre, update value id_gafas
                    db.collection("opticas").document(mAuth.currentUser?.uid.toString())
                        .collection("gafas").document(it.id).update("id_Gafas", it.id)
                            .addOnSuccessListener {
                                // backend actions completed, return true
                            addedGlassestoStorage.postValue(true)
                        }
                }
                }
                   .addOnFailureListener({ e ->
                       addedGlassestoStorage.postValue(false)
                       Log.e("error", e.toString())
                   })

        return addedGlassestoStorage
    }


//get data of all glasses for optic id
    fun getGlassesData(optics_id: String): MutableLiveData<List<Gafas>> {
        val opticGlasses= mutableListOf<Gafas>()
        var opticGlassesLiveDate=MutableLiveData<List<Gafas>>()
        db.collection("opticas").document(optics_id)
                .collection("gafas").get().addOnSuccessListener { results->
                    for(result in results){
                        val glasses=result.toObject(Gafas::class.java)
                        opticGlasses.add(glasses)
                    }

                    opticGlassesLiveDate.postValue(opticGlasses)

                }
        return opticGlassesLiveDate
    }




     //add donation register to Firestore and Storage
    fun addDonationToFirestore(verifyCode: String, idOptic: String?,
                               idGlasses: String?, price: Int):MutableLiveData<Boolean> {

        val donationAdd = MutableLiveData<Boolean>()
        var glasses: Gafas = Gafas()
        var receptor: Receptores = Receptores()

        if (idOptic != null) {
            if (idGlasses != null) {
                //find glases to donate
                db.collection("opticas").document(idOptic)
                        .collection("gafas")
                        .document(idGlasses).get().addOnSuccessListener {
                            //save glasses
                            glasses = it.toObject(Gafas::class.java)!!
                            //save receptor data
                            db.collection("receptores").document(verifyCode)
                                    .get().addOnSuccessListener {
                                receptor = it.toObject(Receptores::class.java)!!
                                //save date
                                val sdf = SimpleDateFormat("dd/M/yyyy")
                                val currentDate = sdf.format(Date())

                                //create a new object Donación to save in new collection
                                val donacion = hashMapOf(
                                        "nombreDonante" to glasses.nombreDonante,
                                        "apellidosDonante" to glasses.apellidosDonante,
                                        "nombreReceptor" to receptor.nombre,
                                        "apellidosReceptor" to receptor.apellidos,
                                        "telefonoDonante" to glasses.telefonoDonante,
                                        "telefonoReceptor" to receptor.telefono,
                                        "emailDonante" to glasses.emailDonante,
                                        "emailReceptor" to receptor.email,
                                        "idGafas" to idGlasses,
                                        "fechaEntrega" to currentDate,
                                        "importeAcordado" to price
                                )

                                //Add the donation data and remove the data and image from the optic stock
                                db.collection("opticas").document(idOptic)
                                        .collection("donaciones").add(donacion)
                                        .addOnSuccessListener ({donacion->
                                            //update cloud firestore with id of donation
                                            db.collection("opticas").document(idOptic)
                                                    .collection("donaciones").document(donacion.id)
                                                    .update("id_donacion", donacion.id).addOnSuccessListener{
                                                        Log.d("Campo actualizado","OK!")
                                                    }
                                            //We delete the data from the optics glasses and the Storage image
                                            db.collection("opticas").document(idOptic).collection("gafas")
                                                    .document(idGlasses).delete().addOnSuccessListener {
                                                        //download the image of the glasses from the optics directory
                                                        val ref = storageReferenence.child("gafas/$idOptic")
                                                        val imageRef = ref.child(idGlasses)
                                                        //save the image and upload to the directory donations for this optic
                                                        imageRef.getBytes(1024 * 1024).addOnSuccessListener {

                                                            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                                                            val newRef = storageReferenence.child("donaciones/$idOptic")
                                                            val newImageRef = newRef.child(idGlasses)
                                                            val baos = ByteArrayOutputStream()
                                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
                                                            val datas: ByteArray = baos.toByteArray()
                                                            val uploadTask: UploadTask = newImageRef.putBytes(datas)

                                                            uploadTask.addOnFailureListener { exception ->
                                                                Log.e("error", exception.toString())
                                                            }.addOnSuccessListener { taskSnapshot ->
                                                                imageRef.delete().addOnSuccessListener {
                                                                    //When the process is finished, notify to view
                                                                    donationAdd.postValue(true)
                                                                }
                                                            }.addOnFailureListener({ e ->
                                                                        Log.e("error", e.toString())
                                                                    })

                                                        }
                                                    }


                                        })
                            }.addOnFailureListener({ e ->
                                Log.e("error", e.toString())
                                donationAdd.postValue(false)
                            })
                       }
                                                    }
        }
        return donationAdd
    }

     //return list of donation with id optic
    fun getOpticDonations(opticId:String) : MutableLiveData<List<Donaciones>>{
        val opticDonations= mutableListOf<Donaciones>()
        var opticDonationsLiveData=MutableLiveData<List<Donaciones>>()
        db.collection("opticas").document(opticId)
                .collection("donaciones").get().addOnSuccessListener { results->
                    for(documentos in results){
                        Log.d("Resultados",documentos.toString())
                        val donation:Donaciones=documentos.toObject(Donaciones::class.java)
                        opticDonations.add(donation)
                    }

                    opticDonationsLiveData.postValue(opticDonations)

                }
        return opticDonationsLiveData

    }

}




