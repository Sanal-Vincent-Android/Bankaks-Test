package com.smb.smbapplication.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.smb.smbapplication.common.AbsentLiveData
import com.smb.smbapplication.data.api.BaseResponse
import com.smb.smbapplication.data.api.Resource
import com.smb.smbapplication.data.model.BillData
import com.smb.smbapplication.data.model.User
import com.smb.smbapplication.repo.UMSRepository
import javax.inject.Inject

class LoginViewModel
@Inject constructor( repoRepository: UMSRepository) : ViewModel() {
    /*** For Login ***/
    private val loginLiveData = MutableLiveData<HashMap<String, String>>()

    fun login(data: HashMap<String, String>?) {
        loginLiveData.value = data
    }

    val loginLiveDataRepo: LiveData<Resource<BaseResponse<BillData>>> =
            Transformations.switchMap(loginLiveData) { data ->
                if (data == null) {
                    AbsentLiveData.create()
                } else {
                    repoRepository.login(data)
                }
            }
    /**********/
}