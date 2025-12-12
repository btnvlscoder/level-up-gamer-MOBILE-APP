package com.example.levelupgamermobile.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import com.example.levelupgamermobile.R
import java.io.File
import java.util.concurrent.Executor

@Composable
fun CameraScreen(
    onImageCaptured: (Uri) -> Unit
) {
    val context = LocalContext.current
    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCamPermission = granted
        }
    )
    LaunchedEffect(key1 = true) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    if (hasCamPermission) {
        CameraContent(onImageCaptured)
    }
}

@Composable
fun CameraContent(onImageCaptured: (Uri) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                PreviewView(context).apply {
                    controller = cameraController
                    cameraController.bindToLifecycle(lifecycleOwner)
                }
            }
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            onClick = {
                takePhoto(context, cameraController) { uri ->
                    onImageCaptured(uri)
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Camera,
                contentDescription = "Take photo"
            )
        }
    }
}

private fun takePhoto(
    context: Context,
    cameraController: LifecycleCameraController,
    onImageCaptured: (Uri) -> Unit
) {
    val file = File.createTempFile("im", "jpg", context.cacheDir)
    val authority = context.packageName + ".provider"
    val uri = FileProvider.getUriForFile(context, authority, file)

    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

    cameraController.takePicture(
        outputOptions,
        context.mainExecutor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                Log.d("Camera", "Success taking photo")
                onImageCaptured(uri)
            }

            override fun onError(ex: ImageCaptureException) {
                Log.e("Camera", "Error taking photo", ex)
            }
        }
    )
}

val Context.mainExecutor: Executor
    get() = ContextCompat.getMainExecutor(this)
