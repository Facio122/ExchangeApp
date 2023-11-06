package com.example.exchangeapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private var list: List<ExchangeRate>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>()
{
    var onItemClick : ((ExchangeRate) -> Unit)? = null
    var flag = true

    fun setFilterItemList(filteredList: List<ExchangeRate>)
    {
        this.list = filteredList
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val codeTextView: TextView = itemView.findViewById(R.id.codeRecyclerView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameRecyclerView)
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerviewitem, parent, false)
        flag = if(flag) {
            false
        } else {
            view.setBackgroundColor(androidx.appcompat.R.color.primary_material_light)
            true
        }
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.codeTextView.text = item.code
        holder.nameTextView.text = item.currency

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(item)
        }
    }
}