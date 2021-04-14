package com.example.takealook.ui.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.takealook.R
import com.example.takealook.Viewmodel.MapaOpticasViewModel
import com.example.takealook.Viewmodel.RecoverPasswordViewModel
import com.example.takealook.Viewmodel.ViewModelFactory

class RecoverPasswordActivity : AppCompatActivity() {

    private lateinit var emailEditText:EditText
    private lateinit var btnRecoverPass: Button
    private lateinit var recoverPasswordViewModel:RecoverPasswordViewModel
    private lateinit var progressBarRecoverPass: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {//change theme to Apptheme
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)
        this.setTitle("Recuperar contraseña")
        //find views
        emailEditText=findViewById(R.id.editTextRecoverEmailPassword)
        btnRecoverPass=findViewById(R.id.btnRecoverPass)
        progressBarRecoverPass=findViewById(R.id.progressBarRecoverPass)
        //hide progressbar
        progressBarRecoverPass.visibility=View.INVISIBLE

        recoverPasswordViewModel = ViewModelProvider(this, ViewModelFactory(this))
                .get(RecoverPasswordViewModel::class.java)

        btnRecoverPass.setOnClickListener({
            //check EditText
            if (!emailEditText.text.toString().isEmpty()) {
                progressBarRecoverPass.visibility=View.VISIBLE
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                //call function to recover password on backend
                recoverPasswordViewModel.recoverPass(emailEditText.text.toString())
                    .observe(this, Observer { recover ->
                        progressBarRecoverPass.visibility=View.VISIBLE
                        hideKeyboard()
                        if (recover) {
                            progressBarRecoverPass.visibility = View.INVISIBLE
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                            //informs user to next action
                            val alertDialogBuilder = AlertDialog.Builder(this)
                            alertDialogBuilder.setTitle("Recuperar contraseña")
                            alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
                                //go to login activity
                                val intent = Intent(this, LoginOpticsActivity::class.java)
                                startActivity(intent)
                            }
                            alertDialogBuilder . setMessage (R.string.recoverPass)
                            alertDialogBuilder.show()
                        }

                            // val intent = Intent(this, LoginOpticsActivity::class.java)
                           // startActivity(intent)                        }
                        else {
                            //email not located on firebase
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                            progressBarRecoverPass.visibility=View.INVISIBLE
                           emailEditText.setError("Su correo no se ha encontrado en nuestra base de datos")
                           emailEditText.requestFocus()
                        }
                    })
                //empty editText
            } else {
                emailEditText.setError("Debe introducir un email")
                emailEditText.requestFocus()
            }
        })
    }
    //hide keyboard
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}