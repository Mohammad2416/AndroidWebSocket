package com.mohammadmirzakhani.android.challenge.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mohammadmirzakhani.android.challenge.DataModel.ChunksDataModel
import com.mohammadmirzakhani.android.challenge.R
import com.mohammadmirzakhani.android.challenge.viewHelper.RowsOfRecyclerViewHolder
/**
 * This is a normal RecyclerViewAdapter that could be user just for Chunks List
 * But I developed GenericAdapter to handel All type of List which can improve reusability
 * so I did not use this class
 * */
class ChunksRecyclerViewAdapter(var listModel : ChunksDataModel) : RecyclerView.Adapter<RowsOfRecyclerViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RowsOfRecyclerViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.row_recycler, p0,false)
        return RowsOfRecyclerViewHolder(view)
    }

    override fun getItemCount() = listModel.content!!.size

    override fun onBindViewHolder(vh: RowsOfRecyclerViewHolder, index: Int) {

       val dataModel  = listModel.content!![index]

       vh.txtTitle.text = dataModel.text
       vh.txtDate.text = dataModel.created
    }

}