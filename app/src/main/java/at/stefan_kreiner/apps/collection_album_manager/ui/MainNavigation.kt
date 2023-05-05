/*
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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDeepLink
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import at.stefan_kreiner.apps.collection_album_manager.ui.main.MainScreenNavigationGraph
import at.stefan_kreiner.apps.collection_album_manager.ui.main.navigation
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationDestination
import com.example.collection_album_manager.ui.navigation.NavigationRoute

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    startDestination: NavigationRoute = MainScreenNavigationGraph.route,
    deepLinks: Map<NavigationDestination, List<NavDeepLink>> = mapOf(),
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination.toString()) {
        MainScreenNavigationGraph.navigation(
            this,
            navController = navController,
        )
    }
}

