package com.omidrezabagherian.datastoretutorials

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.omidrezabagherian.datastoretutorials.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var settingDataStore: SettingDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        settingDataStore = SettingDataStore(this)

        checkingThemeMode()

        settingThemeMode()
    }

    private fun settingThemeMode() {
        binding.swUIMode.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                if (isChecked) {
                    viewModel.setTheme(true)
                } else {
                    viewModel.setTheme(false)
                }
            }
        }
    }

    private fun checkingThemeMode() {
        viewModel.getTheme.observe(this@MainActivity) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.swUIMode.isChecked = true
                binding.swUIMode.text = getString(R.string.text_night_mode)
                binding.lottieUIMode.setAnimation(R.raw.moon)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.swUIMode.isChecked = false
                binding.swUIMode.text = getString(R.string.text_light_mode)
                binding.lottieUIMode.setAnimation(R.raw.sun)
            }
        }
    }
}