package com.mohammadmirzakhani.android.challenge.interfases

import com.neovisionaries.ws.client.WebSocket

interface WebSocketConnectedListener {
    fun getWebSocket(webSocket: WebSocket, messageStatus : String, messageResponse: String)
}