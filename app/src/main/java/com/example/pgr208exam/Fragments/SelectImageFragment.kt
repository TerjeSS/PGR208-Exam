package com.example.pgr208exam.Fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.util.Log.INFO
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pgr208exam.R
import com.example.pgr208exam.UriToBitmap
import com.example.pgr208exam.getBitmap
import java.util.logging.Level.INFO


class SelectImageFragment : Fragment() {

    public lateinit var selectImageView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_select_image, container, false)
        selectImageView = view.findViewById(R.id.imageview_select_image)
        selectImageView.setOnClickListener(View.OnClickListener {
            val i = Intent()
            i.type = "*/*"
            i.action = Intent.ACTION_GET_CONTENT

            startForResult.launch(i)
        })

        return view
    }

    //Callback for getting image/url stored on the device
    var startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        val selectedImage : Bitmap = getBitmap(requireContext(), null, it.data?.data.toString(), ::UriToBitmap)
        selectImageView.setImageBitmap(selectedImage)
    }


}