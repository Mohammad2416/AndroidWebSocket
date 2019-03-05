package com.mohammadmirzakhani.android.challenge.DataModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ChunksDataModel {

    @SerializedName("content")
    @Expose
    var content: List<Content>? = null

}
