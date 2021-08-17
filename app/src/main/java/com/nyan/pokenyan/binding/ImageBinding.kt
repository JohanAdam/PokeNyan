package com.nyan.pokenyan.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.nyan.pokenyan.R


@BindingAdapter("imageUrl")
fun bindImage(target: ImageView, imgUrl: String?) {
    imgUrl?.let {
        Glide.with(target.context)
            .load(imgUrl)
            .apply(RequestOptions()
                .error(R.drawable.ic_broken_img))
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .into(target)
    }
}
