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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onPhotoTaken: (AstronomicEventPhotoDb) -> Unit,
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
    Box(modifier = modifier.fillMaxSize()) {
        CameraView(controller = controller)
        CameraButtons( controller = controller,  context = context, onPhotoTakenToDb = onPhotoTaken, eventId = eventId.value)
    }
}

@Composable
private fun CameraView(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        },
    )
}

@Composable
private fun BoxScope.CameraButtons(
    controller: LifecycleCameraController,
    context: Context,
    onPhotoTakenToDb: (AstronomicEventPhotoDb) -> Unit,
    modifier: Modifier = Modifier,
    eventId: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
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
        IconButton(
            onClick = {
                takePhoto(
                    controller = controller,
                    onPhotoTaken = { },
                    onPhotoTakenToDb = onPhotoTakenToDb,
                    context = context,
                    eventId
                )
            },
        ) {
            Icon(
                painterResource(R.drawable.ic_photo_camera),
                contentDescription = null,
                modifier = Modifier.size(Dimens.minTouchSize),
            )
        }
    }
}

// @Composable
// fun PhotoBottomSheetContent(
//    bitmaps: List<Bitmap>,
//    modifier: Modifier = Modifier,
// ) {
//    Box(modifier = Modifier.fillMaxSize()) {
//        LazyColumn(
//            contentPadding = PaddingValues(16.dp),
//            modifier = modifier,
//        ) {
//            items(bitmaps) {
//                Image(bitmap = it.asImageBitmap(), contentDescription = null)
//            }
//        }
//    }
// }

private fun takePhoto(
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
    onPhotoTakenToDb: (AstronomicEventPhotoDb) -> Unit,
    context: Context,
    eventId: String, // Agrega el ID del evento astronómico
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
                val filePath = savePhotoLocally(context, rotatedBitmap)
                onPhotoTaken(rotatedBitmap)

                // Guarda la foto en la base de datos
                onPhotoTakenToDb(
                    AstronomicEventPhotoDb(
                        photoId = UUID.randomUUID().toString(), // Genera un ID único
                        eventId = eventId,
                        filePath = filePath,
                    ),
                )
//                savePhotoToDatabase(context, eventId, filePath)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Error", exception)
            }
        },
    )
}


private fun savePhotoLocally(context: Context, bitmap: Bitmap): String {
    val fileName = "photo_${System.currentTimeMillis()}.jpg"
    val file = File(context.filesDir, fileName)
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    }
    return file.absolutePath // Retorna la ruta del archivo guardado
}

private fun savePhotoToDatabase(context: Context, eventId: String, filePath: String) {
    val photo = AstronomicEventPhotoDb(
        photoId = UUID.randomUUID().toString(), // Genera un ID único
        eventId = eventId,
        filePath = filePath,
    )

    // Inserta el objeto en la base de datos
//    val db = AppDatabase.getInstance(context) // Supone que tienes una instancia de Room
//    db.astronomicEventPhotoDao().insert(photo)
}

