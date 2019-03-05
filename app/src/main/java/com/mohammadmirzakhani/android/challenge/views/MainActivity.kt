package com.mohammadmirzakhani.android.challenge.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mohammadmirzakhani.android.challenge.amazonCognito.AmazonCognitoUserPool
import com.mohammadmirzakhani.android.challenge.configs.ApiConfig
import kotlinx.android.synthetic.main.activity_main.*
import android.media.AudioRecord
import android.media.MediaRecorder
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import com.mohammadmirzakhani.android.challenge.R
import com.mohammadmirzakhani.android.challenge.interfases.AmazonTokenHasGenerated
import com.mohammadmirzakhani.android.challenge.interfases.WebSocketConnectedListener
import com.mohammadmirzakhani.android.challenge.network.WebSocketConnectionRequest
import com.neovisionaries.ws.client.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicBoolean


class MainActivity : AppCompatActivity() {

    private val sampleRate = 16000
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private var minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

    /**
     * Size of the buffer where the audio data is stored by Android
     */
    private val buffer: ByteBuffer = ByteBuffer.allocateDirect(minBufSize * 40)

    private val permissionsRequestRecordAudio = 1100

    /**
     * Signals whether a recording is in progress (true) or not (false).
     */
    private val recordingInProgress = AtomicBoolean(false)

    private var webSocketFactory = WebSocketFactory()
    private var webSocket = webSocketFactory.createSocket(ApiConfig.wsUrl, 5000)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get Token From AWS-Cognito
        loginToGetToken(ApiConfig.userName, ApiConfig.password)

        main_activity_btn_start_stop_recording.setOnClickListener {
            checkRecordPermission()
        }
        
        main_activity_button_chunks.setOnClickListener {
            startActivity(Intent(this, ListOfChunksActivity::class.java))
        }

    }

/**
 * Get Token from Amazon WS
 * it's not private because I wrote a TEST for this func, {@link MainActivityTest}
 * */
     fun loginToGetToken(userId: String, password: String) {
        AmazonCognitoUserPool(this).conCognitoUserPool
                .getUser(userId)
                .getSessionInBackground(AmazonCognitoUserPool.loginToGetToken(password, object : AmazonTokenHasGenerated {

                    override fun isTokenGenerated(isTokenGenerated: Boolean) {
                        if (isTokenGenerated) {
                            main_activity_txt_status.text = getString(R.string.tokenIsGenerated)
                            wSocketConnectionToServer()
                        }else{
                            main_activity_txt_status.text = getString(R.string.InternetConnectionHasProblem)
                        }
                    }
                }))
    }

/**
 * To create a WebSocket and Connect to Server
 * Live transcription of spoken text via WebSocket
 * */
    @SuppressLint("CheckResult")
    private fun wSocketConnectionToServer() {

        Observable.just("")
            .subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.newThread())
            .subscribe {

                WebSocketConnectionRequest.webSocketConnect(object : WebSocketConnectedListener {
                    override fun getWebSocket(webSocket: WebSocket, messageStatus: String, messageResponse: String) {
                        this@MainActivity.webSocket = webSocket
                        setTextOnUiThread(messageStatus)

                        if (messageStatus.contentEquals("onTextMessage")) {
                            runOnUiThread {
                                main_activity_txt_websocket_what_i_say.text = messageResponse
                            }
                        }
                    }
                })
            }
    }

/**
 * To know Status of socket which received from wSocketConnectionToServer()
 * */
    fun setTextOnUiThread(txt: String) {
        runOnUiThread {

            main_activity_txt_websocket_status.text = txt

            if(txt.contentEquals("onConnected")){
                main_activity_btn_start_stop_recording.visibility = View.VISIBLE
                main_activity_txt_websocket_status.visibility = View.VISIBLE
            }
        }
    }

    private fun startRecordAudioStreaming() {
        val audioRecord = findAudioRecord()
        if (main_activity_btn_start_stop_recording.text == getString(R.string.start_rec)) {
            main_activity_btn_start_stop_recording.text = getString(R.string.close_socket)

            audioRecord!!.startRecording()
            recordingInProgress.set(true)

            //reading data from MIC into buffer
            Thread(Runnable { sendAudioDataToServer(audioRecord) }, "AudioRecorder Thread").start()

        } else {
            main_activity_btn_start_stop_recording.text = getString(R.string.start_rec)
            audioRecord!!.stop()
            recordingInProgress.set(false)
            webSocket.disconnect()
            runOnUiThread { main_activity_btn_start_stop_recording.visibility = View.INVISIBLE }
        }

    }

    private fun sendAudioDataToServer(audioRecord: AudioRecord) {
        var result: Int
        while (recordingInProgress.get()) {
            result = audioRecord.read(buffer, minBufSize * 40)
            if (result < 0) {
                throw  RuntimeException("Reading of audio buffer failed: " + getBufferReadFailureReason(result))
            } else {
                webSocket.sendBinary(buffer.array())

            }
        }
    }

    private fun getBufferReadFailureReason(errorCode: Int): String {
        return when (errorCode) {
            AudioRecord.ERROR_INVALID_OPERATION -> "ERROR_INVALID_OPERATION"
            AudioRecord.ERROR_BAD_VALUE -> "ERROR_BAD_VALUE"
            AudioRecord.ERROR_DEAD_OBJECT -> "ERROR_DEAD_OBJECT"
            AudioRecord.ERROR -> "ERROR"
            else -> "Unknown ($errorCode)"
        }
    }

    private fun findAudioRecord(): AudioRecord? {
        val recorder = AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, minBufSize * 10)
        return if (recorder.state == AudioRecord.STATE_INITIALIZED) {
            recorder
        } else {
            null
        }
    }

    private fun checkRecordPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), permissionsRequestRecordAudio)
            }
        } else {
            // Permission has already been granted
            startRecordAudioStreaming()
        }

    }

    /**
     * If User get us Permission we will call startRecordAudioStreaming() to use Mic
     * */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            permissionsRequestRecordAudio -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the

                    // step 2: Record Audio
                    startRecordAudioStreaming()

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }

    }//


}
