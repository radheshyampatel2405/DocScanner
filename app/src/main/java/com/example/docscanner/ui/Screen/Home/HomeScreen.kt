package com.example.docscanner.Screen

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.docscanner.R
import com.example.docscanner.Utils.Resource
import com.example.docscanner.Utils.Screen.Home.Compontents.PdfLayout
import com.example.docscanner.Utils.copyPdf
import com.example.docscanner.Utils.getFileSize
import com.example.docscanner.Utils.showToast
import com.example.docscanner.data.models.PdfEntity
import com.example.docscanner.ui.Screen.common.Banner
import com.example.docscanner.ui.Screen.common.ErrorScreen
import com.example.docscanner.ui.Screen.common.LoadingScreen
import com.example.docscanner.ui.Screen.common.RenameDialog
import com.example.docscanner.ui.viewmodels.PdfViewModel
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter" , "RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(pdfViewModel : PdfViewModel)
{
    val activity = LocalContext.current as Activity

    val context = LocalContext.current

    val pdfState by pdfViewModel.pdfStateFlow.collectAsState()
//  val pdfState = remember { mutableStateListOf<PdfEntity>() }

    val messsage = pdfViewModel.message
    LaunchedEffect(Unit) {
        messsage.collect{
            when(it){
                is Resource.Error->{
                    context.showToast(it.message)
                }
                is Resource.Success-> {
                    context.showToast(it.data)
                }
                Resource.Idle-> { }
                Resource.Loading-> { }
            }
        }
    }

    val scannerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()){
       result ->
        if (result.resultCode == Activity.RESULT_OK){
            val scanningResult =
                    GmsDocumentScanningResult.fromActivityResultIntent(result.data)

            scanningResult?.pdf?.let {
                pdf ->
                Log.d("pdfName", pdf.uri.lastPathSegment.toString())

                val date = Date()

                val fileName = SimpleDateFormat(
                    "dd-MM-yyyy HH:mm:ss",
                    Locale.getDefault()) .format(date)+ ".pdf"

                copyPdf(
                    context, pdf.uri,
                    fileName
                       )

                val Entity = PdfEntity(
                    UUID.randomUUID().toString(),
                    fileName,
                    getFileSize(context, fileName),
                    date
                                      )
                pdfViewModel.insertPdf(Entity)
//              pdfState.add(Entity)

            }
        }
    }

    val scanner = remember {
        GmsDocumentScanning.getClient(
            GmsDocumentScannerOptions
                    .Builder()
                    .setGalleryImportAllowed(true)
                    .setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_PDF)
                    .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
                    .build()
                                    )
    }

    RenameDialog(pdfViewModel = pdfViewModel)

    LoadingScreen(pdfViewModel = pdfViewModel)

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.app_name) )},
                                          actions = {
                                             Switch(checked = pdfViewModel.isDarkMode,
                                                    onCheckedChange = {
                                                        pdfViewModel.isDarkMode = it

                                                    })
                                          })
                 },

    floatingActionButton = {
        ExtendedFloatingActionButton(onClick = {
            scanner.getStartScanIntent(activity).addOnSuccessListener{
                scannerLauncher.launch(
                    IntentSenderRequest.Builder(it).build()
                                      )
            }.addOnFailureListener {
                it.printStackTrace()
                context.showToast(it.message.toString())
            }
                                               },
                                     text = { Text(text = stringResource(R.string.scan)) },

                                     icon= {
                                         Icon(painter = painterResource(id = R.drawable.baseline_camera_alt_24 ),
                                              contentDescription = "Scan")
                                     }
                                    )
    }
            )

    { paddingValues ->

        Banner()
        pdfState.DisplayResult(

            onLoading ={
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            },

            onSuccess = { pdfList->

                if (pdfList.isEmpty())
                { ErrorScreen(message = "Please create a new pdf ") }

                else{

                    LazyColumn (modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues))
                    {
                        item { Banner() }
                        items(items = pdfList,
                              key = {pdfEntity->pdfEntity.id} )
                        { pdfEntity ->
                            PdfLayout(entity = pdfEntity , pdfViewModel = pdfViewModel)
                        }
                    }
                }
                        },

            onError = {ErrorScreen(message = it)}
                              )
    }
}