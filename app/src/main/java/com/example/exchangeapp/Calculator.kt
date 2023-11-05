package com.example.exchangeapp

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
import android.widget.ImageButton
import android.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable

@Suppress("DEPRECATION")
class Calculator : AppCompatActivity() {

    var dataList = mutableListOf<ExchangeRate>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)


        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.calculatorToolbar)
        setSupportActionBar(toolbar)

        val imageBtnFromCurr = findViewById<ImageButton>(R.id.setFromCurrencyBtn)
        val imageBtnToCurr = findViewById<ImageButton>(R.id.setToCurrencyBtn)

        val receivedBundle = intent.extras
        if(receivedBundle != null)
        {
            dataList = (receivedBundle.getSerializable("dataList") as List<ExchangeRate>).toMutableList()
        }

        imageBtnFromCurr.setOnClickListener{
            showSearchWindow()
        }

        imageBtnToCurr.setOnClickListener{
            showSearchWindow()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.calculatormenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        var itemView = item.itemId
        when(itemView){
            R.id.switchCalculator ->{
            }
            R.id.back ->{
                finish()
            }
        }
        return false
    }

    private fun showSearchWindow()
    {
        val dialog = Dialog(this)
        val window = dialog.window
        window?.requestFeature(Window.FEATURE_NO_TITLE)
        window?.setContentView(R.layout.searchcalculator)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.TOP)
        dialog.show()
        val searchView = dialog.findViewById<SearchView>(R.id.searchRecyclerView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filteredList(newText)
                return true
            }
        })
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
    }

    private fun filteredList(query: String?) : List<ExchangeRate>
    {
        val result = dataList.filter {
            it.code.contains(query!!, true) || it.currency.contains(query!!, true)
        }
        return result
    }
}


