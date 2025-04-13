package com.pawlowski.krakosik2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.ui.Modifier
import com.pawlowski.krakosik2.ui.WrapAuthenticationRequired
import com.pawlowski.krakosik2.ui.screen.map.MapScreen
import com.pawlowski.krakosik2.ui.theme.Krakosik2Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Krakosik2Theme {
                Column(modifier = Modifier.safeContentPadding()) {
                    WrapAuthenticationRequired {
                        MapScreen()
                    }
                }
            }
        }
    }
}
