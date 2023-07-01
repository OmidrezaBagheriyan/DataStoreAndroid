package com.omidrezabagherian.datastoretutorials

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = SettingDataStore(application)

    val getTheme = dataStore.getTheme().asLiveData(Dispatchers.IO)

    val getLang = dataStore.getLang().asLiveData(Dispatchers.IO)

    fun setTheme(darkMode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setTheme(darkMode)
        }
    }

    fun setLang(langMode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setLang(langMode)
        }
    }
}