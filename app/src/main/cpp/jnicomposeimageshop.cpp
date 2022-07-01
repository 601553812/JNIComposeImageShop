#include <jni.h>
#include <string>
#include <android/log.h>
#define LOG_TAG "JNI"
#define logv(...) __android_log_print(ANDROID_LOG_VERBOSE,LOG_TAG,__VA_ARGS__)
#define logd(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define logi(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define logw(...) __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define loge(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)


extern "C"
JNIEXPORT void JNICALL
Java_com_pxh_jnicomposeimageshop_JNI_change(JNIEnv *env, jobject thiz, jintArray bitmap, jint width,
                                            jint height) {
    int image [width*height];
    (*env).GetIntArrayRegion(bitmap,0,width*height,image);
    loge("获取图片数组成功,宽度为%d,高度为%d",width,height);
    for (int i = 0; i < width*height; ++i) {
        int red = image[i]>>16&0xff;
        int green = image[i]>>8&0xff;
        int blue = image[i]&0xff;
        int alpha = image[i]>>24&0xff;
        int gray = (red+green+blue)/3;
        image[i] = gray+(gray<<8)+(gray<<16)+(alpha<<24);
    }
    loge("图片转换完毕,准备输出");
    (*env).SetIntArrayRegion(bitmap,0,width*height,image);
}