package com.example.pgr208exam.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.example.pgr208exam.*
import org.json.JSONArray
import org.json.JSONObject


class ImageSearchFragment : Fragment() {

    private lateinit var googleBtn: Button
    private lateinit var tineyeBtn: Button
    private lateinit var bingBtn: Button
    private val viewModel: SharedViewModel by activityViewModels()
    private var url = ""
    private var imageList: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
        googleBtn = view.findViewById(R.id.googleBtn)
        tineyeBtn = view.findViewById(R.id.tineyeBtn)
        bingBtn = view.findViewById(R.id.bingBtn)
        val recyclerView: RecyclerView = view.findViewById(R.id.rc_view)
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        //Check to see if user has uploaded a image
        if (!url.startsWith("http") && imageList.isEmpty()) {
            view.findViewById<LinearLayout>(R.id.buttonLayout).visibility = GONE
            view.findViewById<TextView>(R.id.search_engine_tv).visibility = GONE
            Toast.makeText(requireContext(), "No image uploaded to server", Toast.LENGTH_LONG)
                .show()
            view.findViewById<RelativeLayout>(R.id.loadingPanel).visibility = GONE
            view.findViewById<TextView>(R.id.loadingTextView).text =
                getString(R.string.uploadText)

        }
        else {
            recyclerView.adapter = context?.let { ItemAdapter(imageList, it) }
            view.findViewById<RelativeLayout>(R.id.loadingPanel).visibility = GONE
            view.findViewById<TextView>(R.id.loadingTextView).visibility = GONE
        }

        googleBtn.setOnClickListener {
            Toast.makeText(context, "Searching Google...",Toast.LENGTH_LONG).show()
            searchImages(view, recyclerView, googleURL)
        }
        tineyeBtn.setOnClickListener {
            Toast.makeText(context, "Searching Tineye...",Toast.LENGTH_LONG).show()
            searchImages(view, recyclerView, tineyeURL)
        }
        bingBtn.setOnClickListener {
            Toast.makeText(context, "Searching Bing...",Toast.LENGTH_LONG).show()
            searchImages(view, recyclerView, bingURL)
        }
        return view
    }

    private fun searchImages(
        view: View,
        recyclerView: RecyclerView,
        getEndpoint: String
    ) {
        view.findViewById<RelativeLayout>(R.id.loadingPanel).visibility = VISIBLE
        view.findViewById<TextView>(R.id.errorTV).visibility = GONE

        AndroidNetworking.get("$getEndpoint$url")
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    if (response.length() == 0) {
                        Toast.makeText(
                            context,
                            "The server could not find any images",
                            Toast.LENGTH_SHORT
                        ).show()
                        view.findViewById<RelativeLayout>(R.id.loadingPanel).visibility = GONE
                        view.findViewById<TextView>(R.id.errorTV).visibility = VISIBLE
                        view.findViewById<TextView>(R.id.errorTV).text =
                            getString(R.string.noImagesText)
                        viewModel.changeResponseFromPost("")

                    } else {
                        for (index in 0 until response.length()) {
                            val imageURL =
                                (response.get(index) as JSONObject).getString("image_link")
                            imageList.add(index, imageURL)
                        }
                        //RecyclerAdapter is created with the list of urls gotten from the GET request
                        recyclerView.adapter = context?.let { ItemAdapter(imageList, it) }
                        view.findViewById<RelativeLayout>(R.id.loadingPanel).visibility = GONE
                        view.findViewById<TextView>(R.id.loadingTextView).visibility = GONE
                        viewModel.changeResponseFromPost("")
                    }
                }

                override fun onError(anError: ANError?) {
                    val textView = view.findViewById<TextView>(R.id.loadingTextView)
                    textView.visibility = VISIBLE
                    textView.text =
                        anError.toString()
                }
            }
            )
    }

    override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            outState.putStringArrayList("imageList", imageList)
        }
}