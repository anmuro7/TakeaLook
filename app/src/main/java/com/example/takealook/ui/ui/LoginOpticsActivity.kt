package com.example.takealook.ui.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.takealook.R
import com.example.takealook.Viewmodel.LoginViewModel
import com.example.takealook.Viewmodel.MapaOpticasViewModel
import com.example.takealook.Viewmodel.ViewModelFactory

class LoginOpticsActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var progressBar:ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //change theme to Apptheme
        setTheme(R.style.AppTheme)
        this.setTitle("Login ópticas")
        setContentView(R.layout.activity_login_optics)

        loginViewModel = ViewModelProvider(this, ViewModelFactory(this)).get(LoginViewModel::class.java)
        progressBar=findViewById(R.id.progressBar_login)
        progressBar.visibility= View.INVISIBLE

            //if getCurrentUser in Firebase, go to optics Profile
            if(loginViewModel.currentUser()!=null){
                var intentOpticProfile = Intent(this, OpticsUserActivity::class.java)
                startActivity(intentOpticProfile)
            }

        val btnLogin:Button=findViewById(R.id.loginButton)
        val btnRegisterOptics:Button=findViewById(R.id.button_registro)
        val forgetPassword:TextView=findViewById(R.id.forgetPassword)

        val emailTextView:EditText=findViewById(R.id.editTextEmailLogin)
        val passwordEditText:EditText=findViewById(R.id.editTextPasswordLogin)

        btnLogin.setOnClickListener({
            //first check EditText flieds
            if(emailTextView.text.toString().isEmpty()){
                emailTextView.setError("Por favor introduzca su email de usuario")
                emailTextView.requestFocus()
            }else if(passwordEditText.text.toString().isEmpty()){
                        passwordEditText.setError("Por favor, introduzca su contraseña")
                        passwordEditText.requestFocus()
            } else{
                //pass login data to backend
                getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                progressBar.visibility= View.VISIBLE
               loginViewModel.login(emailTextView.text.toString(), passwordEditText.text.toString()).
                observe(this, Observer {
                    //success, go to user activity
                    if (it) {
                        progressBar.visibility= View.INVISIBLE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        var intentOpticProfile = Intent(this, OpticsUserActivity::class.java)
                        startActivity(intentOpticProfile)
                    } else {
                        //informs user of the error
                        progressBar.visibility= View.INVISIBLE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        Toast.makeText(this, "Datos de acceso incorrectos", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        })

        //go to register optics activity
        btnRegisterOptics.setOnClickListener({
            var intentRegister = Intent(this, RegisterOpticsActivity::class.java)
            startActivity(intentRegister)
        })
        //go to recover password activity
        forgetPassword.setOnClickListener({
            var intentRecoverPass = Intent(this, RecoverPasswordActivity::class.java)
            startActivity(intentRecoverPass)
        })

    }


}