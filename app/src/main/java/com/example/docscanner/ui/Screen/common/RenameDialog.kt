package com.example.docscanner.ui.Screen.common

import android.content.ClipData
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.docscanner.R
import com.example.docscanner.Utils.delete
import com.example.docscanner.Utils.getFileUri
import com.example.docscanner.Utils.rename
import com.example.docscanner.Utils.showToast
import com.example.docscanner.ui.viewmodels.PdfViewModel
import java.util.Date

@Composable
fun RenameDialog(pdfViewModel : PdfViewModel)
{
    var newNameText by remember (pdfViewModel.currentPdfEntity) {
        mutableStateOf(pdfViewModel.currentPdfEntity?.name ?: "No Name")
    }

    val context = LocalContext.current

    if(pdfViewModel.showRenameDialog){
        Dialog(onDismissRequest = { pdfViewModel.showRenameDialog =false })
        {
            Surface(shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surface)
            {
                Column(modifier = Modifier.padding(16.dp),
                       horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Text(stringResource(id = R.string.rename_pdf),
                         style = MaterialTheme.typography.headlineSmall)

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(value = newNameText,
                                      onValueChange = {newText ->
                                          newNameText = newText
                                      },
                                      label = { Text(stringResource(id = R.string.pdf_name)) }
                                     )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(Modifier.fillMaxWidth()) {

//      This button is for share button
                        IconButton(onClick = {
                            pdfViewModel.currentPdfEntity?.let {
                                pdfViewModel.showRenameDialog = false
                                val getFileUrl = getFileUri(context, it.name)
                                val shareIntent = Intent(Intent.ACTION_SEND)
                                shareIntent.type = "application/pdf"
                                shareIntent.clipData = ClipData.newRawUri("",getFileUrl)
                                shareIntent.putExtra(Intent.EXTRA_STREAM, getFileUrl)
                                shareIntent.flags  =Intent.FLAG_GRANT_READ_URI_PERMISSION
                                context.startActivity(Intent.createChooser(shareIntent, "Share PDF"))

                            }
                        })
                        {
                            Icon(painterResource(id = R.drawable.baseline_share_24),
                                 contentDescription = "Share ")
                        }

//      This button is for delete button

                        IconButton(onClick = {
                            pdfViewModel.currentPdfEntity?.let {
                                pdfViewModel .showRenameDialog= false

                                if (delete(context,it.name)){
                                    pdfViewModel.deletePdf(it)
                                }
                                else{
                                    context.showToast("Something went wrong")
                                }

                            }
                        }
                                  )
                        {
                            Icon(painterResource(id = R.drawable.baseline_delete_24),
                                 contentDescription ="delete",
                                 tint = MaterialTheme.colorScheme.error)
                        }

                        Spacer(modifier = Modifier.width(5.dp))

//      This button is for cancel the renamePdf
                        OutlinedButton(onClick = {pdfViewModel.showRenameDialog = false}) {
                            Text(stringResource(id = R.string.cancel),
                                 color = Color.Red)
                        }

                        Spacer(modifier = Modifier.width(5.dp))

//      This button is for save the rename
                        OutlinedButton(onClick = {
                            pdfViewModel.currentPdfEntity?.let { pdf->
                                if (!pdf.name.equals(newNameText, true)) {
                                    pdfViewModel.showRenameDialog = false
                                    rename(context,
                                           newfile = pdf.name,
                                           oldfile = newNameText
                                          )
                                    val updatePdf = pdf.copy(
                                        name = newNameText, lastModifiedTime = Date()
                                                         )
                                    pdfViewModel.updatePdf(updatePdf)
                                }
                                else{ pdfViewModel.showRenameDialog =false }
                            }
                        })
                        {
                            Text(stringResource(id = R.string.save),
                                 color = Color.Blue) }
                    }
                }
            }
        }
    }
}