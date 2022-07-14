package com.pxh.jnicomposeimageshop

import android.graphics.Bitmap

class JNI {
    init {
        System.loadLibrary("jnicomposeimageshop")
    }
    external  fun change(bitmap: IntArray,width:Int,height:Int)
}