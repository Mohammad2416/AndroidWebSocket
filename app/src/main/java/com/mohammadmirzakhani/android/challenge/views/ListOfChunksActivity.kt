package com.mohammadmirzakhani.android.challenge.views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.mohammadmirzakhani.android.challenge.DataModel.ChunksDataModel
import com.mohammadmirzakhani.android.challenge.DataModel.Content
import com.mohammadmirzakhani.android.challenge.R
import com.mohammadmirzakhani.android.challenge.adapters.GenericAdapter
import com.mohammadmirzakhani.android.challenge.network.OkHttpHelper
import com.mohammadmirzakhani.android.challenge.viewHelper.ViewHolderFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

import io.reactivex.schedulers.Schedulers


class ListOfChunksActivity : AppCompatActivity() {

    private var myCompositeDisposable: CompositeDisposable? = null
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_chunks)

        recyclerView = findViewById(R.id.activity_recycler_rv)

        myCompositeDisposable = CompositeDisposable()

        //Call Api To Get List Of Chunks
        loadData()
    }


     fun loadData() {

        //Add all RxJava disposables to a CompositeDisposable
        myCompositeDisposable?.add(OkHttpHelper.getOkHttpClient()!!.getChunks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse))
    }

     fun handleResponse(chunksDataModels: ChunksDataModel) {

        //::To Use Normal RecyclerViewAdapter
//        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
//        val adapter = ChunksRecyclerViewAdapter(chunksDataModels)
//        recyclerView.adapter = adapter

        //::To Use GenericRecyclerViewAdapter
        val myAdapter = object : GenericAdapter<Any>(chunksDataModels.content!!){
            override fun getLayoutId(position: Int, obj: Any): Int {
                return when(obj){
                    is Content -> R.layout.row_recycler
                    else -> R.layout.row_recycler //:: I just wanted to show how we can add View for Generic Adapter, row_recycler should be replaced with the View which developed in future
                }
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                return ViewHolderFactory.create(view,viewType)
            }

        }

        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter=myAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        myCompositeDisposable?.clear()
    }

}
