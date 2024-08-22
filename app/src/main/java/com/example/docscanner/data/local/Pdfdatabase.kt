package com.example.docscanner.data.local


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.docscanner.data.local.converter.DateConverter
import com.example.docscanner.data.local.dao.PdfDao
import com.example.docscanner.data.models.PdfEntity

@Database(
    entities = [PdfEntity::class] ,
    version = 1 ,
    exportSchema = false
         )

@TypeConverters (DateConverter::class)

abstract class Pdfdatabase: RoomDatabase()
{
    abstract val pdfDao : PdfDao

    companion object
    {
        @Volatile
        private var INSTANCES : Pdfdatabase? = null

        fun getInstance(context : Context): Pdfdatabase
        {
            synchronized(this){
                return INSTANCES ?: Room.databaseBuilder(
                    context.applicationContext ,
                    Pdfdatabase::class.java ,
                    "pdf_db"
                                                        ).build().also {
                                                            INSTANCES = it
                                                        }
            }

        }
    }
}