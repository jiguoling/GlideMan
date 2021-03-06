package com.werb.glideman

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * Created by wanbo on 2018/4/12.
 */
class MaskColorTransformation(private val color: Int) : BitmapTransformation(), TransformationConfig {

    private val id = this::class.java.name

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val bitmap = pool.get(toTransform.width, toTransform.height, getAlphaSafeConfig(toTransform)).apply { setHasAlpha(true) }
        val canvas = Canvas(bitmap)
        val alphaSafeBitmap = getAlphaSafeBitmap(pool, toTransform)
        val paint = getShaderPaint(alphaSafeBitmap)
        val rectF = RectF(0f, 0f, toTransform.width.toFloat(), toTransform.height.toFloat())
        canvas.drawRect(rectF, paint)
        canvas.drawColor(color)
        clear(canvas)
        // save in pool to reuse
        if (alphaSafeBitmap != toTransform) {
            pool.put(alphaSafeBitmap)
        }
        return bitmap
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(id.toByteArray())
    }

    override fun equals(other: Any?): Boolean {
        return other is MaskColorTransformation
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}