package com.example.docscanner.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docscanner.Utils.Resource
import com.example.docscanner.data.models.PdfEntity
import com.example.docscanner.data.repository.PdfRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PdfViewModel(application : Application): ViewModel()
{
    var isSplashScreen by mutableStateOf(false)
    var showRenameDialog by mutableStateOf(false)
    var loadingDialog by mutableStateOf(false)
    var isDarkMode by mutableStateOf(false)

    private val PdfRepository = PdfRepository(application)

    private val _pdfStateFlow =
            MutableStateFlow<Resource<List<PdfEntity>>>(Resource.Idle)

    val pdfStateFlow : StateFlow<Resource<List<PdfEntity>>>
        get()= _pdfStateFlow

    var currentPdfEntity : PdfEntity? by mutableStateOf(null)

    private val _message : Channel<Resource<String>> = Channel()
    val message = _message.receiveAsFlow()
    init
    {
        viewModelScope.launch{
            delay(2000)
            isSplashScreen = false
        }

        viewModelScope.launch {
            pdfStateFlow.collect {
                when(it){
                    is Resource.Error->
                    {
                        loadingDialog = false
                    }
                    Resource.Loading->{
                        loadingDialog = true
                    }
                    is Resource.Success->{
                        loadingDialog = false
                    }
                    Resource.Idle->{}
                }
            }
        }

        viewModelScope.launch ( Dispatchers.IO ){
            _pdfStateFlow.emit(Resource.Loading)

            PdfRepository.getPdfList().catch {
                _pdfStateFlow.emit(Resource.Error(it.message ?: "Unknown Error"))
                it.printStackTrace()
            }.collect{
               _pdfStateFlow.emit(Resource.Success(it))
            }
        }
    }

    fun insertPdf (pdfEntity : PdfEntity){
        viewModelScope.launch(Dispatchers.IO) {
            try
            {
                loadingDialog =true
//                _pdfStateFlow.emit(Resource.Loading)
                val result = PdfRepository.insertPdf(pdfEntity)
                if (result.toInt() != -1){
                    _message.send(Resource.Success("Successfully Inserted the pdf"))
                }
                else{
                    _message.send(Resource.Error("Not Inserted successfully"))
                }
            }catch (e:Exception){
                e.printStackTrace()
                _message.send(Resource.Error(e.message ?: "Something went wrong"))
            }

        }
    }

    fun updatePdf (pdfEntity : PdfEntity){
        viewModelScope.launch(Dispatchers.IO){
            try
            {
                loadingDialog = true
//                _pdfStateFlow.emit(Resource.Loading)
                PdfRepository.updatePdf(pdfEntity)
                _message.send(Resource.Success("Successfully Updated"))
            }
            catch (e:Exception)
            {
                e.printStackTrace()
                _message.send(Resource.Error(e.message ?: "can't update now"))
            }
        }
    }

    fun deletePdf (pdfEntity : PdfEntity){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadingDialog = true
//                _pdfStateFlow.emit(Resource.Loading)
                PdfRepository.deletePdf(pdfEntity)
                _message.send(Resource.Success("Successfully Deleted"))
            }
            catch (e:Exception){
                e.printStackTrace()
                _message.send(Resource.Error(e.message ?: "Something went wrong"))
            }
        }
    }

}