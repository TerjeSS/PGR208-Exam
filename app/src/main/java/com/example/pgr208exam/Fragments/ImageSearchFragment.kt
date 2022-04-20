package com.example.pgr208exam.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.example.pgr208exam.Constants
import com.example.pgr208exam.ItemAdapter
import com.example.pgr208exam.R
import com.example.pgr208exam.SharedViewModel
import org.json.JSONArray
import org.json.JSONObject


class ImageSearchFragment() : Fragment() {

    private val dummyData = Constants.getDummyData();
    var result : ArrayList<String>? = null
    private val viewModel: SharedViewModel by activityViewModels()
    private var url = "";
    val imageList: ArrayList<String> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = if(viewModel.getResponseFromPost().value != null){
            viewModel.getResponseFromPost().value.toString()
        }else {
            "No picture uploaded"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_image_search, container, false)
        val recyclerView: RecyclerView = view.findViewById<RecyclerView>(R.id.rc_view)
        recyclerView.layoutManager = GridLayoutManager(context, 3)


        if(!url.startsWith("http")){
            Toast.makeText(requireContext(), "No image uploaded to server", Toast.LENGTH_LONG).show();
            view.findViewById<RelativeLayout>(R.id.loadingPanel).visibility = GONE;
            view.findViewById<TextView>(R.id.loadingTextView).text = "Please upload a picture to see the results"
        }
        else {

        AndroidNetworking.get("http://api-edu.gtl.ai/api/v1/imagesearch/bing?url=$url")
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    for (index in 0 until response.length()) {
                        val imageURL = (response.get(index) as JSONObject).getString("image_link")
                        imageList.add(index, imageURL)
                        Log.i("testAdded", "image $imageURL added at index $index")
                    }

                    recyclerView.adapter = context?.let { ItemAdapter(imageList, it) };
                    view.findViewById<RelativeLayout>(R.id.loadingPanel).visibility = GONE;
                    view.findViewById<TextView>(R.id.loadingTextView).visibility = GONE;
                    viewModel.changeResponseFromPost("")
                }

                override fun onError(anError: ANError?) {
                    Log.i("error", "there was an error $anError")
                }
            }

            )
        }



//
//        Log.i("test", result.toString() + "dette er etter listener")
//        Log.i("test", viewModel.getList().toString() + "liste av urls");


//        val list = viewModel.getList();
//        recyclerView.adapter = list.value?.let { ItemAdapter(it, requireContext()) };

        return view;
    }


    //Tror bare denne trengs hvis man skal auto-update noe på UIen
   /* override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.list.observe(viewLifecycleOwner) {
            val listOfUrls = Unit
            listOfUrls
        }
    }*/


}