package com.smb.smbapplication.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.smb.smbapplication.AppExecutors

import com.smb.smbapplication.R
import com.smb.smbapplication.common.autoCleared
import com.smb.smbapplication.data.model.TypeOption
import com.smb.smbapplication.data.model.User
import com.smb.smbapplication.databinding.FragmentLoginBinding
import com.smb.smbapplication.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

private const val TAG: String = "LoginFragment"

/**
 * A simple [Fragment] subclass.
 *
 */
class LoginFragment : BaseFragment<FragmentLoginBinding>() ,AdapterView.OnItemSelectedListener{


    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var mViewModel: LoginViewModel
    private var statusList: ArrayList<TypeOption> ?=null
    private var adapterValues by autoCleared<SubSpinnerAdapter>()


    override fun getLayoutId(): Int {
        return R.layout.fragment_login;
    }

    var list: ArrayList<User>? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel = getViewModel(LoginViewModel::class.java)

        activity!!.window.statusBarColor = ContextCompat.getColor(activity!!, R.color.colorAppGreen)

        adapterValues = SubSpinnerAdapter(requireActivity())
        sp_val_cat?.adapter = adapterValues
        btn_sign_up?.setOnClickListener { sp_val_cat?.performClick() }
        sp_val_cat?.onItemSelectedListener = this

        statusList = ArrayList<TypeOption>().apply {
            add(0, TypeOption("1","Option one"))
            add(1,TypeOption("2","Option two"))
            add(2,TypeOption("3","Option three"))
        }

        adapterValues.setList(statusList)

        btn_login?.setOnClickListener {
            if (btn_sign_up.tag==null){
                showSnackBar("Please chose one Options")
            }else{
            val bundle = bundleOf("type" to btn_sign_up.tag.toString())
            findNavController().navigate(R.id.showRegistration,bundle)}
        }


    }

    class SubSpinnerAdapter(val context: Context, private var listItemsTxt: ArrayList<TypeOption> = ArrayList()) : BaseAdapter() {

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
            vh.label.text = listItemsTxt[position].OptionName
            return view
        }

        fun setList(list: ArrayList<TypeOption>?) {
            listItemsTxt.clear()
            if (list != null) {
                listItemsTxt.addAll(list)
            }
            notifyDataSetChanged()
        }

        override fun getItem(position: Int): String? {

            return listItemsTxt[position].OptionName

        }
        fun serviceID(position: Int):String? {
            return listItemsTxt[position].optionId.toString()
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

            sp_val_cat.id -> {
                btn_sign_up.text = adapterValues.getItem(position)!!
                btn_sign_up.tag = adapterValues.serviceID(position)


            }

        }
    }
}
