package com.example.takealook.ui.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.takealook.R
import com.example.takealook.Viewmodel.MapaOpticasViewModel
import com.example.takealook.Viewmodel.VerifyUserFragmentViewModel
import com.example.takealook.Viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar


class VerifyUserFragment : Fragment() {

 private lateinit var verifyUserFragmentViewModel: VerifyUserFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val rootView:View = inflater.inflate(R.layout.fragment_verify_user, container, false)
        //find views
        val btnVerify:Button=rootView.findViewById(R.id.btnverify)
        val editTextCodeVerification:EditText=rootView.findViewById(R.id.editTextVerificationCode)
        val progressBar_verify:ProgressBar=rootView.findViewById(R.id.progressBar_verify)

         verifyUserFragmentViewModel= ViewModelProvider(this,
                ViewModelFactory(requireActivity())).get(VerifyUserFragmentViewModel::class.java)

        btnVerify.setOnClickListener({
            progressBar_verify.visibility=View.VISIBLE
            var verificationCode: String = editTextCodeVerification.text.toString()
            //check in the backend if user is verified
            verifyUserFragmentViewModel.verifyReceptor(verificationCode).observe(viewLifecycleOwner, Observer { verify ->
                if (verify) {
                    //pass user code and change fragment to result of verification
                    var bundle: Bundle = Bundle()
                    bundle.putString("userCode", verificationCode)
                    var resultFragment: VerificationResultFragment = VerificationResultFragment()
                    resultFragment.arguments = bundle
                    setFragment(resultFragment)
                    progressBar_verify.visibility=View.INVISIBLE
                } else {
                    //user don't verified
                    progressBar_verify.visibility=View.INVISIBLE
                    editTextCodeVerification.setError("El usuario introducido no está verificado")
                    editTextCodeVerification.requestFocus()

                }
            })
        })
        return rootView
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.frameLayout_user, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
        requireActivity().setTitle("Resultado verificación")
    }

}


