package com.capstone.batiklens.data

import android.util.Log
import androidx.lifecycle.liveData
import com.capstone.batiklens.data.Result
import com.capstone.batiklens.utils.UserPreferences
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepository private constructor(
    private val userPreferences: UserPreferences
){
    private val fAuth : FirebaseAuth = FirebaseAuth.getInstance()
    fun registerEmailAndPassword(name: String, email:String, password: String)= liveData(Dispatchers.IO){
        emit(Result.Loading)

        try {

            val auth = suspendCoroutine<Result<String>> { continuation ->
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Executors.newSingleThreadExecutor()){
                    if(it.isSuccessful){
                        Log.d("register", "success")
                        continuation.resume( Result.Success("Register Successful"))
                    }else{
                        Log.d("register", "${it.exception?.message}")
                        continuation.resume( Result.Error("${it.exception?.message}"))
                    }
                }
            }
            emit(auth)

        }catch (e: Exception){
            Log.d("register", "$e")
            emit(Result.Error("$e"))
        }
    }

    fun signEmailAndPassword(email: String, password: String) = liveData(Dispatchers.IO){
        emit(Result.Loading)

        try {
            val signin = suspendCoroutine<Result<String>> { continuation ->
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if(it.isSuccessful){
                        continuation.resume(Result.Success("Login Success"))
                    }else{
                        continuation.resume(Result.Error(it.exception?.message))
                    }
                }
            }

            emit(signin)
        }catch (e : Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    fun loginUsingGoogle(idToken :String) = liveData(Dispatchers.IO){
        emit(Result.Loading)
        val credential : AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        try {
            val googleAuth = suspendCoroutine {continuation ->
                fAuth.signInWithCredential(credential).addOnCompleteListener(Executors.newSingleThreadExecutor()) {
                    if (it.isSuccessful) {
                        Log.d("authRepo", "signin success")
                        continuation.resume( Result.Success("Sign In Success"))
                    } else {
                        Log.d("authRepo", "signin failed")
                        continuation.resume(  Result.Error(it.exception?.message))
                    }
                }
            }
            emit(googleAuth)

        }catch (e : Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return fAuth.currentUser
    }

    fun signOut(){
        fAuth.signOut()
    }


    companion object{
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            userPreferences: UserPreferences
        ): AuthRepository =
            instance ?: synchronized(this){
                instance ?:AuthRepository(userPreferences)
            }.also { instance = it }
    }
}