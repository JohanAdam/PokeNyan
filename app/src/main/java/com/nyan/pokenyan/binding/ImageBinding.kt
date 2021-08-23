package com.nyan.pokenyan.binding

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.nyan.pokenyan.R
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import timber.log.Timber


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    Timber.i("bindImage: ")
    val factory = DrawableCrossFadeFactory.Builder(10000).setCrossFadeEnabled(true).build()

    imgUrl?.let {
        Glide.with(imgView.context)
            .load(imgUrl)
            .placeholder(R.drawable.ic_pokeball)
            .apply(
                RequestOptions()
                    .error(R.drawable.ic_broken_img)
            )
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .listener(object : RequestListener<Drawable> {
//                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    e?.printStackTrace()
//                    return false
//                }
//
//                override fun onResourceReady(
//                    resource: Drawable?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    imgView.setImageDrawable(resource)
//                    return false
//                }
//
//            })
            .into(imgView)

    }
}
