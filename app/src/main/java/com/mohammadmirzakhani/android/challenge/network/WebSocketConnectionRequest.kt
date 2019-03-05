package com.mohammadmirzakhani.android.challenge.network

import android.util.Log
import com.google.gson.Gson
import com.mohammadmirzakhani.android.challenge.DataModel.WebsocketResponseDataModel
import com.mohammadmirzakhani.android.challenge.configs.ApiConfig
import com.mohammadmirzakhani.android.challenge.interfases.WebSocketConnectedListener
import com.neovisionaries.ws.client.*
import java.util.*


class WebSocketConnectionRequest {


    companion object {
        private var webSocketFactory = WebSocketFactory()
        private var webSocket = webSocketFactory.createSocket(ApiConfig.wsUrl, 5000)

         fun webSocketConnect(webSocketListener: WebSocketConnectedListener) {
            webSocket.addHeader("Authorization", "Bearer " + ApiConfig.token)
            webSocket.addHeader("Offline-Reference", UUID.randomUUID().toString())
            webSocket.addHeader("Transcribe-Only", "true")

            // Register a listener to receive WebSocket events.
            webSocket.addListener(object : WebSocketAdapter() {

                override fun onTextMessage(websocket: WebSocket?, text: String?) {
                    super.onTextMessage(websocket, text)
                    Log.v("WSS", "onTextMessage: $text")
                    val gson = Gson()
                    val dataModel: WebsocketResponseDataModel = gson.fromJson(text, WebsocketResponseDataModel::class.java)

                    webSocketListener.getWebSocket(webSocket,"onTextMessage",dataModel.text)

                }

                override fun onFrameError(websocket: WebSocket?, cause: WebSocketException?, frame: WebSocketFrame?) {
                    super.onFrameError(websocket, cause, frame)
                    Log.v("WSS", " a frame failed to be read.:: ${frame.toString()}")
                }

                override fun onFrameSent(websocket: WebSocket?, frame: WebSocketFrame?) {
                    super.onFrameSent(websocket, frame)
                    Log.v("WSS", " a frame was sent.:: ${frame.toString()} ")
                    webSocketListener.getWebSocket(webSocket," a frame was sent.","")

                }

                override fun onFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
                    super.onFrame(websocket, frame)
                    Log.v("WSS", " a frame was received.: ${frame.toString()}")
                    webSocketListener.getWebSocket(webSocket,"a close frame was received.","")

                }

                override fun onCloseFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
                    super.onCloseFrame(websocket, frame)
                    Log.v("WSS", " a close frame was received.")
                    webSocketListener.getWebSocket(webSocket,"a close frame was received.","")

                }

                override fun onConnected(websocket: WebSocket?, headers: MutableMap<String, MutableList<String>>?) {
                    super.onConnected(websocket, headers)
                    Log.v("WSS", "onConnected: opening handshake succeeded")

                    webSocketListener.getWebSocket(webSocket,"onConnected","")


                }

                //Called when connectAsynchronously() failed.
                override fun onConnectError(websocket: WebSocket?, exception: WebSocketException?) {
                    super.onConnectError(websocket, exception)
                    Log.v("WSS", "onConnectError")
                    webSocketListener.getWebSocket(webSocket,"onConnectError","")

                }

            })


            try {
                // Connect to the server and perform an opening handshake.
                // This method blocks until the opening handshake is finished.
                webSocket.connect()

            } catch (e: OpeningHandshakeException) {
                // Status line.
                val sl = e.statusLine
                println("=== Status Line ===")
                System.out.format("HTTP Version  = %s\n", sl.httpVersion)
                System.out.format("Status Code   = %d\n", sl.statusCode)
                System.out.format("Reason Phrase = %s\n", sl.reasonPhrase)

            } catch (e: HostnameUnverifiedException) {
                // The certificate of the peer does not match the expected hostname.
                e.printStackTrace()
            } catch (e: WebSocketException) {
                // Failed to establish a WebSocket connection.
                e.printStackTrace()
            }

        }

    }
}