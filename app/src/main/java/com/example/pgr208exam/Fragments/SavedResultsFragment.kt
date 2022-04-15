package com.example.pgr208exam.Fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.*
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scaleMatrix
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pgr208exam.FullScreenImage
import com.example.pgr208exam.HorizontalAdapter
import com.example.pgr208exam.MainActivity
import com.example.pgr208exam.R
import java.io.ByteArrayOutputStream

class SavedResultsFragment : Fragment() {

    data class OriginalImage(val id: Int, val image: Bitmap, val date: String)
    data class ResultsImage(val id: Int, val image: Bitmap, val date: String, val original: Int)

    lateinit var linearLayout: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_saved_results, container, false)

        linearLayout = view.findViewById(R.id.linearLayout)

        val spinner: Spinner = view.findViewById(R.id.sortSpinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sorting_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        val sortingArray = resources.getStringArray(R.array.sorting_array)
        val orderCursor = ((activity as MainActivity).getImage("resultsNumber"))
        val orderArray: MutableList<Int> = arrayListOf()

        while (orderCursor.moveToNext()) {
            val original = orderCursor.getInt(orderCursor.getColumnIndexOrThrow("original"))
            orderArray.add(original)
        }
        val originalsCursor =
            ((activity as MainActivity).getImage("originals"))
        val resultsCursor =
            ((activity as MainActivity).getImage("results"))

        val originalsArrayNewest: MutableList<OriginalImage> = arrayListOf()
        var originalsArrayOldest: MutableList<OriginalImage> = arrayListOf()
        val originalsArraySize: MutableList<OriginalImage> = arrayListOf()
        val resultsArray: MutableList<ResultsImage> = arrayListOf()

        while (originalsCursor.moveToNext()) {
            val imageId = originalsCursor.getInt(0)
            val image: ByteArray =
                originalsCursor.getBlob(originalsCursor.getColumnIndexOrThrow("image"))
            val date = originalsCursor.getString(2)
            val image2: Bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
            originalsArrayNewest += (OriginalImage(imageId, image2, date))
        }

        originalsCursor.moveToFirst()

        while (originalsCursor.moveToNext()) {
            val imageId = originalsCursor.getInt(0)
            val image: ByteArray =
                originalsCursor.getBlob(originalsCursor.getColumnIndexOrThrow("image"))
            val date = originalsCursor.getString(2)
            val image2: Bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
            originalsArrayOldest = (arrayListOf(OriginalImage(imageId, image2, date)) + originalsArrayOldest) as MutableList<OriginalImage>
        }

        originalsCursor.moveToFirst()

        while (originalsCursor.moveToNext()) {
            val imageId = originalsCursor.getInt(0)
            val image: ByteArray =
                originalsCursor.getBlob(originalsCursor.getColumnIndexOrThrow("image"))
            val date = originalsCursor.getString(2)
            val image2: Bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
            for (item in orderArray) {
                originalsArraySize += OriginalImage(-1, image2, "-1")
            }

            if (orderArray.contains(imageId)) {
                originalsArraySize[orderArray.indexOf(imageId)] =
                    OriginalImage(imageId, image2, date)
            } else {
                originalsArraySize += (OriginalImage(imageId, image2, date))
            }
        }

        while (resultsCursor.moveToNext()) {
            val imageId = resultsCursor.getInt(0)
            val image: ByteArray =
                resultsCursor.getBlob(resultsCursor.getColumnIndexOrThrow("image"))
            val date = resultsCursor.getString(2)
            val original = resultsCursor.getInt(3)
            val image2: Bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
            resultsArray += ResultsImage(imageId, image2, date, original)
        }


        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {

                linearLayout.removeAllViews();

                var arrayToUse: MutableList<OriginalImage> = arrayListOf()
                when (sortingArray[pos]) {
                    "Newest" -> arrayToUse = originalsArrayOldest
                    "Oldest" -> arrayToUse = originalsArrayNewest
                    "Collection size" -> arrayToUse = originalsArraySize
                }

                for (item in arrayToUse) {
                    val newLayout: LinearLayout = LinearLayout(context)
                    val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        450,
                    )
                    newLayout.orientation = LinearLayout.HORIZONTAL
                    newLayout.layoutParams = params
                    linearLayout.addView(newLayout)

                    val imageView: ImageView = ImageView(context)
                    val imageParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                        250,
                        ViewGroup.LayoutParams.WRAP_CONTENT

                    )
                    imageView.layoutParams = imageParams

                    imageView.setOnClickListener {
                        val bitmap = imageView.drawable.toBitmap()
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                        val byteArray = stream.toByteArray()
                        val intent = Intent(requireContext(), FullScreenImage().javaClass)
                        intent.putExtra("bitmapImage", byteArray)
                        requireContext().startActivity(intent)
                    }

                    imageView.setImageBitmap(item.image)
                    newLayout.addView(imageView)

                    val recyclerView: RecyclerView = RecyclerView(requireContext())
                    val recyclerParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                    recyclerView.layoutParams = recyclerParams
                    recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    newLayout.addView(recyclerView)

                    val randomArray: ArrayList<ResultsImage> = arrayListOf()

                    for (newItem in resultsArray) {
                        if (newItem.original == item.id) {
                            /*val newImageView: ImageView = ImageView(context)
                            val newImageParams: LinearLayout.LayoutParams =
                                LinearLayout.LayoutParams(
                                    250,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                            newImageView.layoutParams = newImageParams
                            newImageView.setOnClickListener {
                                val bitmap = newImageView.drawable.toBitmap()
                                val stream = ByteArrayOutputStream()
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                                val byteArray = stream.toByteArray()
                                val intent = Intent(requireContext(), FullScreenImage().javaClass)
                                intent.putExtra("bitmapImage", byteArray)
                                requireContext().startActivity(intent)
                            }*/

                            randomArray += newItem
                        }
                    }
                    recyclerView.adapter = HorizontalAdapter(randomArray, requireContext())
                }

            }


            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        return view
    }

}

