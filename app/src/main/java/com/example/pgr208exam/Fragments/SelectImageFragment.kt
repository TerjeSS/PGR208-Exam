package com.example.pgr208exam.Fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener
import com.example.pgr208exam.R
import com.example.pgr208exam.UriToBitmap
import com.example.pgr208exam.getBitmap
import okhttp3.Response
import java.io.File


class SelectImageFragment : Fragment() {

    lateinit var selectImageView: ImageView
    lateinit var imageUri: String
    lateinit var selectTextView: TextView
    lateinit var uploadButton: Button

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
        Log.i("This is the image URI", imageUri)
        val selectedImage : Bitmap = getBitmap(requireContext(), null, imageUri, ::UriToBitmap)
        selectImageView.setImageBitmap(selectedImage)


        //Adding elements after successfully adding image
        selectTextView.text = "Allrighty, now you can upload!"
        uploadButton.visibility = View.VISIBLE


        //OnClick to and send uri to server
        uploadButton.setOnClickListener {
            Log.i("Button click", "Upload button got clicked")
            AndroidNetworking.post("http://api-edu.gtl.ai/api/v1/imagesearch/upload")
                .addHeaders("Content-Type", "image/jpeg")
                .addHeaders("Content-Disposition", "form-data")
                .addBodyParameter("image", imageUri)
                .build()
                .getAsOkHttpResponseAndString(object : OkHttpResponseAndStringRequestListener {
                    override fun onResponse(okHttpResponse: Response, response: String) {
                        Log.i("This is the OK code", okHttpResponse.toString())
                        Log.i("This is the response", response)
                    }
                    override fun onError(anError: ANError) {
                        Log.i( "This is the error", anError.toString())
                    }
                })
        }
    }

}


