package com.werb.glideman.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.werb.glideman.CircleBorderTransformation
import com.werb.glideman.CircleBorderWithPaddingTransformation
import com.werb.glideman.CircleTransformation
import com.werb.glideman.RoundTransformation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "http://store.happytifywin.com/uploads/20171130/48/48ADD8E0E01Dw640h640.jpeg"

        button.setOnClickListener {
            GlideApp.with(this)
                .load(url)
                .transition(withCrossFade())
                .transform(CircleTransformation())
                .into(imageView1)
            GlideApp.with(this)
                .load(url)
                .transition(withCrossFade())
                .transform(RoundTransformation(5f))
                .into(imageView2)
            GlideApp.with(this)
                .load(url)
                .transition(withCrossFade())
                .transform(CircleBorderTransformation(5f, resources.getColor(R.color.colorPrimaryDark)))
                .into(imageView3)
            GlideApp.with(this)
                .load(url)
                .transition(withCrossFade())
                .transform(CircleBorderWithPaddingTransformation(
                    5f,
                    resources.getColor(R.color.colorPrimaryDark),
                    5f,
                    resources.getColor(android.R.color.white)))
                .into(imageView4)
        }

        button2.setOnClickListener {
            GlideApp.with(this).clear(imageView1)
            GlideApp.with(this).clear(imageView2)
            GlideApp.with(this).clear(imageView3)
            GlideApp.with(this).clear(imageView4)
        }
    }
}
