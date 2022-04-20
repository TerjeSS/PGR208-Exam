package com.example.pgr208exam.Fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pgr208exam.FullScreenImage
import com.example.pgr208exam.HorizontalAdapter
import com.example.pgr208exam.MainActivity
import com.example.pgr208exam.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import kotlin.concurrent.thread

class SavedResultsFragment : Fragment() {

    data class OriginalImage(val id: Int, val image: Bitmap)
    data class ResultsImage(val id: Int, val image: Bitmap, val original: Int)

    lateinit var linearLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_saved_results, container, false)

        linearLayout = view.findViewById(R.id.linearLayout)

        val sortingArray: Array<out String> = resources.getStringArray(R.array.sorting_array)
        val orderArray: MutableList<Int> = arrayListOf()
        val originalsArrayNewest: MutableList<OriginalImage> = arrayListOf()
        var originalsArrayOldest: MutableList<OriginalImage> = arrayListOf()
        val originalsArraySize: MutableList<OriginalImage> = arrayListOf()
        val resultsArray: MutableList<ResultsImage> = arrayListOf()

        val createViewThread = Thread(Runnable {

            val spinner: Spinner = view.findViewById(R.id.sortSpinner)

            createLayout(
                sortingArray,
                originalsArrayNewest,
                originalsArrayOldest,
                originalsArraySize,
                resultsArray,
                orderArray,
                spinner
            )

            requireActivity().runOnUiThread(java.lang.Runnable {
                ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.sorting_array,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                }
            })
        })

        createViewThread.start()

        return view
    }



    fun createLayout(
        sortingArray: Array<out String>,
        originalsArrayNewest: MutableList<OriginalImage>,
        originalsArrayOldest: MutableList<OriginalImage>,
        originalsArraySize: MutableList<OriginalImage>,
        resultsArray: MutableList<ResultsImage>,
        orderArray: MutableList<Int>,
        spinner: Spinner
    ) {
        fetchData(
            orderArray, originalsArrayNewest, originalsArrayOldest, originalsArraySize,
            resultsArray
        )

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
                        val tableAndId: ArrayList<String> = arrayListOf("originals", item.id.toString())
                        intent.putExtra("bitmapImage", byteArray)
                        intent.putExtra("tableAndId", tableAndId)
                        activity?.onBackPressed()
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
                    recyclerView.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    newLayout.addView(recyclerView)

                    val randomArray: ArrayList<ResultsImage> = arrayListOf()

                    for (newItem in resultsArray) {
                        if (newItem.original == item.id) {
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
    }



    fun fetchData(
        orderArray: MutableList<Int>,
        originalsArrayNewest: MutableList<OriginalImage>,
        originalsArrayOldest: MutableList<OriginalImage>,
        originalsArraySize: MutableList<OriginalImage>,
        resultsArray: MutableList<ResultsImage>,
    ) {

        val orderCursor = ((activity as MainActivity).getImage("resultsNumber"))

        while (orderCursor.moveToNext()) {
            val original = orderCursor.getInt(orderCursor.getColumnIndexOrThrow("original"))
            orderArray.add(original)
        }
        val originalsCursor =
            ((activity as MainActivity).getImage("originals"))
        val resultsCursor =
            ((activity as MainActivity).getImage("results"))

        while (originalsCursor.moveToNext()) {
            val imageId = originalsCursor.getInt(0)
            val image: ByteArray =
                originalsCursor.getBlob(originalsCursor.getColumnIndexOrThrow("image"))
            val image2: Bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
            originalsArrayNewest += OriginalImage(imageId, image2)
            originalsArraySize += OriginalImage(imageId, image2)
        }
        val originalsArraySizeTmp: MutableList<OriginalImage> = arrayListOf()


        while (originalsCursor.moveToPrevious()) {
            val imageId = originalsCursor.getInt(0)
            val image: ByteArray =
                originalsCursor.getBlob(originalsCursor.getColumnIndexOrThrow("image"))
            val image2: Bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
            originalsArrayOldest += OriginalImage(imageId, image2)
        }

        for (item in originalsArraySize) {
            originalsArraySizeTmp += item
        }
        for ((index, item) in orderArray.withIndex()) {
            loop@ for (otherItem in originalsArraySizeTmp) {
                if (otherItem.id == item) {
                    originalsArraySize[index] = otherItem
                    break@loop
                }
            }
        }

        while (resultsCursor.moveToNext()) {
            val imageId = resultsCursor.getInt(0)
            val image: ByteArray =
                resultsCursor.getBlob(resultsCursor.getColumnIndexOrThrow("image"))
            val original = resultsCursor.getInt(2)
            val image2: Bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
            resultsArray += ResultsImage(imageId, image2, original)
        }
    }

}

