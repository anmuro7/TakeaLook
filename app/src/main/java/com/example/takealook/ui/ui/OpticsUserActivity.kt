package com.example.takealook.ui.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.takealook.R
import com.example.takealook.Viewmodel.LoginViewModel
import com.example.takealook.Viewmodel.MapaOpticasViewModel
import com.example.takealook.Viewmodel.OpticsUserActivityViewModel
import com.example.takealook.Viewmodel.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class OpticsUserActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView:BottomNavigationView
    private lateinit var frameLayout:FrameLayout
    private lateinit var opticsUserActivityViewModel: OpticsUserActivityViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        this.setTitle("Mi stock")
        setContentView(R.layout.activity_optics_user)

        bottomNavigationView=findViewById(R.id.navigationBottom)
        frameLayout=findViewById(R.id.frameLayout_user)
        opticsUserActivityViewModel = ViewModelProvider(this, ViewModelFactory(this))
                .get(OpticsUserActivityViewModel::class.java)

        var stockGlassesFragment:StockGlasses= StockGlasses()
        var verificarFragment:VerifyUserFragment= VerifyUserFragment()
        var addGlassesFragment:AddGlassesFragment= AddGlassesFragment()
        var donationsOpticFragment:DonationsOpticFragment=DonationsOpticFragment()


        setFragment(stockGlassesFragment)

        //actions for navigation bottom menu
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.añadir -> {
                    setFragment(addGlassesFragment)
                    this.setTitle("Añadir nuevas gafas")
                }
                R.id.verificar -> {
                    setFragment(verificarFragment)
                    this.setTitle("Verificar receptor")
                }
                R.id.gafas -> {
                    setFragment(stockGlassesFragment)
                    this.setTitle("Mi stock")
                }
                R.id.delivered -> {
                    setFragment(donationsOpticFragment)
                    this.setTitle("Mis donaciones")

                }
            }
            true
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_appbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //actions for menu on appbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            //optic logout
            R.id.optics_logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Aviso")
                builder.setMessage("" +
                        "¿Está seguro que quiere cerrar sesión?")
                builder.setPositiveButton("Si") { dialog, which ->
                    opticsUserActivityViewModel.logout()
                    val loginIntent=Intent(this, LoginOpticsActivity::class.java)
                    startActivity(loginIntent)
                }
                builder.setNegativeButton("No") { dialog, which -> }
                builder.show()
                
            }
            R.id.closeApp ->{
                //close app
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Aviso")
                builder.setMessage("¿Está seguro que quiere salir de la aplicación?")
                builder.setPositiveButton("Si") { dialog, which ->
                    finishAffinity()
                    System.exit(0)

                }
                builder.setNegativeButton("No") { dialog, which -> }
                builder.show()
            }

        }

        return super.onOptionsItemSelected(item)
    }


    private fun setFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout_user, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }



}