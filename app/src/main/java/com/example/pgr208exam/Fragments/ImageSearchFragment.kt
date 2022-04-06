package com.example.pgr208exam.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pgr208exam.Constants
import com.example.pgr208exam.ItemAdapter
import com.example.pgr208exam.R
import kotlinx.coroutines.NonDisposableHandle.parent

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImageSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageSearchFragment : Fragment() {

    val dummyData = Constants.getDummyData()

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
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = ItemAdapter(dummyData, requireContext())

        return view;
    }


}