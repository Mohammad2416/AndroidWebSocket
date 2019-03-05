package com.mohammadmirzakhani.android.challenge.viewHelper


import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.mohammadmirzakhani.android.challenge.DataModel.Content
import com.mohammadmirzakhani.android.challenge.R
import com.mohammadmirzakhani.android.challenge.adapters.GenericAdapter

object ViewHolderFactory {

    fun create(view: View, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.row_recycler -> ChunksViewHolder(view)
            //::Here you Can add any other RecyclerRowView which you will need during develop the app
            else -> {//::I wrote this to show you how it works if We have more than one recyclerRowView
                 ChunksViewHolder(view)
            }
        }
    }

    class ChunksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), GenericAdapter.Binder<Content> {

        private var tvTitle: TextView = itemView.findViewById(R.id.row_recycler_txt_title)
        private var tvDate: TextView = itemView.findViewById(R.id.row_recycler_txt_date)
        override fun bind(data: Content) {
            tvTitle.text = data.text
            tvDate.text = data.created
        }
    }

}