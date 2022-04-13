package com.example.pgr208exam

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
     var list: MutableLiveData<ArrayList<String>> = MutableLiveData();
    var testString: String = "This is a test string";

    fun changeString(input: String){

    }

    fun changeList(input: ArrayList<String>){
        list.value = input;
    }
    fun getList(): LiveData<ArrayList<String>> {
        return list
    }

}