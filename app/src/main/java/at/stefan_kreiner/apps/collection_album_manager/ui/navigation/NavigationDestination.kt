package at.stefan_kreiner.apps.collection_album_manager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


abstract class NavigationDestination : NavigationGraphNode


fun NavGraphBuilder.composable(
    destination: NavigationDestination,
    deepLinks: List<NavDeepLink>,
    content: @Composable (NavBackStackEntry) -> Unit,
) {
    composable(
        route = destination.route.toString(),
        arguments = destination.route.arguments,
        deepLinks = deepLinks,
        content = content,
    )
}