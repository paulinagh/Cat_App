package com.cat

import android.content.Context
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide

fun placeImage(context: Context, image: String, imageView: ImageView) {
    Glide.with(context)
        .load(image)
        .into(imageView)
    //.placeholder(R.drawable.ic_baseline_person_24)
    //.error(R.drawable.ic_baseline_error_24)
}

fun placeImage(context: CardView, image: String, imageView: ImageView) {
    Glide.with(context)
        .load(image)
        .into(imageView)
    //.placeholder(R.drawable.ic_baseline_person_24)
    //.error(R.drawable.ic_baseline_error_24)
}