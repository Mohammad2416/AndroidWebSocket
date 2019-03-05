package com.mohammadmirzakhani.android.challenge.amazonCognito

import android.content.Context
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.regions.Regions
import com.mohammadmirzakhani.android.challenge.configs.ApiConfig
import com.mohammadmirzakhani.android.challenge.interfases.AmazonTokenHasGenerated
/**
 * Authentication:
 * use AWS Cognito to authenticate
 * */
class AmazonCognitoUserPool(var context: Context) {

    val conCognitoUserPool: CognitoUserPool
        get() = CognitoUserPool(context, ApiConfig.userPoolId, ApiConfig.clientId, null, Regions.EU_CENTRAL_1)



    companion object {
        fun loginToGetToken(password: String, amazonTokenListener : AmazonTokenHasGenerated): AuthenticationHandler {


            return object : AuthenticationHandler {
                override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {}
                override fun authenticationChallenge(continuation: ChallengeContinuation?) {}

                override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
                    val token = userSession?.accessToken

                    ApiConfig.token = token?.jwtToken.toString()
                    amazonTokenListener.isTokenGenerated(true)

                }

                override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation, userId: String) {
                    val authenticationDetails = AuthenticationDetails(userId, password, null)
                    authenticationContinuation.setAuthenticationDetails(authenticationDetails)
                    authenticationContinuation.continueTask()
                }

                override fun onFailure(exception: Exception) {
                    amazonTokenListener.isTokenGenerated(false)

                }
            }
        }
    }

}
