package com.example.pgr208exam.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.example.pgr208exam.ItemAdapter
import com.example.pgr208exam.R
import com.example.pgr208exam.SharedViewModel
import org.json.JSONArray
import org.json.JSONObject


class ImageSearchFragment() : Fragment() {

    //Dummy-data to not overload server
    //private val dummyData = Constants.getDummyData();

    private val viewModel: SharedViewModel by activityViewModels()
    private var url = "";
    private var imageList: ArrayList<String> = ArrayList();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Checking to see if the fragment has a saved imageList
        if(savedInstanceState != null){
        imageList = savedInstanceState.getStringArrayList("imageList") as ArrayList<String>
        }

        //Getting the response-url from server, stored in a shared ViewModel with SelectImageFragment
        url = if (viewModel.getResponseFromPost().value != null) {
            viewModel.getResponseFromPost().value.toString()
        } else {
            "No picture uploaded"
        }
        //Remove the saved imageList state if a new upload has happened
        if(url.startsWith("http")){
            imageList.clear()
        }
        // Inflate the layout for this fragment, with 3 views created on each row
        val view: View = inflater.inflate(R.layout.fragment_image_search, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.rc_view)
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        //Check to see if user has uploaded a image
        if (!url.startsWith("http") && imageList.isEmpty()) {
            Toast.makeText(requireContext(), "No image uploaded to server", Toast.LENGTH_LONG)
                .show();
            view.findViewById<RelativeLayout>(R.id.loadingPanel).visibility = GONE;
            view.findViewById<TextView>(R.id.loadingTextView).text =
                "Please upload a picture to see the results"
        } else if(imageList.isEmpty()) {
            //If user has uploaded image and gotten response, GET request is executed
            AndroidNetworking.get("http://api-edu.gtl.ai/api/v1/imagesearch/bing?url=$url")
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray) {

                        if(response.length() == 0) {
                            Toast.makeText(context, "The server couldent find any images", Toast.LENGTH_SHORT).show()
                            view.findViewById<RelativeLayout>(R.id.loadingPanel).visibility = GONE;
                            view.findViewById<TextView>(R.id.loadingTextView).text = "Server could not find any images, please try again"
                            viewModel.changeResponseFromPost("")

                        }
                        else {
                        for (index in 0 until response.length()) {
                            val imageURL =
                                (response.get(index) as JSONObject).getString("image_link")
                            imageList.add(index, imageURL)
                        }
                        //RecyclerAdapter is created with the list of urls gotten from the GET request
                        recyclerView.adapter = context?.let { ItemAdapter(imageList, it) };
                        view.findViewById<RelativeLayout>(R.id.loadingPanel).visibility = GONE;
                        view.findViewById<TextView>(R.id.loadingTextView).visibility = GONE;
                        viewModel.changeResponseFromPost("")

                        }
                    }


                    override fun onError(anError: ANError?) {
                        val textView = view.findViewById<TextView>(R.id.loadingTextView)
                        textView.visibility = VISIBLE
                        textView.text =
                           anError.toString()
                        Log.i("error", "there was an error $anError")
                    }
                }
                )
        }
        else {
            recyclerView.adapter = context?.let { ItemAdapter(imageList, it) };
            view.findViewById<RelativeLayout>(R.id.loadingPanel).visibility = GONE;
            view.findViewById<TextView>(R.id.loadingTextView).visibility = GONE;
        }


        return view;
    }

        override fun onSaveInstanceState(outState: android.os.Bundle) {
            super.onSaveInstanceState(outState)
            outState.putStringArrayList("imageList", imageList)
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