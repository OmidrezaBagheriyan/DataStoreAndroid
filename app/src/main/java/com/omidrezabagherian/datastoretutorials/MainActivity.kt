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
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var settingDataStore: SettingDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        settingDataStore = SettingDataStore(this)

        Thread {
            runOnUiThread {
                configSeekBarLang()

                configSeekBarTheme()

                checkThemeMode()

                checkLangMode()
            }
        }.start()

    }

    private fun setLanguage(value: String) {
        val locale = Locale(value)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }

    private fun checkLangDefault() {
        when (Locale.getDefault().language) {
            "en" -> {
                setLanguage("en")
            }

            "fa" -> {
                setLanguage("fa")
            }
        }
    }

    private fun checkLangMode() {
        viewModel.getLang.observe(this@MainActivity) { langMode ->
            when (langMode) {
                Lang.DEFAULT.name -> {
                    Log.i("ANDROID", Lang.DEFAULT.name)
                    binding.sbLangMode.progress = 0
                    checkLangDefault()
                }

                Lang.PERSIAN.name -> {
                    Log.i("ANDROID", Lang.PERSIAN.name)
                    binding.sbLangMode.progress = 1
                    setLanguage("fa")
                }

                Lang.ENGLISH.name -> {
                    Log.i("ANDROID", Lang.ENGLISH.name)
                    binding.sbLangMode.progress = 2
                    setLanguage("en")
                }
            }
        }
    }


    private fun configSeekBarLang() {
        binding.apply {
            sbLangMode.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    when (progress) {
                        0 -> {
                            viewModel.setLang(Lang.DEFAULT.name)
                        }

                        1 -> {
                            viewModel.setLang(Lang.PERSIAN.name)
                        }

                        2 -> {
                            viewModel.setLang(Lang.ENGLISH.name)
                        }
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            })
        }
    }

    private fun checkThemeDefault() {
        if (resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
        ) {
            binding.lottieUIMode.setAnimation(R.raw.moon)
        } else {
            binding.lottieUIMode.setAnimation(R.raw.sun)
        }
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