# JNIComposeImageShop
一个通过JNI实现将图片灰度化的小工具
界面使用jetpack compose,将相册中的图片通过Uri读取为Bitmap,转换为Int数组后传入JNI,进行灰度化计算后传回JAVA层,通过Bitmap.create重新生成Bitmap,显示在界面上.
