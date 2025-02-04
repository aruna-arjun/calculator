


package com.example.calculator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable
import java.lang.Exception
import kotlin.math.sqrt

class CalculatorViewModel : ViewModel() {

    private val _equationText = MutableLiveData("")
    val equationText: LiveData<String> = _equationText

    private val _resultText = MutableLiveData("0")
    val resultText: LiveData<String> = _resultText

    private var lastResult: String = "0" // Store the last calculated result

    fun onButtonClick(btn: String) {
        Log.i("Clicked Button", btn)

        _equationText.value?.let { equation ->
            when (btn) {
                "AC" -> {
                    _equationText.value = ""
                    _resultText.value = "0"
                    lastResult = "0" // Reset last result
                    return
                }
                "C" -> {
                    if (equation.isNotEmpty()) {
                        _equationText.value = equation.substring(0, equation.length - 1)
                    }
                    return
                }
                "=" -> {
                    _equationText.value = lastResult // Display the last result.
                    return
                }
                "sqrt" -> {
                    try {
                        val num = equation.toDouble()
                        val result = sqrt(num)
                        _resultText.value = result.toString()
                        lastResult = result.toString() // Update the last result
                        _equationText.value = "" // Clear equation after sqrt
                    } catch (e: Exception) {
                        _resultText.value = "Error"
                    }
                    return
                }
                "%" -> {
                    try {
                        val num = equation.toDouble()
                        val result = num / 100
                        _resultText.value = result.toString()
                        lastResult = result.toString() // Update the last result
                        _equationText.value = "" // Clear equation after %
                    } catch (e: Exception) {
                        _resultText.value = "Error"
                    }
                    return
                }
                "+/-" ->{
                    try {
                        val num = equation.toDouble()
                        val result = -num
                        _resultText.value = result.toString()
                        lastResult = result.toString() // Update the last result
                        _equationText.value = "" // Clear equation after +/-
                    } catch (e: Exception) {
                        _resultText.value = "Error"
                    }
                    return
                }


                else -> {
                    _equationText.value = equation + btn
                }
            }


            // Calculate Result (only for basic operators)
            try {
                if (btn !in listOf("sqrt", "%", "+/-")) { // Don't recalculate after special functions
                    _resultText.value = calculateResult(_equationText.value.toString())
                    lastResult = _resultText.value.toString() // Update last result
                }

            } catch (e: Exception) {
                _resultText.value = "Error"
            }
        }
    }


    private fun calculateResult(equation: String): String {
        return try {
            val context: Context = Context.enter()
            context.optimizationLevel = -1
            val scriptable: Scriptable = context.initStandardObjects()
            var finalResult = context.evaluateString(scriptable, equation, "Javascript", 1, null).toString()

            if (finalResult.endsWith(".0")) {
                finalResult = finalResult.replace(".0", "")
            }
            finalResult
        } catch (e: Exception) {
            "Error" // Handle calculation errors
        }
    }
}


