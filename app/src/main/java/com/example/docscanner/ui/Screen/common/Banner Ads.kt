package com.example.docscanner.ui.Screen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView


// This is banner ads file
@Composable
fun Banner()
{
//    Spacer(modifier = Modifier.size(22.dp))
        AndroidView(modifier = Modifier.fillMaxWidth().background(color = Color.Red, shape = RectangleShape),
                    factory = { context ->
                        AdView(context).apply {
                            setAdSize(AdSize.BANNER)
                            adUnitId = "ca-app-pub-3940256099942544/9214589741"
                            loadAd(AdRequest.Builder().build())
                        }
                    }
                   )


}
