package at.stefan_kreiner.apps.collection_album_manager.ui.main

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import at.stefan_kreiner.apps.collection_album_manager.ui.album_list.AlbumListScreenNavigationGraph
import at.stefan_kreiner.apps.collection_album_manager.ui.album_list.navigation
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationDestination
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationGraph
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationRoute0
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.UniversalResourceIdentifierPath
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.navigation


object MainScreenNavigationGraph : NavigationGraph(AlbumListScreenNavigationGraph) {
    override val route = NavigationRoute0(
        path = UniversalResourceIdentifierPath("main")
    )
}

fun MainScreenNavigationGraph.navigation(
    builder: NavGraphBuilder,
    navController: NavController,
    deepLinks: Map<NavigationDestination, List<NavDeepLink>>,
    windowSizeClass: WindowSizeClass,
) {
    builder.navigation(this) {
        AlbumListScreenNavigationGraph.navigation(
            this,
            navController,
            deepLinks = deepLinks,
            windowSizeClass = windowSizeClass,
        )
    }
}