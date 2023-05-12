package com.example.nalssieottae

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String) {
    Glide.with(view.context)
        .load(imageUrl)
        .centerCrop()
        .into(view)
}