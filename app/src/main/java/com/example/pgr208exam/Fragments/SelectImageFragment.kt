package com.example.pgr208exam.Fragments

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.contentValuesOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navDeepLink
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener
import com.edmodo.cropper.CropImageView
import com.example.pgr208exam.*
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import android.content.Context as Context1


class SelectImageFragment : Fragment() {
    var listOfUrls: ArrayList<String> = ArrayList();
    lateinit var selectImageView: CropImageView
    lateinit var imageUri: String
    lateinit var selectTextView: TextView
    lateinit var uploadButton: Button
    lateinit var rotateLeftButton: Button
    lateinit var rotateRightButton: Button

    lateinit var seeResultTextView: TextView
    private val viewModel: SharedViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.list.observe(viewLifecycleOwner) { listOfUrls }
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
        rotateLeftButton = view.findViewById(R.id.rotate_left_button)
        rotateRightButton = view.findViewById(R.id.rotate_right_button)
        seeResultTextView = view.findViewById(R.id.seeResultTextView)


        //animation on icon
        selectImageView.animate().apply {
            duration = 2000
            rotationX(360f)
        }.start()

        uploadButton.setOnClickListener(View.OnClickListener {
            //Ask the user for access to manage all files on the device
            (activity as MainActivity).deleteUnused()
            requestPermission()
            val i = Intent()
            i.type = "image/*"
            i.action = Intent.ACTION_GET_CONTENT
            uploadButton.text = "upload"
            uploadButton.text = getString(R.string.uploadBtnText)
            startForResult.launch(i)
        })
        return view
    }


    //Request permission to access all files once
    private fun requestPermission() {
        if (SDK_INT > Build.VERSION_CODES.Q) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.data =
                        Uri.parse(
                            String.format(
                                "package:%s",
                                getApplicationContext<Context1>().packageName
                            )
                        )
                    startActivity(intent)
                } catch (ex: java.lang.Exception) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    startActivity(intent)
                }
            }
        }
    }


    //Callback for getting image/url stored on the device
    var startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                imageUri = it.data?.data.toString()
                Log.i("This is the image URI", imageUri)
                val selectedImage: Bitmap =
                    getBitmap(requireContext(), null, imageUri, ::UriToBitmap)
                selectImageView.setImageBitmap(selectedImage)

                //Adding elements after successfully adding image

                selectTextView.text = getString(R.string.ImageIsReadyTextView)
                uploadButton.visibility = View.VISIBLE
                rotateLeftButton.visibility = View.VISIBLE
                rotateRightButton.visibility = View.VISIBLE
                selectImageView.setBackgroundResource(android.R.color.transparent)


                if (it.resultCode != Activity.RESULT_OK) {
                    selectTextView.text = getString(R.string.somethingWentWrongTextView)

                }

                //OnClick to rotate image to the left
                rotateLeftButton.setOnClickListener() {
                    selectImageView.rotateImage(-90)
                }

                //OnClick to rotate image to the right
                rotateRightButton.setOnClickListener() {
                    selectImageView.rotateImage(90)
                }


                //OnClick to rotate image to the right
                rotateRightButton.setOnClickListener() {
                    selectImageView.rotateImage(90)
                }


                //OnClick to crop and upload to the server
                uploadButton.setOnClickListener() {
                    //Bitmap of cropped image
                    val croppedBitmap: Bitmap = selectImageView.croppedImage
                    selectImageView.setImageBitmap(croppedBitmap)

                    val dbHelper = FeedReaderDbHelper(requireContext())
                    dbHelper.writableDatabase.insert("originals", null, ContentValues().apply {
                        put("image", (activity as MainActivity).bitArray(croppedBitmap))
                    })


                    //Creating a jpeg-file of the bitmap and saving it on the device
                    val filename = "selectedImage.jpeg"
                    val sd = Environment.getExternalStorageDirectory()
                    val dest = File(sd, filename)
                    Log.i("This is the jpeg", dest.absolutePath)

                    try {
                        val out = FileOutputStream(dest)
                        croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 15, out)
                        out.flush()
                        out.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    //Upload
                    Log.i("Button click", "Upload button got clicked")
                    AndroidNetworking.upload("http://api-edu.gtl.ai/api/v1/imagesearch/upload")
                        .addMultipartFile("image", dest)
                        .addMultipartParameter("Content-Type", "image/jpeg")
                        .setTag("uploadTest")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsOkHttpResponseAndString(object :
                            OkHttpResponseAndStringRequestListener {
                            override fun onResponse(okHttpResponse: Response, response: String) {
                                Log.i("This is the OK code", okHttpResponse.toString())
                                Log.i("This is the response", response)

                                //Sending result to ImageSearchFragment
                                val result = response
                                viewModel.changeResponseFromPost(response)

                                //Updating UI
                                selectTextView.text = getString(R.string.successTextView)
                                rotateLeftButton.visibility = View.GONE
                                rotateRightButton.visibility = View.GONE
                                uploadButton.visibility = View.GONE
                                selectImageView.setGuidelines(0)
                                selectImageView.imageResource = android.R.color.transparent
                                selectImageView.setBackgroundResource(R.drawable.icon)
                                seeResultTextView.visibility = View.GONE

                                //animation on icon
                                selectImageView.animate().apply {
                                    duration = 2000
                                    rotationY(720f)
                                    scaleXBy(-0.4F)
                                    scaleYBy(-0.4F)
                                }.start()
                            }

                            override fun onError(anError: ANError) {
                                //Error handling when not uploading pics
                                uploadButton.visibility = View.GONE
                                rotateLeftButton.visibility = View.GONE
                                rotateRightButton.visibility = View.GONE
                                selectTextView.text = getString(R.string.onErrorTextView)
                                Log.i("This is the error", anError.errorBody)
                            }
                        })
                }
            }
        }

}

