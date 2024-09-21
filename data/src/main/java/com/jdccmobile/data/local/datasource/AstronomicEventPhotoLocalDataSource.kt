package com.jdccmobile.data.local.datasource

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.jdccmobile.data.local.dao.AstronomicEventPhotoDao
import com.jdccmobile.data.local.model.toDb
import com.jdccmobile.data.local.model.toDomain
import com.jdccmobile.data.utils.toMyError
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.model.AstronomicEventPhoto
import com.jdccmobile.domain.model.MyError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalCoroutinesApi::class)
class AstronomicEventPhotoLocalDataSource(
    private val astronomicEventPhotoDao: AstronomicEventPhotoDao
) {

    fun getPhotosByEvent(eventId: AstronomicEventId): Flow<List<AstronomicEventPhoto>> =
        astronomicEventPhotoDao.getPhotosByEvent(eventId.value)
            .mapLatest { photos -> photos.map { it.toDomain() } }

    suspend fun insertPhoto(photo: AstronomicEventPhoto, file: File, imageBitmap: ByteArray): Either<MyError, Unit> =
        catch {
            FileOutputStream(file).use { out ->
                out.write(imageBitmap)
            }
            astronomicEventPhotoDao.insertPhoto(photo.toDb())
        }.mapLeft { it.toMyError() }

    suspend fun deletePhoto(photo: AstronomicEventPhoto): Either<MyError, Unit> =
        catch {
            val fileToDelete = File(photo.filePath)
            if(fileToDelete.exists()) fileToDelete.delete()
            astronomicEventPhotoDao.deletePhoto(photo.toDb())
        }.mapLeft { it.toMyError() }

}
