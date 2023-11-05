package com.example.exchangeapp

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.SearchView
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import java.io.InputStreamReader
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var listExchangeRate = mutableListOf<ExchangeRate>()
    private var request = Array<Request>(1){
        Request("","","",
            listExchangeRate)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        fetchCurrencyData().start()

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.mainToolbar)
        setSupportActionBar(toolbar)
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
                request = Gson().fromJson<Array<Request>>(inputStreamReader, Array<Request>::class.java)
                updateUI()
                inputStreamReader.close()
                inputSystem.close()
            }
            else
            {
                Log.d("myTag","Error connection")
            }
        }
    }

    private fun updateUI() {
        runOnUiThread{
            kotlin.run {
                if (request != null) {
                    createTable(listOfData())
                }
            }
        }
    }

    private fun listOfData(): List<ExchangeRate>
    {
        val dataList = mutableListOf<ExchangeRate>()
        dataList.addAll(request[0].rates)
        val pln = ExchangeRate("polski złoty", "PLN",1.00)
        dataList.add(pln)
        return dataList
    }

    private fun createTable(list: List<ExchangeRate>)
    {
        val table = findViewById<TableLayout>(R.id.mainTable)
        list.forEach{
            val tr = layoutInflater.inflate(R.layout.field, null)
            val nameField = tr.findViewById<TextView>(R.id.nameField)
            val codeField = tr.findViewById<TextView>(R.id.codeField)
            val rateField = tr.findViewById<TextView>(R.id.rateField)
            nameField.text = it.currency
            codeField.text = it.code
            rateField.text = String.format("%.4f", it.mid)
            table.addView(tr)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        var itemView = item.itemId
        when(itemView){
            R.id.search ->{
                showSearchDialogWindow()
            }
            R.id.calculator ->{
                val intent = Intent(this, Calculator::class.java)
                val bundle = Bundle()
                bundle.putSerializable("dataList", listOfData() as Serializable)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
        return false
    }

    private fun showSearchDialogWindow()
    {
        val dialog = Dialog(this)
        val window = dialog.window
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.search)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.TOP)
        dialog.show()
        val searchView = dialog.findViewById<SearchView>(R.id.searchViewSearch)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }



    private fun filterList(query: String?)
    {
        val table = findViewById<TableLayout>(R.id.mainTable)
        val filteredList = listOfData().filter {
            it.currency.contains(query!!, ignoreCase = true) || it.code.contains(query!!, ignoreCase = true)
        }
        table.removeAllViews()
        createTable(filteredList)
    }

}
