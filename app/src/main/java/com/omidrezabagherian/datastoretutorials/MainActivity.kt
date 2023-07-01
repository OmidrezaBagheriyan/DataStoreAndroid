package com.omidrezabagherian.datastoretutorials

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.omidrezabagherian.datastoretutorials.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var settingDataStore: SettingDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        settingDataStore = SettingDataStore(this)

        checkThemeMode()

        configSeekBarTheme()
    }

    private fun checkThemeDefault() {
        Thread {
            runOnUiThread {
                if (resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
                ) {
                    binding.lottieUIMode.setAnimation(R.raw.moon)
                } else {
                    binding.lottieUIMode.setAnimation(R.raw.sun)
                }
            }
        }.start()
    }

    private fun checkThemeMode() {
        viewModel.getTheme.observe(this@MainActivity) { darkMode ->
            when (darkMode) {
                Theme.DEFAULT.name -> {
                    binding.sbUIMode.progress = 0
                    checkThemeDefault()
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }

                Theme.LIGHT.name -> {
                    binding.sbUIMode.progress = 1
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.lottieUIMode.setAnimation(R.raw.sun)
                }

                Theme.NIGHT.name -> {
                    binding.sbUIMode.progress = 2
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.lottieUIMode.setAnimation(R.raw.moon)
                }
            }
        }
        binding.sbLangMode.progress = 1
    }

    private fun configSeekBarTheme() {
        binding.apply {
            sbUIMode.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    when (progress) {
                        0 -> {
                            viewModel.setTheme(Theme.DEFAULT.name)
                        }

                        1 -> {
                            viewModel.setTheme(Theme.LIGHT.name)
                        }

                        2 -> {
                            viewModel.setTheme(Theme.NIGHT.name)
                        }
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }

            })
        }
    }
}