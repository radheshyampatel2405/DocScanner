package com.example.docscanner.Utils.Screen.Home.Compontents

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.docscanner.R
import com.example.docscanner.Utils.getFileUri
import com.example.docscanner.data.models.PdfEntity
import com.example.docscanner.ui.viewmodels.PdfViewModel
import java.util.Locale

@Composable
fun PdfLayout(entity : PdfEntity , pdfViewModel : PdfViewModel)
{

    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    Card(
        onClick = {
            val getFileUri = getFileUri( context, entity.name)
            val browserIntent = Intent(Intent.ACTION_VIEW,getFileUri)
            browserIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            activity.startActivity(browserIntent)
                  },
        modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp))
    {
        Row(modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
            ,verticalAlignment = Alignment.CenterVertically)
        {
            Icon(
                modifier = Modifier.size(80.dp),
                painter = painterResource(id = R.drawable.pdf) ,
                 contentDescription = "Pdf Icon",
                 tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f))
            {

                Text(text = "Name: ${entity.name}",
                     style = MaterialTheme.typography.bodyLarge,
                     maxLines = 2,
                     overflow = TextOverflow.Ellipsis)

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = "Date: ${SimpleDateFormat("dd-MMM-yyyy HH:mm a" ,
                     Locale.getDefault()).format(entity.lastModifiedTime) }"
                     ,style = MaterialTheme.typography.bodyMedium )

                Text(text = "Size: ${entity.size}",
                     style = MaterialTheme.typography.bodyMedium )
            }

            IconButton(onClick = {
                pdfViewModel.currentPdfEntity = entity
                pdfViewModel.showRenameDialog = true }) {
                Icon(imageVector = Icons.Default.MoreVert , contentDescription = "More option")
            }
        }
    }
}