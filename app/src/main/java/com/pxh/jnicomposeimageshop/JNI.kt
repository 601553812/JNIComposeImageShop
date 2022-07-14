package com.pxh.jnicomposeimageshop

class JNI {
    init {
        System.loadLibrary("jnicomposeimageshop")
    }

    external fun change(bitmap: IntArray, width: Int, height: Int)
}