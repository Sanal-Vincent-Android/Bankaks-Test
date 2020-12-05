package com.smb.smbapplication.ui.login


import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.smb.smbapplication.R
import com.smb.smbapplication.common.autoCleared
import com.smb.smbapplication.data.api.Status
import com.smb.smbapplication.data.model.ModelView
import com.smb.smbapplication.data.model.TypeOption
import com.smb.smbapplication.data.model.Values
import com.smb.smbapplication.databinding.FragmentRegistrationStepOneBinding
import com.smb.smbapplication.di.Injectable
import com.smb.smbapplication.ui.BaseFragment
import com.smb.smbapplication.utils.AppConstants
import com.smb.smbapplication.utils.logger.Log
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_registration_step_one.*
import org.jetbrains.anko.padding
import java.util.regex.Pattern


private const val TAG = "RegistrationStepOneFragment"


class RegistrationStepOneFragment : BaseFragment<FragmentRegistrationStepOneBinding>(), Injectable,AdapterView.OnItemSelectedListener {

    override fun getLayoutId()= R.layout.fragment_registration_step_one
    lateinit var mViewModel: LoginViewModel
    var addValidation : ArrayList<ModelView>?=null
    var appCompatEditText:AppCompatEditText?=null
    private var statusList: ArrayList<Values> ?=null
    private var adapterValues by autoCleared<SubSpinnerAdapter>()

    var binding by autoCleared<FragmentRegistrationStepOneBinding>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = getViewModel(LoginViewModel::class.java)
        activity!!.window.statusBarColor = ContextCompat.getColor(activity!!, R.color.colorWhite)
        addValidation = ArrayList()



                val data = HashMap<String, String>()
                data["type"] = arguments?.getString("type").toString()

                mViewModel.login(data)


            mViewModel.loginLiveDataRepo.removeObservers(this)
            mViewModel.loginLiveDataRepo.observe(requireActivity(), Observer { response ->
                if (response == null || response.status == Status.LOADING) {
                    return@Observer
                }

                Log.e("REsponse",response.toString())

                when {
                    response.data == null -> {


                    }

                    response.data.status -> {

                        val textTitle = AppCompatTextView(activity!!)
                        textTitle.textSize = 20f
                        textTitle.padding = 100
                        textTitle.text = response.data.result?.screenTitle
                        lnMain.addView(textTitle)

                        response.data.result?.fields?.forEach {
                        if (it.uiType?.type.equals("textfield"))
                            it.placeholder?.let { it1 -> it.regex?.let { it2 -> it.servicetype?.data_type?.let { it3 -> it.hintText?.let { it4 -> createTextField(it1, it2, it3, it4) } } } }
                            else{
                            it.placeholder?.let { it1 -> it.regex?.let { it2 -> it.servicetype?.data_type?.let { it3 -> it.hintText?.let { it4 -> dropTile(it1, it2, it3, it4) } } } }
                            adapterValues = SubSpinnerAdapter(requireActivity())
                            sp_val?.adapter = adapterValues

                            statusList = it.uiType?.values

                            adapterValues.setList(statusList)

                        }
                        }
                        val btn = AppCompatButton(activity!!).apply {
                            textSize = 20f
                            padding = 50

                            this.text = "Go"
                        }

                        btn.setOnClickListener {

                            findNavController().navigate(R.id.registrationStepTwo)
                        }

                        lnMain.addView(btn)
                    }

                    else -> {


                    }
                }

                mViewModel.login(null)

            })

        }

    private fun createTextField(textData :String,regex: String,data : String,hintText:String){
        val textInputLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        val textInputLayout = activity?.let { TextInputLayout(it) }



        val textTitle = TextInputEditText(activity!!).apply {
            textSize = 20f
            padding = 50

            this.hint = hintText
            this.setText(textData)

        }
        when(data){
            "string" -> textTitle.inputType = InputType.TYPE_CLASS_TEXT
            "int" -> textTitle.inputType = InputType.TYPE_CLASS_NUMBER
            else -> textTitle.inputType = InputType.TYPE_CLASS_TEXT
        }
        textInputLayout?.addView(textTitle, textInputLayoutParams);
        addValidation(textTitle,regex,textData)
        lnMain.addView(textInputLayout)
        pgLoading.visibility = View.GONE

    }

    private fun dropTile(textData :String,regex: String,data : String,hintText:String){


        val textInputLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        val textInputLayout = activity?.let { TextInputLayout(it) }



        val appCompatEditText = TextInputEditText(activity!!).apply {
            textSize = 20f
            padding = 50
            this.isFocusable = false
            this.hint = hintText
            this.setText(textData)

        }
        textInputLayout?.addView(appCompatEditText, textInputLayoutParams);
        lnMain.addView(textInputLayout)
        pgLoading.visibility = View.GONE
        addValidation(appCompatEditText,regex,textData)
        appCompatEditText?.setOnClickListener { sp_val?.performClick() }
        sp_val?.onItemSelectedListener = this


    }

    private fun addValidation(view: TextInputEditText, regex: String ,type : String){
        addValidation?.add(ModelView(view,regex,type))

    }

    class SubSpinnerAdapter(val context: Context, private var listItemsTxt: ArrayList<Values> = ArrayList()) : BaseAdapter() {

        private val mInflater: LayoutInflater = LayoutInflater.from(context)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            val vh: ItemRowHolder
            if (convertView == null) {
                view = mInflater.inflate(R.layout.item_type, parent, false)
                vh = ItemRowHolder(view)
                view?.tag = vh
            } else {
                view = convertView
                vh = view.tag as ItemRowHolder
            }
            vh.label.text = listItemsTxt[position].name
            return view
        }

        fun setList(list: ArrayList<Values>?) {
            listItemsTxt.clear()
            if (list != null) {
                listItemsTxt.addAll(list)
            }
            notifyDataSetChanged()
        }

        override fun getItem(position: Int): String? {

            return listItemsTxt[position].name

        }
        fun serviceID(position: Int):String? {
            return listItemsTxt[position].id.toString()
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return listItemsTxt.size
        }
        private class ItemRowHolder(row: View?) {

            val label: TextView = row?.findViewById(R.id.txt_name) as TextView

        }
    }
    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {

            sp_val.id -> {
                appCompatEditText?.setText(adapterValues.getItem(position))



            }

        }
    }
    }





