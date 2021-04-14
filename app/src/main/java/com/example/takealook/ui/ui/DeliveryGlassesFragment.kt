package com.example.takealook.ui.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.takealook.R
import com.example.takealook.Viewmodel.DeliveryGlassesFragmentViewModel
import com.example.takealook.Viewmodel.MapaOpticasViewModel
import com.example.takealook.Viewmodel.ViewModelFactory
import com.example.takealook.data.model.Gafas


class DeliveryGlassesFragment : Fragment() {


    private var opticId: String? = null
    private var glassesId: String? = null
    private lateinit var codeReceptorEditText:EditText
    private lateinit var priceDonEditText:EditText
    private lateinit var btn_delivery:Button
    private lateinit var deliveryGlassesFragmentViewModel: DeliveryGlassesFragmentViewModel
    private lateinit var progressBar_deliveryGlasses:ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_delivery_glasses, container, false)

        val args = arguments
        arguments?.getString("opticId")?.let {
            opticId = it
        }
        arguments?.getString("glassesId")?.let {
            glassesId = it
        }

        //find views
        codeReceptorEditText=view.findViewById(R.id.codeRecEditText)
        priceDonEditText=view.findViewById(R.id.priceEditText)
        btn_delivery=view.findViewById(R.id.btn_delivery)

        deliveryGlassesFragmentViewModel = ViewModelProvider(this, ViewModelFactory(requireActivity()))
                .get(DeliveryGlassesFragmentViewModel::class.java)
        progressBar_deliveryGlasses= view.findViewById(R.id.progressBar_deliveryGlasses)

        btn_delivery.setOnClickListener({
            //check if EditText is empty
            if (codeReceptorEditText.text.isEmpty()) {
                codeReceptorEditText.setError("Por favor, introduzca el código del receptor")
                codeReceptorEditText.requestFocus()
            } else if (priceDonEditText.text.isEmpty()) {
                priceDonEditText.setError("Por favor, introduzca el importe acordado")
                priceDonEditText.requestFocus()
            } else {
                progressBar_deliveryGlasses.visibility=View.VISIBLE
                //We verify that the receiver has a code assigned
                deliveryGlassesFragmentViewModel.verifyReceptor(codeReceptorEditText.text.toString())
                        .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                            if (it) {
                                // If the code is correct, we will create a new collection '' Donations '' for the optic
                                deliveryGlassesFragmentViewModel.addDonateToFirestore(
                                        codeReceptorEditText.text.toString(),
                                        opticId,
                                        glassesId,
                                        priceDonEditText.text.toString().toInt()
                                ).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                                    if (it) {
                                        progressBar_deliveryGlasses.visibility=View.INVISIBLE
                                        val fragmentManager = requireActivity().supportFragmentManager
                                        val transaction = fragmentManager?.beginTransaction()
                                        val fragment: DonationsOpticFragment = DonationsOpticFragment()
                                        transaction?.replace(R.id.frameLayout_user, fragment)
                                        transaction?.addToBackStack(null)
                                        transaction?.commit()
                                        requireActivity().setTitle("Mis donaciones")
                                    }
                                })
                            } else {
                                // informs the user that the receiver is not verified
                                progressBar_deliveryGlasses.visibility=View.INVISIBLE
                                codeReceptorEditText.setError("El código de usuario introducido no es correcto")
                                codeReceptorEditText.requestFocus()
                            }
                        })
            }
        })
        // Inflate the layout for this fragment
        return view
    }



}