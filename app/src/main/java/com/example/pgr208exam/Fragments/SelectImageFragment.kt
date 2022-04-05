package com.example.pgr208exam.Fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.util.Log.INFO
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pgr208exam.R
import com.example.pgr208exam.UriToBitmap
import com.example.pgr208exam.getBitmap
import java.util.logging.Level.INFO


class SelectImageFragment : Fragment() {

    public lateinit var selectImageView: ImageView
    public lateinit var imageUri: String
    public lateinit var selectTextView: TextView
    public lateinit var uploadButton: Button

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
        selectTextView = view.findViewById(R.id.textview_select_image)
        uploadButton = view.findViewById(R.id.upload_button)
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
        if (it.resultCode == Activity.RESULT_OK)
        imageUri = it.data?.data.toString()
        val selectedImage : Bitmap = getBitmap(requireContext(), null, imageUri, ::UriToBitmap)
        selectImageView.setImageBitmap(selectedImage)

        selectTextView.text = "Allrighty now you can upload"
        uploadButton.visibility = View.VISIBLE
    }

}