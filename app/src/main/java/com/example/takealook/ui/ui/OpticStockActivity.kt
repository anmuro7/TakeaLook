package com.example.takealook.ui.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore.Images
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.takealook.R
import com.example.takealook.Viewmodel.OpticStockViewModel
import com.example.takealook.Viewmodel.ViewModelFactory
import com.example.takealook.data.model.Gafas
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


class OpticStockActivity : AppCompatActivity() {

    private lateinit var opticStockViewModel: OpticStockViewModel
    private lateinit var recylcerViewGlasses:RecyclerView
    private lateinit var progressBar_opticsStock:ProgressBar
    private lateinit var icon_notGlasses:ImageView
    private lateinit var textView_infonotglasses:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        //get data of the intent
        val optic_id = intent.extras!!.getString("opticID")
        val opticName=intent.extras!!.getString("opticName")
        this.setTitle("$opticName")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_optic_stock)
        opticStockViewModel = ViewModelProvider(this, ViewModelFactory(this))
                .get(OpticStockViewModel::class.java)
        //recyclerview, put layoutmanager and item decoration
        recylcerViewGlasses=findViewById(R.id.recyclerView_Glasses)
        progressBar_opticsStock=findViewById(R.id.progressBar_opticsStock)
        progressBar_opticsStock.visibility=View.INVISIBLE
        val layoutManager = LinearLayoutManager(this)

        recylcerViewGlasses.layoutManager = layoutManager
       val dividerItemDecoration: DividerItemDecoration = DividerItemDecoration(
               this, layoutManager.orientation)
        recylcerViewGlasses.addItemDecoration(dividerItemDecoration)

        icon_notGlasses=findViewById(R.id.icon_notGlasses)
        textView_infonotglasses=findViewById(R.id.textView_infonotglasses)

    //permissions to share images of glasses
        if (Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1) {// Marshmallow+
            if (ContextCompat.checkSelfPermission(applicationContext,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this
                                ,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 2 );
                }
            }
        }


        if (optic_id != null) {
            opticStockViewModel.opticGlasses(optic_id).observe(this, Observer { glasses ->
                //view for empty list
                    if (glasses.isEmpty()) {
                    progressBar_opticsStock.visibility = View.INVISIBLE
                    icon_notGlasses.visibility = View.VISIBLE
                    textView_infonotglasses.visibility = View.VISIBLE

                } else {
                    //put data with adapter in recyclerview
                    val adapter: MyAdapterOpticGlasses = MyAdapterOpticGlasses(
                            glasses,
                            optic_id,
                            this,
                            progressBar_opticsStock
                    )
                    progressBar_opticsStock.visibility = View.VISIBLE
                    recylcerViewGlasses.visibility = View.VISIBLE
                    recylcerViewGlasses.adapter = adapter
                }


            })
        }

      
    }
}



/*-------------------------adapter class-----------------------------*/

class MyAdapterOpticGlasses(
        private val glassesList: List<Gafas>,
        private val opticId: String,
        private val context: Context,
        private val progressBar: ProgressBar
) :
    RecyclerView.Adapter<MyAdapterOpticGlasses.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.recyclerview_glasses_item,
                parent,
                false
        )
        return MyViewHolder(view!!)
    }


    override fun onBindViewHolder(holder: MyAdapterOpticGlasses.MyViewHolder, position: Int) {

        //hide views
        holder.mGlassesImageView.visibility = View.INVISIBLE
        holder.mNameDonanteTextView.visibility = View.INVISIBLE
        holder.title_donante.visibility = View.INVISIBLE
        holder.glasses_id.visibility = View.INVISIBLE
        holder.shareButton.visibility = View.INVISIBLE



        val idGlasses: String = glassesList.get(position).id_Gafas
        val storageRef = Firebase.storage.reference
        val rutaStorage = storageRef.child("gafas/$opticId/$idGlasses")
        rutaStorage.downloadUrl.addOnSuccessListener {

            //add image from Storage
            Glide.with(context)
                    .load(it)
                    // .override(500, 1000)
                    .fitCenter()
                    .into(holder.mGlassesImageView)
            holder.mNameDonanteTextView.text = "${glassesList.get(position).nombreDonante}" +
                    " ${glassesList.get(position).apellidosDonante}"
            holder.glasses_id.text = "Ref. gafas: $idGlasses"
            progressBar.visibility = View.INVISIBLE
            //show views
            holder.mGlassesImageView.visibility = View.VISIBLE
            holder.mNameDonanteTextView.visibility = View.VISIBLE
            holder.title_donante.visibility = View.VISIBLE
            holder.glasses_id.visibility = View.VISIBLE
            holder.shareButton.visibility = View.VISIBLE

            //share image glasses
            holder.shareButton.setOnClickListener {
               val bmap: Bitmap = holder.mGlassesImageView.drawable.toBitmap()
                val share:Intent= Intent(Intent.ACTION_SEND)
                share.type = "image/*"
                share.putExtra(Intent.EXTRA_TEXT,"!Mira las gafas que he visto en Take a Look!")
                share.putExtra(Intent.EXTRA_STREAM, getImageUri(context, bmap))
                startActivity(context,share,null)

            }

        }

    }

    override fun getItemCount(): Int {
        return glassesList.size
    }

    inner class MyViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mGlassesImageView: ImageView = mView.findViewById(R.id.glasses_imageview)
        val mNameDonanteTextView: TextView = mView.findViewById(R.id.textView_nombre_donante)
        val title_donante: TextView = mView.findViewById(R.id.title_donante)
        val glasses_id: TextView = mView.findViewById(R.id.glasses_id)
        val shareButton: ImageButton = mView.findViewById(R.id.shareImage)


    }


    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

}







