package com.example.takealook.ui.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.takealook.R

class InformationUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //change theme to Apptheme
        setTheme(R.style.AppTheme)
        this.setTitle("Información importante")
        setContentView(R.layout.activity_information_user)

        var btnColaboradores:Button=findViewById(R.id.bntVerColaboradores)

        btnColaboradores.setOnClickListener({
            val intent = Intent(this, MapaOpticasActivity::class.java)
            startActivity(intent)
            finish()
        })
    }
}