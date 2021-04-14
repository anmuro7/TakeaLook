package com.example.takealook.ui.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.takealook.R
import com.example.takealook.Viewmodel.MapaOpticasViewModel
import com.example.takealook.Viewmodel.VerificationResultFragmentViewmodel
import com.example.takealook.Viewmodel.VerifyUserFragmentViewModel
import com.example.takealook.Viewmodel.ViewModelFactory


class VerificationResultFragment : Fragment() {

    private lateinit var verificationResultFragmentViewmodel: VerificationResultFragmentViewmodel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view:View=inflater.inflate(R.layout.fragment_verification_result, container, false)
        var codeUser: String? = arguments?.getString("userCode","no data")

        //find views
        var textViewResult:TextView=view.findViewById(R.id.resultVerify)
        var textViewNameReceptor:TextView=view.findViewById(R.id.nameReceptor)
        var textViewDniReceptor:TextView=view.findViewById(R.id.dniReceptor)
        var textViewPhoneReceptor:TextView=view.findViewById(R.id.phoneReceptor)
        var textViewEmailReceptor:TextView=view.findViewById(R.id.emailReceptor)
        var imageIconVerify:ImageView=view.findViewById(R.id.verfied)

        //hide views, wait for the data to show
        textViewResult.visibility=View.INVISIBLE
        textViewNameReceptor.visibility=View.INVISIBLE
        textViewDniReceptor.visibility=View.INVISIBLE
        textViewPhoneReceptor.visibility=View.INVISIBLE
        textViewEmailReceptor.visibility=View.INVISIBLE
        imageIconVerify.visibility=View.INVISIBLE


        verificationResultFragmentViewmodel = ViewModelProvider(this, ViewModelFactory(requireActivity()))
                .get(VerificationResultFragmentViewmodel::class.java)


        verificationResultFragmentViewmodel.getReceptorData(codeUser).observe(viewLifecycleOwner,Observer{receptor->
            if(receptor!=null){
                //show views
                textViewResult.visibility=View.VISIBLE
                textViewNameReceptor.visibility=View.VISIBLE
                textViewDniReceptor.visibility=View.VISIBLE
                textViewPhoneReceptor.visibility=View.VISIBLE
                textViewEmailReceptor.visibility=View.VISIBLE
                imageIconVerify.visibility=View.VISIBLE

                textViewResult.text="Código $codeUser verificado"
                textViewNameReceptor.text="Nombre: ${receptor.nombre} ${receptor.apellidos}"
                textViewDniReceptor.text="DNI: ${receptor.dni}"
                textViewPhoneReceptor.text="Teléfono: ${receptor.telefono}"
                textViewEmailReceptor.text="Email: ${receptor.email}"
            }
        })
        //go to stock optic fragment
        var btnContinue: Button =view.findViewById(R.id.btnContinueVerify)
        btnContinue.setOnClickListener({
            val fragmentManager = activity?.supportFragmentManager
            val transaction = fragmentManager?.beginTransaction()
            val fragment:StockGlasses=StockGlasses()
            transaction?.replace(R.id.frameLayout_user, fragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
            requireActivity().setTitle("Mi stock")
        })
        // Inflate the layout for this fragment
        return view
    }

}