package com.werb.glideman

import android.content.res.Resources
import android.graphics.*
import android.os.Build
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool

/**
 * Created by wanbo on 2018/4/7.
 */
interface TransformationConfig {

    fun clear(canvas: Canvas) {
        canvas.setBitmap(null)
    }

    fun getAlphaSafeBitmap(
        pool: BitmapPool, maybeAlphaSafe: Bitmap): Bitmap {
        val safeConfig = getAlphaSafeConfig(maybeAlphaSafe)
        if (safeConfig == maybeAlphaSafe.config) {
            return maybeAlphaSafe
        }
        val argbBitmap = pool.get(maybeAlphaSafe.width, maybeAlphaSafe.height, safeConfig)
        Canvas(argbBitmap).drawBitmap(maybeAlphaSafe, 0f, 0f, null)
        return argbBitmap
    }

    fun getAlphaSafeConfig(inBitmap: Bitmap): Bitmap.Config {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Bitmap.Config.RGBA_F16 == inBitmap.config) {
                return Bitmap.Config.RGBA_F16
            }
        }
        return Bitmap.Config.ARGB_8888
    }

    fun getShaderPaint(targetWidth: Int, targetHeight: Int, alphaSafeBitmap: Bitmap) = Paint().apply {
        isAntiAlias = true
        isDither = true
        shader = getPaintShader(targetWidth, targetHeight, alphaSafeBitmap)
    }

    fun getShaderPaint(alphaSafeBitmap: Bitmap) = Paint().apply {
        isAntiAlias = true
        isDither = true
        shader = BitmapShader(alphaSafeBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    }

    fun getBoardPaint(borderWidth: Float, borderColor: Int) = Paint().apply {
        isAntiAlias = true
        isDither = true
        strokeWidth = borderWidth
        color = borderColor
        style = Paint.Style.STROKE
    }

    fun getDefaultPaint(paintColor: Int? = null) = Paint().apply {
        isAntiAlias = true
        isDither = true
        paintColor?.let { color = it }
    }

    private fun getPaintShader(targetWidth: Int, targetHeight: Int, alphaSafeBitmap: Bitmap): BitmapShader {

        val shader = BitmapShader(alphaSafeBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        shader.setLocalMatrix(getMatrix(targetWidth, targetHeight, alphaSafeBitmap.width, alphaSafeBitmap.height))

        return shader
    }

    fun getMatrix(targetWidth: Int, targetHeight: Int, srcWidth: Int, srcHeight: Int): Matrix {
        val scaleX = targetWidth / srcWidth.toFloat()
        val scaleY = targetHeight / srcHeight.toFloat()
        val maxScale = Math.max(scaleX, scaleY)

        val scaledWidth = maxScale * srcWidth
        val scaledHeight = maxScale * srcHeight

        val left = (targetWidth - scaledWidth) / 2f
        val top = (targetWidth - scaledHeight) / 2f

        val matrix = Matrix()

        matrix.postTranslate(left, top)
        return matrix
    }

    fun dip2px(dpValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

}