package com.smb.smbapplication.ui.login



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController

import com.smb.smbapplication.R
import com.smb.smbapplication.binding.FragmentDataBindingComponent
import com.smb.smbapplication.common.autoCleared
import com.smb.smbapplication.databinding.FragmentRegistrationStepOneBinding
import com.smb.smbapplication.databinding.FragmentRegistrationStepTwoBinding
import com.smb.smbapplication.di.Injectable
import com.smb.smbapplication.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_registration_step_two.*
import org.jetbrains.anko.support.v4.intentFor

private const val TAG = "RegistrationStepTwoFragment"

/**
 * A simple [Fragment] subclass.
 *
 */
class RegistrationStepTwoFragment : BaseFragment<FragmentRegistrationStepTwoBinding>(), Injectable {

    override fun getLayoutId()=R.layout.fragment_registration_step_two

    var binding by autoCleared<FragmentRegistrationStepTwoBinding>()



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                this@RegistrationStepTwoFragment.onBack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedCallback)


        imgBack?.setOnClickListener {
            onBack()
        }

    }
    fun onBack()  {

        startActivity(intentFor<LoginActivity>())
        activity?.finishAffinity()

    }
}
