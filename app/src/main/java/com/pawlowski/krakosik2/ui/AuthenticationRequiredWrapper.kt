package com.pawlowski.krakosik2.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

@Composable
internal fun WrapAuthenticationRequired(content: @Composable () -> Unit) {
    val isLoading = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        if (FirebaseAuth.getInstance().currentUser != null) {
            isLoading.value = false
        } else {
            FirebaseAuth.getInstance().signInAnonymously().await()
            isLoading.value = false
        }
    }

    if (isLoading.value) {
        Box(modifier = Modifier.fillMaxSize().wrapContentSize()) {
            CircularProgressIndicator()
        }
    } else {
        content()
    }
}
