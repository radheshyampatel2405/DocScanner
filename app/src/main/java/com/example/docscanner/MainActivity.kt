package com.example.docscanner

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.docscanner.Screen.HomeScreen
import com.example.docscanner.ui.theme.DocScannerTheme
import com.example.docscanner.ui.viewmodels.PdfViewModel

class MainActivity : ComponentActivity()
{
    private val pdfViewModel by viewModels <PdfViewModel>
    {
        viewModelFactory {
            addInitializer(PdfViewModel::class){
                PdfViewModel(application)
            }
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState : Bundle?)
    {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            splashScreen.setKeepOnScreenCondition {pdfViewModel.isSplashScreen}
            DocScannerTheme(pdfViewModel.isDarkMode, false) {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    HomeScreen(pdfViewModel = pdfViewModel)
                }
            }
        }
    }
}
