package com.example.exchangeapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View.OnCreateContextMenuListener
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable

@Suppress("DEPRECATION")
class Calculator : AppCompatActivity() {

    private var dataList = mutableListOf<ExchangeRate>()
    private var fromCurrency = "USD"
    private var toCurrency = "EUR"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)


        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.calculatorToolbar)
        setSupportActionBar(toolbar)

        val imageBtnFromCurr = findViewById<ImageButton>(R.id.setFromCurrencyBtn)
        val imageBtnToCurr = findViewById<ImageButton>(R.id.setToCurrencyBtn)
        val convertBtn = findViewById<Button>(R.id.convertBtn)

        val receivedBundle = intent.extras
        if(receivedBundle != null)
        {
            dataList = (receivedBundle.getSerializable("dataList") as List<ExchangeRate>).toMutableList()
        }

        imageBtnFromCurr.setOnClickListener{
            showSearchWindow(1)
        }

        imageBtnToCurr.setOnClickListener{
            showSearchWindow(2)
        }

        convertBtn.setOnClickListener{
            onClick()
        }

    }

    private fun onClick()
    {
        val editText = findViewById<EditText>(R.id.editTextNumberSigned).text.toString().trim()
        var quantityDouble: Double = if(editText.isEmpty())
            0.0
        else {
            editText.toDouble()
        }

        val fromCash = dataList.find {
            it.code == fromCurrency
        }
        val toCash = dataList.find {
            it.code == toCurrency
        }

        val result = quantityDouble * (fromCash!!.mid / toCash!!.mid)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)
        resultTextView.text = String.format("%.2f", result) + " " + toCash.code
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.calculatormenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        var itemView = item.itemId
        when(itemView){
            R.id.switchCalculator ->{
                var temp = fromCurrency
                fromCurrency = toCurrency
                toCurrency = temp
                findViewById<TextView>(R.id.fromCurrencyTextView).text = fromCurrency
                findViewById<TextView>(R.id.toCurrencyTextViewTop).text = toCurrency
                findViewById<TextView>(R.id.toCurrencyTextView).text = fromCurrency
                onClick()
            }
            R.id.back ->{
                finish()
            }
        }
        return false
    }

    private fun showSearchWindow(option: Int?)
    {
        val dialog = Dialog(this)
        val window = dialog.window
        window?.requestFeature(Window.FEATURE_NO_TITLE)
        window?.setContentView(R.layout.searchcalculator)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.TOP)
        dialog.show()
        val searchView = dialog.findViewById<SearchView>(R.id.searchRecyclerView)

        var recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView)
        var recyclerAdapter = RecyclerAdapter(dataList)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.onItemClick = {
            val fromCurrencyText = findViewById<TextView>(R.id.fromCurrencyTextView)
            val toCurrencyText = findViewById<TextView>(R.id.toCurrencyTextView)
            val toCurrencyTextTop = findViewById<TextView>(R.id.toCurrencyTextViewTop)
            if(option == 1)
            {
                fromCurrency = it.code
                fromCurrencyText.text = it.code
                toCurrencyText.text = it.code
            }
            else if(option == 2)
            {
                toCurrency = it.code
                toCurrencyTextTop.text = it.code
            }
            dialog.dismiss()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    recyclerAdapter.setFilterItemList(filteredList(newText))
                    recyclerView.adapter = recyclerAdapter
                }
                return true
            }
        })
    }

    private fun filteredList(query: String?) : List<ExchangeRate>
    {
        val result = dataList.filter {
            it.code.contains(query!!, true) || it.currency.contains(query!!, true)
        }
        return result
    }
}


