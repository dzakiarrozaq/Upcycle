package com.bangkit.upcycle.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.upcycle.R
import com.bangkit.upcycle.databinding.ActivityMainBinding
import com.bangkit.upcycle.databinding.ActivityRecycleBagBinding
import com.bangkit.upcycle.databinding.FragmentCameraBinding

class RecycleBag : AppCompatActivity() {
    private lateinit var binding: ActivityRecycleBagBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecycleBagBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
//        val layoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = layoutManager
//
////        val jsonData = loadJsonFromFile(this)
//
//        val predictionDataAdapter = AdapterRcycleBag(jsonData)
//        recyclerView.adapter = predictionDataAdapter

    }
    private fun loadJsonFromFile(context: Context): String {
        return try {
            val fileName = "prediction_data.json"
            context.openFileInput(fileName).bufferedReader().use { reader ->
                reader.readText()
            }
        } catch (e: Exception) {
            Log.e("LoadData", "Error loading data: $e")
            ""
        }
    }


}