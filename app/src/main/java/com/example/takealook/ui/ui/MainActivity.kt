package com.example.takealook.ui.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.takealook.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //wait 2,5 seconds to init app
        Thread.sleep(2000)
        //change theme to Apptheme
        setTheme(R.style.AppTheme)
        //hide action bar
        supportActionBar?.hide()
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        goToOpticActivity()
        //find views
        val btn_facebook:ImageView=findViewById(R.id.btn_facebook)
        val btn_instagram:ImageView=findViewById(R.id.btn_instagram)
        val btn_mail:ImageView=findViewById(R.id.btn_mail)
        val btn_twitter:ImageView=findViewById(R.id.btn_twitter)
        val opticsAcces:TextView=findViewById(R.id.opticsAcces)

        //underline optics acces
        opticsAcces.setPaintFlags(opticsAcces.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        //go to Login activity
        opticsAcces.setOnClickListener{
            val loginIntent=Intent(this, LoginOpticsActivity::class.java)
            startActivity(loginIntent)
        }

        //open facebook page
        btn_facebook.setOnClickListener({
            val facebookId = "fb://page/105239938114011"
            val urlPage = "http://www.facebook.com/TAKE-A-LOOK-105239938114011"
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(facebookId)))
            } catch (e: Exception) {
                Log.e("Error", "Aplicación no instalada.")
                //open url of page
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)))
            }
        })

        //open twitter profile
        btn_twitter.setOnClickListener({
            val intent: Intent
            intent = try {
                // Check if the Twitter app is installed on the phone.
                getPackageManager().getPackageInfo("com.twitter.android", 0)
                Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=1335168836174229505"))
            } catch (e: java.lang.Exception) {
                Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/TakeaLo20215993"))
            }
            startActivity(intent)
        })

        //open instagram page
        btn_instagram.setOnClickListener({
            val uri = Uri.parse("http://instagram.com/_u/app_takealook")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.instagram.android")
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                //No app, open web version
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/app_takealook")))
            }
        })

        //send a email
        btn_mail.setOnClickListener {
            val TO = arrayOf("apptakealook@gmail.com") //Direcciones email  a enviar.
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:") // only email apps should handle this

            intent.putExtra(Intent.EXTRA_EMAIL, TO)
            intent.putExtra(Intent.EXTRA_SUBJECT, "Consulta")
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }



 }
//go to maps
    private fun goToOpticActivity(){
        val btn:Button=findViewById(R.id.btn_opticas)
        btn.setOnClickListener {
            val intent = Intent(this, InformationUserActivity::class.java)
            startActivity(intent)
        }

    }




}