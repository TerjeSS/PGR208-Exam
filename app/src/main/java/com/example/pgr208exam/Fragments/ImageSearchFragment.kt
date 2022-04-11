package com.example.pgr208exam.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pgr208exam.Constants
import com.example.pgr208exam.ItemAdapter
import com.example.pgr208exam.R
import kotlinx.coroutines.NonDisposableHandle.parent

class ImageSearchFragment : Fragment() {

    val dummyData = Constants.getDummyData()

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

        return view;
    }


}