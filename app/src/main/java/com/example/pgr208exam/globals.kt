package com.example.pgr208exam

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore


fun UriToBitmap(context: Context, id: Int?, uri: String?): Bitmap {
    val image: Bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, Uri.parse(uri))
    return image
}

fun getBitmap(context: Context, id: Int?, uri: String?, decoder: (Context, Int?, String?) -> Bitmap): Bitmap {
    return decoder(context, id, uri)
}


//GET REQUEST URLS
val googleURL: String = "http://api-edu.gtl.ai/api/v1/imagesearch/google?url=";
val bingURL: String = "http://api-edu.gtl.ai/api/v1/imagesearch/bing?url=";
val tineyeURL: String = "http://api-edu.gtl.ai/api/v1/imagesearch/tineye?url=";