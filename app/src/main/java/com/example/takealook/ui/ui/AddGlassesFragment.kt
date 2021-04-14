package com.example.takealook.ui.ui

import android.Manifest.permission.CAMERA
import android.Manifest.permission_group.CAMERA
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Color.red
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.takealook.BuildConfig
import com.example.takealook.R
import com.example.takealook.Viewmodel.AddGlassesFragmentViewModel
import com.example.takealook.Viewmodel.MapaOpticasViewModel
import com.example.takealook.Viewmodel.RegisterOpticsViewModel
import com.example.takealook.Viewmodel.ViewModelFactory
import com.example.takealook.data.model.Gafas
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest


class AddGlassesFragment : Fragment() {

    private lateinit var addGlassesFragmentViewModel: AddGlassesFragmentViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var imBtnPhoto: ImageButton
    private lateinit var btnAddGlasses:Button
    private lateinit var imageState: TextView
    lateinit var currentPhotoPath: String
    private lateinit var bitmap: Bitmap
    val REQUEST_TAKE_PHOTO = 1


    //EditText
    private lateinit var editTextNombreDonante:EditText
    private lateinit var editTextApellidosDonante:EditText
    private lateinit var editTextDniDonante:EditText
    private lateinit var editTextTelefonoDonante:EditText
    private lateinit var editTextEmailDonante:EditText
    // check if the photo has been taken
    private  var imageAdded:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_add_glasses, container, false)

        imBtnPhoto = view.findViewById(R.id.imBtn_Camera)
        imageState = view.findViewById(R.id.imageState)
        progressBar = view.findViewById(R.id.progressBarAddGlasses)
        btnAddGlasses=view.findViewById(R.id.btnAddGlasses)

        //find for the views
        editTextNombreDonante=view.findViewById(R.id.editTextNombreDonante)
        editTextApellidosDonante=view.findViewById(R.id.editTextApellidosDonante)
        editTextDniDonante=view.findViewById(R.id.editTextDniDonante)
        editTextTelefonoDonante=view.findViewById(R.id.editTextTelefonoDonante)
        editTextEmailDonante=view.findViewById(R.id.editTextEmailDonante)

        //provide viewmodel
        addGlassesFragmentViewModel = ViewModelProvider(this,
                ViewModelFactory(requireActivity())).get(AddGlassesFragmentViewModel::class.java)

        //check permissions and open camera
        imBtnPhoto.setOnClickListener {
            if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1) {// Marshmallow+
                if (ContextCompat.checkSelfPermission(requireActivity(),
                                android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity()
                                    ,android.Manifest.permission.CAMERA)) {
                    } else {
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA)
                                , REQUEST_TAKE_PHOTO );
                    }
                }else{ //have permissions
                    dispatchTakePictureIntent()
                }
            }else{ // Pre-Marshmallow
                dispatchTakePictureIntent()
            }
        }




        btnAddGlasses.setOnClickListener({
            //check if edittext is empty
            val checkDataDon: Boolean = checkEditText()

            if(checkDataDon){
                //check that the glasses image has been added
                if (imageAdded == true) {
                    //activate the progressbar view to indicate that tasks are being done in the backend
                    progressBar.visibility = View.VISIBLE
                    // with the content of the editext a glasses object is created
                    var gafas: Gafas = Gafas(
                        editTextNombreDonante.text.toString(),
                        editTextApellidosDonante.text.toString(),
                        editTextDniDonante.text.toString(),
                        editTextTelefonoDonante.text.toString(),
                        editTextEmailDonante.text.toString()
                    )
                    //send data to viewmodel
                    addGlassesFragmentViewModel.addDataOfGlasses(gafas, bitmap)
                        .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                            requireActivity().getWindow().setFlags(
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                            if (it) {
                                clearEditText()
                                requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                                progressBar.visibility = View.INVISIBLE
                                val fragmentManager = requireActivity().supportFragmentManager
                                val transaction = fragmentManager?.beginTransaction()
                                val fragment: StockGlasses = StockGlasses()
                                transaction?.replace(R.id.frameLayout_user, fragment)
                                transaction?.addToBackStack(null)
                                transaction?.commit()
                                requireActivity().setTitle("Mi stock")
                            } else {
                                Toast.makeText(
                                    requireActivity(),
                                    "Error en la subida",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })

                } else {
                    imageState.text="Por favor añada una imagen de la donación!"
                    imageState.setTextColor(Color.RED)
                }
            }


        })


        // Inflate the layout for this fragment
        return view

    }

    private fun checkEditText():Boolean {
        var check:Boolean=false
        if(editTextNombreDonante.text.isEmpty()){
            editTextNombreDonante.setError("Introduzca el nombre del donante")
            editTextNombreDonante.requestFocus()
            check =false
        }else if(editTextApellidosDonante.text.isEmpty()){
            editTextNombreDonante.setError("Introduzca el nombre del donante")
            editTextNombreDonante.requestFocus()
            check =false
        }else if(editTextDniDonante.text.isEmpty()){
            editTextDniDonante.setError("Introduzca el DNI del donante")
            editTextDniDonante.requestFocus()
            check=false
        }else if(editTextEmailDonante.text.isEmpty()){
            editTextEmailDonante.setError("Introduzca el email del donante")
            editTextEmailDonante.requestFocus()
        }else   if(editTextTelefonoDonante.text.isEmpty()){
            editTextTelefonoDonante.setError("Introduzca el teléfono del donante")
            editTextTelefonoDonante.requestFocus()
            check=false
        }else{
            check=true
        }

        return check

    }

    private fun clearEditText() {
        editTextNombreDonante.text.clear()
        editTextApellidosDonante.text.clear()
        editTextDniDonante.text.clear()
        editTextTelefonoDonante.text.clear()
        editTextEmailDonante.text.clear()

    }




    @SuppressLint("ResourceAsColor")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_TAKE_PHOTO) {
            bitmap=BitmapFactory.decodeFile(currentPhotoPath)
            imageState.setTextColor(R.color.color4)
            imageState.text="Imagen guardada correctamente"
            imageAdded=true
            super.onActivityResult(requestCode, resultCode, data)
        }else{
            imageState.text="Error, intente de nuevo"
            imageAdded=false

        }

    }
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            requireActivity(),
                            BuildConfig.APPLICATION_ID + ".provider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
}










