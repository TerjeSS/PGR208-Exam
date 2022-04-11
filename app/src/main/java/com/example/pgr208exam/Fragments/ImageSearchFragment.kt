package com.example.pgr208exam.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.pgr208exam.Constants
import com.example.pgr208exam.ItemAdapter
import com.example.pgr208exam.R
import org.json.JSONObject


class ImageSearchFragment : Fragment() {

    val dummyData = Constants.getDummyData()
    val googleURL: String = "http://api-edu.gtl.ai/api/v1/imagesearch/google?url=";
    val bingURL: String = "http://api-edu.gtl.ai/api/v1/imagesearch/bing?url=";
    val tineyeURL: String = "http://api-edu.gtl.ai/api/v1/imagesearch/tineye?url=";

    fun getImages(url: String) {
        AndroidNetworking.get("http://api-edu.gtl.ai/api/v1/imagesearch/bing?url=https://gtl-bucket.s3.amazonaws.com/13132cec1fe04c06b4f0b40d9fc8ecb3.jpg")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    response.keys()

                    }
                }

                override fun onError(anError: ANError) {
                    Log.i("error", "there was an error $anError" )
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_image_search, container, false)
        val recyclerView : RecyclerView = view.findViewById<RecyclerView>(R.id.rc_view)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = ItemAdapter(dummyData, requireContext())
        getImages(bingURL);
        return view;
    }


}