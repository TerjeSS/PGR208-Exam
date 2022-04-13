package com.example.pgr208exam

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import org.json.JSONArray
import org.json.JSONObject


//Import images from local storage
fun VectorDrawableToBitmap(context: Context, id: Int?, uri: String?) : Bitmap {
    val drawable = (ContextCompat.getDrawable(context!!, id!!) as VectorDrawable)
    val image = Bitmap.createBitmap(
        drawable.getIntrinsicWidth(),
        drawable.getIntrinsicHeight(),
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(image)
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
    drawable.draw(canvas)

    return image
}

fun UriToBitmap(context: Context, id: Int?, uri: String?): Bitmap {
    val image: Bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, Uri.parse(uri))
    return image
}

fun getBitmap(context: Context, id: Int?, uri: String?, decoder: (Context, Int?, String?) -> Bitmap): Bitmap {
    return decoder(context, id, uri)
}

 fun getImages(url: String, uploadButton: Button): ArrayList<String> {
     val imageList: ArrayList<String> = ArrayList()

    Log.i("test", "start of get request")
    AndroidNetworking.get("http://api-edu.gtl.ai/api/v1/imagesearch/bing?url=$url")
        .build()
        .getAsJSONArray(object : JSONArrayRequestListener {
            override fun onResponse(response: JSONArray) {
                for (index in 0 until response.length()) {
                    val imageURL = (response.get(index) as JSONObject).getString("image_link")
                    imageList?.add(index, imageURL)
                    Log.i("testAdded", "image $imageURL added at index $index")
                }


            }

            override fun onError(anError: ANError?) {
                Log.i("error", "there was an error $anError")
            }
        }

        )
    return imageList
}
//GET REQUEST URLS
val googleURL: String = "http://api-edu.gtl.ai/api/v1/imagesearch/google?url=";
val bingURL: String = "http://api-edu.gtl.ai/api/v1/imagesearch/bing?url=";
val tineyeURL: String = "http://api-edu.gtl.ai/api/v1/imagesearch/tineye?url=";