package com.example.docscanner.ui.Screen.common


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ErrorScreen(message : String)
{
    val offset = Offset(5.0f, 10.0f)

        Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center)
    {

        Column(modifier = Modifier.fillMaxWidth(),
               verticalArrangement = Arrangement.Center,
               horizontalAlignment = Alignment.CenterHorizontally)
        {

            Banner()
            Column(Modifier.width(400.dp)) {
                Text(
                    message,
                    modifier = Modifier.basicMarquee(),
                    style = TextStyle(
                        fontSize = 44.sp,
                        shadow = Shadow(
                            color = Color.Red, offset = offset, blurRadius = 3f
                    ))
                    )
            }
        }
    }
}