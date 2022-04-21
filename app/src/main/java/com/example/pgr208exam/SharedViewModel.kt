package com.example.pgr208exam

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    var list: MutableLiveData<ArrayList<String>> = MutableLiveData();
    private var responseFromPost: MutableLiveData<CharSequence> = MutableLiveData<CharSequence>()
    var originalImage: MutableLiveData<Bitmap> = MutableLiveData()


    //Getter and setter for the response from POST request
    fun getResponseFromPost(): MutableLiveData<CharSequence> {
        return responseFromPost
    }
    fun changeResponseFromPost(input: String) {
        responseFromPost.value = input;
    }

}
