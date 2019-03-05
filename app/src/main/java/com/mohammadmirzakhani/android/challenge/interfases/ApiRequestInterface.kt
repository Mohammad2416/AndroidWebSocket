package com.mohammadmirzakhani.android.challenge.interfases


import com.mohammadmirzakhani.android.challenge.DataModel.ChunksDataModel

import io.reactivex.Observable
import retrofit2.http.GET

interface ApiRequestInterface {
    @GET("chunks")
    fun getChunks() : Observable<ChunksDataModel>
}