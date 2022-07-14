package com.pxh.jnicomposeimageshop

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel : ViewModel() {
    val _bitmap: MutableLiveData<Bitmap> = MutableLiveData()

    fun changeImageByC() {
        val bitmap = _bitmap.value!!
        //初始化JNI
        val jni = JNI()
        //获取尺寸
        val size = bitmap.width * bitmap.height
        //创建像素数组
        val pixels = IntArray(size)

        //将图片装入像素数组
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        //将像素数组传入JNI
        Log.e("MyViewModel", "changeImageByC: ")


                jni.change(pixels, bitmap.width, bitmap.height)

            _bitmap.value =
                Bitmap.createBitmap(pixels, bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            Log.e("MyViewModel", "changeImageByC2: ${Thread.currentThread()}")



        //利用返回的数组创建bitmap

    }


}