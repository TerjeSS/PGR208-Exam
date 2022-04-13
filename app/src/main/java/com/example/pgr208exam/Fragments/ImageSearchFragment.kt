package com.example.pgr208exam.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pgr208exam.Constants
import com.example.pgr208exam.ItemAdapter
import com.example.pgr208exam.R
import com.example.pgr208exam.SharedViewModel


class ImageSearchFragment() : Fragment() {

    private val dummyData = Constants.getDummyData();
    val googleURL: String = "http://api-edu.gtl.ai/api/v1/imagesearch/google?url=";
    val bingURL: String = "http://api-edu.gtl.ai/api/v1/imagesearch/bing?url=";
    val tineyeURL: String = "http://api-edu.gtl.ai/api/v1/imagesearch/tineye?url=";
    var result : ArrayList<String>? = null
    private val viewModel: SharedViewModel by activityViewModels()



    //val imageList: ArrayList<String> = ArrayList()




    fun doThisFirst(){
        setFragmentResultListener("requestKey") { key, bundle ->
            result = bundle.getStringArrayList("data")

            if (result != null) {
                Log.i("Result fragmentlistener", result.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Receiving result response URL from SelectImageFragment
        /*setFragmentResultListener("requestKey") { key, bundle ->
            result = bundle.getStringArrayList("data")

            if (result != null) {
                Log.i("Result in new fragmentlistener", result.toString())
            }
        }
        Log.i("test", result.toString() + "dette er etter listener")*/

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_image_search, container, false)
        val recyclerView: RecyclerView = view.findViewById<RecyclerView>(R.id.rc_view)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        doThisFirst();
        Log.i("test", result.toString() + "dette er etter listener")
        Log.i("test", viewModel.getList().toString() + "liste av urls");
        val list = viewModel.getList();
        recyclerView.adapter = list.value?.let { ItemAdapter(it, requireContext()) };

        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.list.observe(viewLifecycleOwner) {
            val listOfUrls = Unit
            listOfUrls
        }
    }


}