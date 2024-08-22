package com.example.docscanner.Utils

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream


fun Context.showToast( message : String)
{
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

// This file is for copy pdf file to the app directory
fun copyPdf(context : Context, pdfUri : Uri, destinationFileName : String)
{
    val inputStream = context.contentResolver.openInputStream(pdfUri)
    val outputFile = File(context.filesDir, destinationFileName)

    FileOutputStream(outputFile).use { inputStream?.copyTo(it)}

}

fun getFileUri (context : Context, filename: String): Uri{
    val file = File(context.filesDir, filename)
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)


}

fun delete(context : Context,filename : String):Boolean{
    val file = File(context.filesDir, filename)
    return file.deleteRecursively()
}

fun rename(context : Context, newfile : String, oldfile : String){
    val oldFile = File(context.filesDir, oldfile)
    val newFile = File(context.filesDir, newfile)
    oldFile.renameTo(newFile)
}

fun getFileSize (context : Context,filename : String):String
{
    val file = File(context.filesDir,filename)
    val fileSizeBytes = file.length()
    val fileSizeKB = fileSizeBytes/1024
    return if (fileSizeKB > 1024 ){
        val fileSizeMB = fileSizeKB/ 1024
        "$fileSizeMB MB"
    }
    else { "$fileSizeKB KB" }
}
