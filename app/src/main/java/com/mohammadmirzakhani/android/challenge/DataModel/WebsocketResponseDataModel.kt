package com.mohammadmirzakhani.android.challenge.DataModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//{"event":"startDetected","text":""}

class WebsocketResponseDataModel {

    @SerializedName("event")
    @Expose
    val event: String = ""

    @SerializedName("text")
    @Expose
    val text: String = ""

}