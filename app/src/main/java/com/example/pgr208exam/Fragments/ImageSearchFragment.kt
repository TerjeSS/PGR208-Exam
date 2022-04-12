package com.example.pgr208exam.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.example.pgr208exam.Constants
import com.example.pgr208exam.ItemAdapter
import com.example.pgr208exam.R
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread


class ImageSearchFragment : Fragment() {

    private val dummyData = Constants.getDummyData()
    val googleURL: String = "http://api-edu.gtl.ai/api/v1/imagesearch/google?url=";
    val bingURL: String = "http://api-edu.gtl.ai/api/v1/imagesearch/bing?url=";
    val tineyeURL: String = "http://api-edu.gtl.ai/api/v1/imagesearch/tineye?url=";
    val imageList: ArrayList<String> = ArrayList()

    private fun getImages(url: String): ArrayList<String> {
        Log.i("test", "test")
        AndroidNetworking.get("http://api-edu.gtl.ai/api/v1/imagesearch/bing?url=https://gtl-bucket.s3.amazonaws.com/13132cec1fe04c06b4f0b40d9fc8ecb3.jpg")
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    for (index in 0 until response.length()) {
                        val imageURL = (response.get(index) as JSONObject).getString("image_link")
                        imageList?.add(index, imageURL)
                        Log.i("added", "image $imageURL added at index $index")
                    }

                }

                override fun onError(anError: ANError?) {
                    Log.i("error", "there was an error $anError")
                }
            }

            )
return imageList
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Receiving result response URL from SelectImageFragment
        setFragmentResultListener("requestKey") { key, bundle ->
            val result = bundle.getString("data")

            if (result != null) {
                Log.i("Result in new fragment", result)
            }
        }

        //Function to GET images from server
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        val list = getImages("");
        val view: View = inflater.inflate(R.layout.fragment_image_search, container, false)
        val recyclerView: RecyclerView = view.findViewById<RecyclerView>(R.id.rc_view)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        //recyclerView.adapter = imageList?.let { ItemAdapter(it, requireContext()) }
        recyclerView.adapter = ItemAdapter(list, requireContext())
        return view;
    }


}