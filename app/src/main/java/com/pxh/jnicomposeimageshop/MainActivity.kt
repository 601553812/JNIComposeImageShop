package com.pxh.jnicomposeimageshop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.imageLoader
import com.pxh.jnicomposeimageshop.ui.theme.JNIComposeImageShopTheme

class MainActivity : ComponentActivity() {
    private val myViewModel: MyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JNIComposeImageShopTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting(myViewModel)
                }
            }
        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 111) {
            val uri = data?.data
            if (uri != null) {
                Log.e("MainActivity", "onActivityResult: ")
                myViewModel._bitmap.value = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}

@Composable
fun Greeting(viewModel: MyViewModel) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "ShowImage") {
        composable("ShowImage") {
            ShowImage(viewModel = viewModel, nav = nav)
        }
    }

}


@Composable
fun ShowImage(viewModel: MyViewModel, nav: NavHostController) {
    val bitmap = remember {
        mutableStateOf(viewModel._bitmap.value)
    }
    viewModel._bitmap.observe(LocalLifecycleOwner.current) {
        if (bitmap.value !== it) {
            Log.e("", "ShowImage: ")
            bitmap.value = it
        }
    }
    Column(Modifier.fillMaxSize()) {
        val context = LocalContext.current
        val showDialog = remember {
            mutableStateOf(false)
        }
        Text(text = "选择一张图片进行黑白处理")
        Button(onClick = {
            val intent = Intent(Intent.ACTION_PICK, null)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(context as Activity, intent, 111, null)
        }) {
            Text(text = "从相册选择图片")
        }
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            model = bitmap.value,
            contentDescription = null,
            imageLoader = LocalContext.current.imageLoader

        )
        Button(onClick = {
            if (viewModel._bitmap.value != null) {
                viewModel.changeImageByC()
            } else {
                showDialog.value = true
            }
        }) {
            Text(text = "黑白处理")
        }
        if (showDialog.value) {
            AlertDialog(onDismissRequest = { showDialog.value = false },
                buttons = {
                    Button(
                        onClick = { showDialog.value = false },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .align(alignment = Alignment.CenterHorizontally)
                    ) {
                        Text(text = "好的")
                    }
                },
                title = { Text(text = "请选择一张图片!") },
                text = {
                    Text(text = "如果没有图片就没办法处理哦!")
                }


            )
        }

    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JNIComposeImageShopTheme {
        ShowImage(MyViewModel(), NavHostController(LocalContext.current))
    }
}