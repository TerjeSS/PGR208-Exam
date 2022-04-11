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
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.pgr208exam.Constants
import com.example.pgr208exam.ItemAdapter
import com.example.pgr208exam.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject
import com.example.pgr208exam.ImageObject as ImageObject1


class ImageSearchFragment : Fragment() {

    val dummyData = Constants.getDummyData()
    val googleURL: String = "http://api-edu.gtl.ai/api/v1/imagesearch/google?url=";
    val bingURL: String = "http://api-edu.gtl.ai/api/v1/imagesearch/bing?url=";
    val tineyeURL: String = "http://api-edu.gtl.ai/api/v1/imagesearch/tineye?url=";



    val jsonImage = """
    {
        "store_link": "https://hymanltd.com/vehicles/6773-1966-ford-mustang-gt-coupe/",
        "name": "1966 Ford Mustang GT Coupe | Hyman Ltd.",
        "domain": "hymanltd.com",
        "identifier": "bing",
        "tracking_id": "None",
        "thumbnail_link": "https://th.bing.com/th/id/OIP.m6CXdg6dLGPUJJw0IzoZlAHaE8?pid=ImgDet&w=211&h=140&c=7",
        "description": "",
        "image_link": "https://th.bing.com/th/id/OIP.m6CXdg6dLGPUJJw0IzoZlAHaE8?pid=ImgDet&w=211&h=140&c=7",
        "current_date": "None"
    }""";

    fun getImages(url: String) {
        AndroidNetworking.get("http://api-edu.gtl.ai/api/v1/imagesearch/bing?url=https://gtl-bucket.s3.amazonaws.com/13132cec1fe04c06b4f0b40d9fc8ecb3.jpg")
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    for(index in 0 until response.length()){
                        val imageURL = (response.get(index) as JSONObject).getString("image_link")
                        Log.i("imageurl", imageURL)
                    }

                }

                override fun onError(anError: ANError?) {
                    Log.i("error", "there was an error $anError")
                }
            }
            )
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