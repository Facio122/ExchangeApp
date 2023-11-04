package com.example.exchangeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    val fieldList = mutableListOf<Field>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        fetchCurrencyData().start()
    }


    private fun fetchCurrencyData(): Thread
    {
        return Thread {
            val url = URL("http://api.nbp.pl/api/exchangerates/tables/A/?format=json")
            val connection = url.openConnection() as HttpURLConnection
            if(connection.responseCode === 200)
            {
                var inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val request = Gson().fromJson<Array<Request>>(inputStreamReader, Array<Request>::class.java)
                Log.d("myTag", inputSystem.toString())
                updateUI(request)
                inputStreamReader.close()
                inputSystem.close()
            }
            else
            {
                Log.d("myTag","Error connection")
            }
        }
    }

    private fun updateUI(request: Array<Request>) {
        runOnUiThread{
            kotlin.run {
                if (request != null) {
                    Log.d("myTag", request[0].table)
                }
            }
        }
    }
}