package com.example.pgr208exam

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    var list: MutableLiveData<ArrayList<String>> = MutableLiveData();
    private var responseFromPost: MutableLiveData<CharSequence> = MutableLiveData<CharSequence>()

    fun getResponseFromPost(): MutableLiveData<CharSequence> {
        return responseFromPost
    }
    fun changeResponseFromPost(input: String) {
        responseFromPost.value = input;
    }

    fun changeList(input: ArrayList<String>) {
        list.value = input;
    }

    fun getList(): LiveData<ArrayList<String>> {
        return list
    }
}
