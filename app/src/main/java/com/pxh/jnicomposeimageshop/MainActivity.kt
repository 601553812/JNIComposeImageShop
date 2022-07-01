package com.pxh.jnicomposeimageshop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
                myViewModel._bitmap.value = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}

@Composable
fun Greeting(viewModel: MyViewModel) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "Menu") {
        composable("Menu") {
            Menu(viewModel = viewModel, nav = nav)
        }
        composable("ShowImage") {
            ShowImage(viewModel = viewModel)
        }
    }

}

@Composable
fun Menu(viewModel: MyViewModel, nav: NavHostController) {
    val context = LocalContext.current
    Row(modifier = Modifier.fillMaxSize()) {
        Text(text = "选择一张图片进行黑白处理")
        Button(onClick = {
            val intent = Intent(Intent.ACTION_PICK, null)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(context as Activity, intent, 111, null)
        }) {
            Text(text = "从相册选择图片")
        }
    }
    viewModel._bitmap.observe(LocalLifecycleOwner.current) {
        nav.navigate("ShowImage")
    }


}


@Composable
fun ShowImage(viewModel: MyViewModel) {
    val bitmap = remember {
        mutableStateOf(viewModel._bitmap.value)
    }
    viewModel._bitmap.observe(LocalLifecycleOwner.current) {
        bitmap.value = it
    }
    Column(Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = bitmap.value,
            contentDescription = null
        )
        Button(onClick = { viewModel.changeImageByC() }) {
            Text(text = "黑白处理")
        }
    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JNIComposeImageShopTheme {
        Menu(MyViewModel(), NavHostController(LocalContext.current))
    }
}