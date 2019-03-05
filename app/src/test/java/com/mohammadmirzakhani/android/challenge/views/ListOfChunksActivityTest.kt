package com.mohammadmirzakhani.android.challenge.views

import com.mohammadmirzakhani.android.challenge.DataModel.ChunksDataModel
import com.mohammadmirzakhani.android.challenge.network.OkHttpHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.junit.Test

import org.junit.Assert.*

class ListOfChunksActivityTest {

    private var myCompositeDisposable: CompositeDisposable? = null


    @Test
    fun loadData() {

        myCompositeDisposable?.add(OkHttpHelper.getOkHttpClient()!!.getChunks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse))
    }


    fun handleResponse(chunksDataModels: ChunksDataModel) {
        assertNotNull(chunksDataModels)
    }
}