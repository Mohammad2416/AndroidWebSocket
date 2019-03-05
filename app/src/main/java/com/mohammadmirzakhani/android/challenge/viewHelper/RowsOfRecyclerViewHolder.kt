package com.mohammadmirzakhani.android.challenge.viewHelper

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.row_recycler.view.*

class RowsOfRecyclerViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
    val txtTitle = itemView.row_recycler_txt_title!!
    val txtDate = itemView.row_recycler_txt_date!!
}