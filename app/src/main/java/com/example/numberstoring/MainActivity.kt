package com.example.numberstoring

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var numberInput: EditText
    private lateinit var storedNumberText: TextView
    private lateinit var averageResultText: TextView
    private val numberList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        numberInput = findViewById(R.id.numberInput)
        storedNumberText = findViewById(R.id.storedNumberText)
        averageResultText = findViewById(R.id.averageResultText)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val clearButton = findViewById<Button>(R.id.clearButton)
        val averageButton = findViewById<Button>(R.id.averageButton)

        // Display the stored number on startup
        displayStoredNumber()

        saveButton.setOnClickListener {
            saveNumber()
        }

        clearButton.setOnClickListener {
            clearNumber()
        }

        averageButton.setOnClickListener {
            calculateAverage()
        }
    }

    private fun saveNumber() {
        val inputText = numberInput.text.toString()
        if (inputText.isNotEmpty()) {
            val numberToSave = inputText.toInt()
            
            // Add to our list for average calculation
            numberList.add(numberToSave)
            
            val sharedPref = getPreferences(Context.MODE_PRIVATE)
            sharedPref.edit {
                putInt("saved_number", numberToSave)
            }
            
            displayStoredNumber()
            numberInput.text.clear()
        }
    }

    private fun clearNumber() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        sharedPref.edit {
            remove("saved_number")
        }
        numberList.clear()
        storedNumberText.text = getString(R.string.no_number_stored)
        averageResultText.text = ""
    }

    private fun calculateAverage() {
        if (numberList.isEmpty()) {
            averageResultText.text = getString(R.string.no_number_stored)
            return
        }

        val numbersArray = numberList.toIntArray()
        var sum = 0
        var index = 0
        
        // Using a while loop to add up all numbers as per requirement
        while (index < numbersArray.size) {
            sum += numbersArray[index]
            index++
        }

        val average = sum.toDouble() / numbersArray.size
        averageResultText.text = getString(R.string.average_result, average)
    }

    private fun displayStoredNumber() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val savedNumber = sharedPref.getInt("saved_number", -1)
        
        if (numberList.isNotEmpty()) {
            storedNumberText.text = getString(R.string.all_numbers, numberList.joinToString(", "))
        } else if (savedNumber != -1) {
            storedNumberText.text = getString(R.string.stored_number, savedNumber)
        } else {
            storedNumberText.text = getString(R.string.no_number_stored)
        }
    }
}
