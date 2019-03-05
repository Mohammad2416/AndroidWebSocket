package com.mohammadmirzakhani.android.challenge.DataModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Content {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("text")
    @Expose
    var text: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("created")
    @Expose
    var created: String? = null
    @SerializedName("updated")
    @Expose
    var updated: String? = null
    @SerializedName("edited")
    @Expose
    var isEdited: Boolean = false
    @SerializedName("userId")
    @Expose
    var userId: String? = null
    @SerializedName("audioAttachments")
    @Expose
    var audioAttachments: List<AudioAttachment>? = null
    @SerializedName("labels")
    @Expose
    var labels: List<Label>? = null
    @SerializedName("offlineReference")
    @Expose
    var offlineReference: String? = null
    @SerializedName("clientCreated")
    @Expose
    var clientCreated: String? = null

}
