package com.jdccmobile.nasapi.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.jdccmobile.data.local.model.AstronomicEventPhotoDb
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.nasapi.R
import com.jdccmobile.nasapi.ui.theme.Dimens
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

@Composable
fun Camera(
    eventId: AstronomicEventId,
    onPhotoTakenToDb: (AstronomicEventPhotoDb) -> Unit,
    onCloseCamera: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE,
            )
        }
    }
    var previewPhoto by remember { mutableStateOf<Bitmap?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        if (previewPhoto == null) {
            CameraView(
                onCloseCamera = onCloseCamera,
                onShowPreview = { previewPhoto = it },
                controller = controller,
                context = context,
            )
        } else {
            PhotoPreview(
                eventId = eventId,
                previewPhoto = previewPhoto!!,
                onPhotoTakenToDb = onPhotoTakenToDb,
                onRemakePhoto = { previewPhoto = null },
                context = context,
            )
        }
    }
}

@Composable
private fun CameraView(
    onCloseCamera: () -> Unit,
    onShowPreview: (Bitmap) -> Unit,
    controller: LifecycleCameraController,
    context: Context,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = {
                PreviewView(it).apply {
                    this.controller = controller
                    controller.bindToLifecycle(lifecycleOwner)
                }
            },
        )
        MainButtons(
            onCloseCamera = onCloseCamera,
            onShowPreview = { onShowPreview(it) },
            controller = controller,
            context = context,
        )
    }
}

@Composable
private fun PhotoPreview(
    eventId: AstronomicEventId,
    previewPhoto: Bitmap,
    onPhotoTakenToDb: (AstronomicEventPhotoDb) -> Unit,
    onRemakePhoto: () -> Unit,
    context: Context,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            bitmap = previewPhoto.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        PreviewButtons(
            eventId = eventId.value,
            previewPhoto = previewPhoto,
            onPhotoTakenToDb = onPhotoTakenToDb,
            onRemakePhoto = onRemakePhoto,
            context = context,
        )
    }
}

@Composable
private fun BoxScope.MainButtons(
    onCloseCamera: () -> Unit,
    onShowPreview: (Bitmap) -> Unit,
    context: Context,
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        IconButton(
            onClick = { onCloseCamera() },
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier.size(Dimens.minTouchSize),
            )
        }
        IconButton(
            onClick = {
                takePhoto(
                    onPhotoTaken = { onShowPreview(it) },
                    controller = controller,
                    context = context,
                )
            },
        ) {
            Icon(
                painterResource(R.drawable.ic_photo_camera),
                contentDescription = null,
                modifier = Modifier.size(Dimens.minTouchSize),
            )
        }
        IconButton(
            onClick = {
                controller.cameraSelector =
                    if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    }
            },
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_camera_switch),
                contentDescription = null,
                modifier = Modifier.size(Dimens.minTouchSize),
            )
        }
    }
}

@Composable
private fun BoxScope.PreviewButtons(
    eventId: String,
    previewPhoto: Bitmap,
    onPhotoTakenToDb: (AstronomicEventPhotoDb) -> Unit,
    onRemakePhoto: () -> Unit,
    context: Context,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        IconButton(
            onClick = { onRemakePhoto() },
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                modifier = Modifier.size(Dimens.minTouchSize),
            )
        }
        IconButton(
            onClick = {
                savePhotoLocally(
                    eventId,
                    previewPhoto,
                    onPhotoTakenToDb,
                    context,
                )
            },
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(Dimens.minTouchSize),
            )
        }
    }
}

private fun takePhoto(
    onPhotoTaken: (Bitmap) -> Unit,
    controller: LifecycleCameraController,
    context: Context,
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,
                    0,
                    image.width,
                    image.height,
                    matrix,
                    true,
                )
                onPhotoTaken(rotatedBitmap)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Error", exception)
            }
        },
    )
}


private fun savePhotoLocally(
    eventId: String,
    previewImage: Bitmap,
    onPhotoTakenToDb: (AstronomicEventPhotoDb) -> Unit,
    context: Context,
) {
    val fileName = "photo_${System.currentTimeMillis()}.jpg"
    val file = File(context.filesDir, fileName)
    FileOutputStream(file).use { out ->
        previewImage.compress(Bitmap.CompressFormat.JPEG, 100, out)
    }
    onPhotoTakenToDb(
        AstronomicEventPhotoDb(
            photoId = UUID.randomUUID().toString(), // Genera un ID único
            eventId = eventId,
            filePath = file.absolutePath,
        ),
    )
}
