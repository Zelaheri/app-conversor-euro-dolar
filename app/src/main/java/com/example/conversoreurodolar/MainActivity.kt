package com.example.conversoreurodolar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.example.conversoreurodolar.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.security.Key

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dollar = binding.dolarBox
        val euro = binding.euroBox
        val appLogo = binding.appLogo

        // Exibe "$ " quando em foco
        // e esconde quando em desfoco
        dollar.setOnFocusChangeListener { _, _ ->
            if (dollar.isFocused && dollar.text.isNullOrEmpty()) {
                appLogo.updateLayoutParams {
                    height = 0
                }
                dollar.setText("$ ")
            } else if (dollar.text.trim('$').trim(' ').isEmpty()) {
                appLogo.updateLayoutParams {
                    height = 550
                }
                dollar.setText("")
            }
            return@setOnFocusChangeListener
        }

        // Exibe "€ " quando em foco
        // e esconde quando em desfoco
        euro.setOnFocusChangeListener { _, _ ->
            if (euro.isFocused && euro.text.isNullOrEmpty()) {
                appLogo.updateLayoutParams {
                    height = 0
                }
                euro.setText("€ ")
            } else if (euro.text.trim('€').trim(' ').isEmpty()) {
                appLogo.updateLayoutParams {
                    height = 550
                }
                euro.setText("")
            }
            return@setOnFocusChangeListener
        }


        dollar.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP) {
                // Como sempre haverá, no mínimo, 2 caracteres
                if (dollar.text.length > 2) {
                    // Conversão somente do valor numérico
                    euro.setText("€ " +
                        dollar.text.toString()
                            .trim('$')
                            .trim(' ')
                            .toDouble()
                            .times(0.92)
                            .format(2)
                    )
                } else {
                    // Normalizando a exibição padrão
                    dollar.setText("$ ")
                    euro.setText("")
                }
                // Retornando o cursor para o final
                dollar.setSelection(dollar.length())
                return@OnKeyListener true
            }
            false
        })

        euro.setOnKeyListener(View.OnKeyListener { _, _, event ->
            if (event.action == KeyEvent.ACTION_UP) {
                // Como sempre haverá, no mínimo, 2 caracteres
                if (euro.text.length > 2) {
                    // Conversão somente do valor numérico
                    dollar.setText("$ " +
                            euro.text.toString()
                                .trim('€')
                                .trim(' ')
                                .toDouble()
                                .times(1.09)
                                .format(2)
                    )
                } else {
                    // Normalizando a exibição padrão
                    euro.setText("€ ")
                    dollar.setText("")
                }
                // Retornando o cursor para o final
                euro.setSelection(euro.length())
                return@OnKeyListener true
            }
            false
        })

        // Normalização da exibição padrão
        binding.main.setOnClickListener {
            dollar.clearFocus()
            euro.clearFocus()
            it.hideKeyboard()
        }
    }

    fun Double.format(digits: Int) = "%.${digits}f".format(this)

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}