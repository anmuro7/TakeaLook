package com.example.takealook.ui.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.takealook.R
import com.example.takealook.Viewmodel.DonationsOpticFragmentViewModel
import com.example.takealook.Viewmodel.MapaOpticasViewModel
import com.example.takealook.Viewmodel.ViewModelFactory
import com.example.takealook.data.model.Donaciones
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso


class DonationsOpticFragment : Fragment() {

    private lateinit var donationsRecyclerView:RecyclerView
    private lateinit var progressBar_donations:ProgressBar
    private lateinit var donationsOpticFragmentViewModel: DonationsOpticFragmentViewModel
    private lateinit var opticId:String
    private lateinit var noDonationsIcon:ImageView
    private lateinit var nodonations:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View=inflater.inflate(R.layout.fragment_donations_optic, container, false)

        donationsRecyclerView=view.findViewById(R.id.donationsRecyclerView)
        progressBar_donations=view.findViewById(R.id.progressBar_donations)
        noDonationsIcon=view.findViewById(R.id.noDonationsIcon)
        nodonations=view.findViewById(R.id.nodonations)

























































































































































































































































        


        val layoutManager = LinearLayoutManager(requireActivity())
        donationsRecyclerView.layoutManager = layoutManager
        val dividerItemDecoration: DividerItemDecoration = DividerItemDecoration(
                requireActivity(),
                layoutManager.orientation
        )
        donationsRecyclerView.addItemDecoration(dividerItemDecoration)

        donationsOpticFragmentViewModel = ViewModelProvider(this,
                ViewModelFactory(requireActivity())).get(DonationsOpticFragmentViewModel::class.java)

        opticId = donationsOpticFragmentViewModel.currentUser().toString()

        donationsOpticFragmentViewModel.getOpticDonations(opticId).observe(
                viewLifecycleOwner,
                Observer { donations ->
                    progressBar_donations.visibility = View.VISIBLE
                    if (donations.isEmpty()) {
                        //if there are no donations
                        progressBar_donations.visibility = View.INVISIBLE
                        nodonations.text = "Todavía no ha realizado donaciones"
                        noDonationsIcon.visibility = View.VISIBLE
                        nodonations.visibility = View.VISIBLE
                    } else {
                        //if there are donations in Firestore
                        val adapter: AdapterDonationsOptic = AdapterDonationsOptic(
                                donations,
                                opticId,
                                requireActivity(),
                                progressBar_donations
                        )
                        donationsRecyclerView.adapter = adapter
                    }
                })

        return view
    }


}

/*-------------------------------------------------Adapter Class---------------------------*/
class AdapterDonationsOptic(
        private val donations: List<Donaciones>,
        private val opticId: String,
        private val context: Context,
        private val progressBar: ProgressBar
) :
    RecyclerView.Adapter<AdapterDonationsOptic.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {

        val  view = LayoutInflater.from(parent.context).inflate(
                R.layout.donations_recyclerview_item,
                parent,
                false
        )
        return MyViewHolder(view!!)
    }


    override fun getItemCount(): Int {
        return donations.size
    }
    inner class MyViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        //find views of recyclerview item
        val imageDonation: ImageView =mView.findViewById(R.id.imageDonation)
        val titleRefDonation:TextView=mView.findViewById(R.id.titleRefDonation)
        val refDonation:TextView=mView.findViewById(R.id.refDonation)
        val titleDateDonation:TextView=mView.findViewById(R.id.titleDateDonation)
        val dateDonation:TextView=mView.findViewById(R.id.dateDonation)
        val titleDonorName:TextView=mView.findViewById(R.id.titleDonorName)
        val donorName:TextView=mView.findViewById(R.id.donorName)
        val titleReceptorName:TextView=mView.findViewById(R.id.titleReceptorName)
        val receptorName:TextView=mView.findViewById(R.id.receptorName)
        val more:TextView=mView.findViewById(R.id.more)


    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //hide the views until we have placed the data in the views
        holder.imageDonation.visibility=View.INVISIBLE
        holder.titleRefDonation.visibility=View.INVISIBLE
        holder.refDonation.visibility=View.INVISIBLE
        holder.titleDateDonation.visibility=View.INVISIBLE
        holder.dateDonation.visibility=View.INVISIBLE
        holder.titleDonorName.visibility=View.INVISIBLE
        holder.donorName.visibility=View.INVISIBLE
        holder.titleReceptorName.visibility=View.INVISIBLE
        holder.receptorName.visibility=View.INVISIBLE
        holder.more.visibility=View.INVISIBLE

        val idGlasses:String=donations.get(position).idGafas
        //Firebase Storage reference
        val storageRef = Firebase.storage.reference
        val rutaStorage=storageRef.child("donaciones/$opticId/$idGlasses")
        //download image from Storage
        rutaStorage.downloadUrl.addOnSuccessListener {
            //add image downloaded
            Glide.with(context)
                    .load(it)
                    .override(400, 800)
                    .fitCenter()
                    .into(holder.imageDonation)

            //put data in fields
            holder.donorName.text="${donations.get(position).nombreDonante} ${donations.get(position).apellidosDonante}"
            holder.receptorName.text="${donations.get(position).nombreReceptor} ${donations.get(
                    position
            ).apellidosReceptor}"
            holder.dateDonation.text="${donations.get(position).fechaEntrega}"
            holder.refDonation.text="${donations.get(position).id_donacion}"



            //show views
            holder.imageDonation.visibility=View.VISIBLE
            holder.titleRefDonation.visibility=View.VISIBLE
            holder.refDonation.visibility=View.VISIBLE
            holder.titleDateDonation.visibility=View.VISIBLE
            holder.dateDonation.visibility=View.VISIBLE
            holder.titleDonorName.visibility=View.VISIBLE
            holder.donorName.visibility=View.VISIBLE
            holder.titleReceptorName.visibility=View.VISIBLE
            holder.receptorName.visibility=View.VISIBLE
            holder.more.visibility=View.VISIBLE

            //hide progressbar
            progressBar.visibility=View.INVISIBLE

            //image enlarged
            holder.imageDonation.setOnClickListener({
                val builder = AlertDialog.Builder(context)

                val li = LayoutInflater.from(context)
                val promptsView: View = li.inflate(R.layout.custom_alertdialog_image_glasses, null)
                val alertDialogBuilder = AlertDialog.Builder(context)
                alertDialogBuilder.setView(promptsView)

                val imageViewGlasses = promptsView.findViewById<View>(R.id.glasses_image_alertdialog) as ImageView
                val closeDialog = promptsView.findViewById<View>(R.id.close_dialog)

                val idGlasses: String = donations.get(position).idGafas

                val storageRef = Firebase.storage.reference
                val rutaStorage = storageRef.child("donaciones/$opticId/$idGlasses")

                rutaStorage.downloadUrl.addOnSuccessListener {
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

            })

        }.addOnFailureListener({
            progressBar.visibility = View.INVISIBLE
            Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
        })


        //alertDialog with more info of Donations
        holder.more.setOnClickListener({
            val builderinfo = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val view: View = inflater.inflate(R.layout.moreinfodonations_alertdialog, null)
            val builderDialog = AlertDialog.Builder(context)
            builderDialog.setView(view)


            //find views in alertdialog
            val refDonation = view.findViewById<View>(R.id.ref_donation) as TextView
            val nameDonor = view.findViewById<View>(R.id.nameDonante) as TextView
            val phoneDonante = view.findViewById<View>(R.id.phoneDonante) as TextView
            val emailDonante = view.findViewById<View>(R.id.emailDonante) as TextView
            val nameReceptorDialog = view.findViewById<View>(R.id.nameReceptorDialog) as TextView
            val phoneReceptorDialog = view.findViewById<View>(R.id.phoneReceptorDialog) as TextView
            val emailReceptorDialog = view.findViewById<View>(R.id.emailReceptorDialog) as TextView
            val fechadeentrega = view.findViewById<View>(R.id.fechadeentrega) as TextView
            val importeacordado = view.findViewById<View>(R.id.importeacordado) as TextView
            val idGafas = view.findViewById<View>(R.id.idGafas) as TextView

            refDonation.text="Referencia: ${donations.get(position).id_donacion}"
            nameDonor.text="Nombre: ${donations.get(position).nombreDonante} ${donations.get(position).apellidosDonante}"
            phoneDonante.text="Teléfono: ${donations.get(position).telefonoDonante}"
            emailDonante.text="Email: ${donations.get(position).emailDonante}"
            nameReceptorDialog.text="Nombre: ${donations.get(position).nombreReceptor} " +
                    "${donations.get(position).apellidosReceptor}"
            phoneReceptorDialog.text="Teléfono: ${donations.get(position).telefonoReceptor}"
            emailReceptorDialog.text="Email: ${donations.get(position).emailReceptor}"
            fechadeentrega.text="Fecha de entrega:${donations.get(position).fechaEntrega}"
            importeacordado.text="Importe: ${donations.get(position).importeAcordado} €"
            idGafas.text="Referencia gafas: ${donations.get(position).idGafas}"
            // create alert dialog
            val dialog = builderDialog.create()
            dialog.show()
        })



    }

}