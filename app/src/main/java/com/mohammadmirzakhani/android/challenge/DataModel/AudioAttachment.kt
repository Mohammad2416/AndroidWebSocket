package com.mohammadmirzakhani.android.challenge.DataModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AudioAttachment {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("created")
    @Expose
    var created: String? = null
    @SerializedName("updated")
    @Expose
    var updated: String? = null

}
