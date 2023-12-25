package com.example.flightsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.app.DatePickerDialog
import android.widget.EditText
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var originDateEditText: EditText
    private lateinit var wheretoDateEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val originCitySpinner: Spinner = findViewById(R.id.originCity)
        val wheretoCitySpinner: Spinner = findViewById(R.id.wheretoCity)
        val citiesArray = resources.getStringArray(R.array.cities)
        originDateEditText = findViewById(R.id.originDate)
        wheretoDateEditText = findViewById(R.id.wheretoDate)


        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, citiesArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        originCitySpinner.adapter = adapter
        wheretoCitySpinner.adapter = adapter

        originDateEditText.setOnClickListener {
            showDatePickerDialog(originDateEditText)
        }

        wheretoDateEditText.setOnClickListener {
            showDatePickerDialog(wheretoDateEditText)
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                editText.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
}
