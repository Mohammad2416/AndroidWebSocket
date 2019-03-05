package com.mohammadmirzakhani.android.challenge.views

import android.content.Context
import android.util.Log
import com.mohammadmirzakhani.android.challenge.amazonCognito.AmazonCognitoUserPool
import com.mohammadmirzakhani.android.challenge.configs.ApiConfig
import com.mohammadmirzakhani.android.challenge.configs.ApiConfig.password
import com.mohammadmirzakhani.android.challenge.interfases.AmazonTokenHasGenerated
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.mock
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Log::class)
class MainActivityTest {


    @Before
    fun beforeTest() {
        PowerMockito.mockStatic(Log::class.java)
        Mockito.`when`(Log.i(any(), any())).then {
            println(it.arguments[1] as String)
            1
        }
    }


    @Test
    fun loginToGetToken() {

        AmazonCognitoUserPool(mock(Context::class.java)).conCognitoUserPool
            .getUser(ApiConfig.userName)
            .getSessionInBackground(AmazonCognitoUserPool.loginToGetToken(password, object : AmazonTokenHasGenerated {

                override fun isTokenGenerated(isTokenGenerated: Boolean) {
                    if (isTokenGenerated) {

                        Assert.assertEquals(isTokenGenerated, true)

                    }
                }
            }))
    }
}