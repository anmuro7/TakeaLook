package com.example.takealook.ui.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.takealook.R
import com.example.takealook.Viewmodel.MapaOpticasViewModel
import com.example.takealook.Viewmodel.OpticStockViewModel
import com.example.takealook.Viewmodel.ViewModelFactory
import com.example.takealook.data.model.Gafas
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.w3c.dom.Text

class StockGlasses : Fragment() {

    private lateinit var opticStockViewModel: OpticStockViewModel
    private lateinit var recylcerViewGlassesFrag:RecyclerView
    private lateinit var  opticId: String
    private lateinit var progressBarOpticsGlasses:ProgressBar
    private lateinit var noStockTextView:TextView
    private lateinit var icon_notGlassesOpticsStock:ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view:View=inflater.inflate(R.layout.fragment_stock_glasses, container, false)
        opticStockViewModel = ViewModelProvider(this, ViewModelFactory(requireActivity()))
                .get(OpticStockViewModel::class.java)
        //save current user
        opticId= opticStockViewModel.currentUser().toString()

        //find views
        noStockTextView=view.findViewById(R.id.noStockTextView)
        recylcerViewGlassesFrag=view.findViewById(R.id.opticsstock_recyclerview)
        progressBarOpticsGlasses=view.findViewById(R.id.progressBarOpticStock)
        icon_notGlassesOpticsStock=view.findViewById(R.id.icon_notGlassesOpticsStock)

        //create layaoutmanager to recyclerview
        val layoutManager = LinearLayoutManager(requireActivity())
        recylcerViewGlassesFrag.layoutManager = layoutManager
        //add divider decoration to recyclerview
        val dividerItemDecoration: DividerItemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        recylcerViewGlassesFrag.addItemDecoration(dividerItemDecoration)


        // check that there is a current user
        if(opticId!=null){
            //ask Firestore for a list of the optical donation stock
           opticStockViewModel.opticGlasses(opticId).observe(
                viewLifecycleOwner,
                Observer { glasses ->
                    //view for list without data
                    if (glasses.isEmpty()) {
                        progressBarOpticsGlasses.visibility=View.INVISIBLE
                        noStockTextView.text="No hay stock de donaciones disponibles"
                        icon_notGlassesOpticsStock.visibility=View.VISIBLE

                    }else{
                        // if there is stock, we put the data in the recyclerview with the adapter
                        val adapter: AdapterStockOpticsGlasses = AdapterStockOpticsGlasses(
                            glasses,
                            opticId,
                            requireActivity(),
                            progressBarOpticsGlasses
                        )
                        recylcerViewGlassesFrag.adapter=adapter

                    }

                })
        }

        // Inflate the layout for this fragment
        return view
    }


}


//adapter class
class AdapterStockOpticsGlasses(private val glasses: List<Gafas>, private val opticId: String, private val context: Context,
    private val progressBar: ProgressBar) : RecyclerView.Adapter<AdapterStockOpticsGlasses.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        //inflate template for adapter
        val  view = LayoutInflater.from(parent.context).inflate(
            R.layout.opticstock_recyclerview_item,
            parent,
            false
        )
        return MyViewHolder(view!!)
    }


    override fun getItemCount(): Int {
        return glasses.size
    }

    inner class MyViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    //find views
        val mGlassesImageView: ImageView =mView.findViewById(R.id.imageStockOptics)
        val refGlasses: TextView =mView.findViewById(R.id.refglasses)
        val btnDelivered: TextView =mView.findViewById(R.id.btn_delivered)
        val referenceTitle:TextView=mView.findViewById(R.id.referenceTextView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //hide views until has data
        holder.mGlassesImageView.visibility=View.INVISIBLE
        holder.refGlasses.visibility=View.INVISIBLE
        holder.btnDelivered.visibility=View.INVISIBLE
        holder.referenceTitle.visibility=View.INVISIBLE

        //obtains id_glasses for this position of list
        val idGlasses:String=glasses.get(position).id_Gafas

        //obtains reference of storage
        val storageRef = Firebase.storage.reference
        val rutaStorage=storageRef.child("gafas/$opticId/$idGlasses")
        //download the image corresponding to the id
        rutaStorage.downloadUrl.addOnSuccessListener {
            //add image from Storage
            Glide.with(context)
                .load(it)
                .override(400, 800)
                .fitCenter()
                .into(holder.mGlassesImageView)

            //put data on views
            holder.refGlasses.text=idGlasses
            holder.mGlassesImageView.visibility=View.VISIBLE
            holder.refGlasses.visibility=View.VISIBLE
            holder.btnDelivered.visibility=View.VISIBLE
            holder.referenceTitle.visibility=View.VISIBLE

            //hide progressbar
            progressBar.visibility=View.INVISIBLE

            holder.btnDelivered.setOnClickListener {
                //pass data for optic and glasses to delivery fragment
                val deliveryGlassesFragment=DeliveryGlassesFragment()
                val arguments = Bundle()
                arguments.putString("opticId", opticId)
                arguments.putString("glassesId",idGlasses)
                deliveryGlassesFragment.arguments=arguments
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout_user,deliveryGlassesFragment)
                        .addToBackStack(null)
                        .commit()
                //change title on AppBar
                context.setTitle("Entrega donación")
            }

            // enlarge image thumbnail
            holder.mGlassesImageView.setOnClickListener {
                //inflate layaout for alert dialog
                val li = LayoutInflater.from(context)
                val promptsView: View = li.inflate(R.layout.custom_alertdialog_image_glasses, null)
                val alertDialogBuilder = AlertDialog.Builder(context)
                alertDialogBuilder.setView(promptsView)

                //find views
                val imageViewGlasses = promptsView.findViewById<View>(R.id.glasses_image_alertdialog) as ImageView
                val closeDialog = promptsView.findViewById<View>(R.id.close_dialog)
                //download the image and resize
                rutaStorage.downloadUrl.addOnSuccessListener{
                    Glide.with(context)
                        .load(it)
                        .override(1000,2000)
                        .into(imageViewGlasses)
                }
                // create alert dialog
                val alertDialog = alertDialogBuilder.create()
                // show it
                alertDialog.show()
                closeDialog.setOnClickListener({
                    alertDialog.hide()
                })

            }
        }.addOnFailureListener({
            progressBar.visibility=View.INVISIBLE
            Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
        })
    }

}