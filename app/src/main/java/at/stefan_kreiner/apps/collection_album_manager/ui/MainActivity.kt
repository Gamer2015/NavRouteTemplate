/*
 * Copyright (C) 2023 Stefan Kreiner
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.stefan_kreiner.apps.collection_album_manager.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import at.stefan_kreiner.apps.collection_album_manager.ui.ads.AdMobActivity
import at.stefan_kreiner.apps.collection_album_manager.ui.theme.MyApplicationTheme
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentRequestParameters
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AdMobActivity(
) {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        this.prepareAdMob(
            requestConfiguration = RequestConfiguration.Builder().setTestDeviceIds(
                listOf(
                    "6B18619BD708F9911BA92FC3300CF0E7", "A914562AE272BEFFCB903C909D9B4723"
                )
            ).build(),
            consentRequestParameters = ConsentRequestParameters.Builder().setConsentDebugSettings(
                ConsentDebugSettings.Builder(this)
                    .addTestDeviceHashedId("6B18619BD708F9911BA92FC3300CF0E7")
                    .addTestDeviceHashedId("A914562AE272BEFFCB903C909D9B4723")
                .build()
            ).setTagForUnderAgeOfConsent(false).build()
        )
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            App(
                windowSizeClass = windowSizeClass
            )
        }
    }
}

@Composable
fun App(
    windowSizeClass: WindowSizeClass,
) {
    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            MainNavigation(
                windowSizeClass = windowSizeClass
            )
        }
    }
}