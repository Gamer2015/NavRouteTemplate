package at.stefan_kreiner.apps.collection_album_manager.ui.album_list

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import at.stefan_kreiner.apps.collection_album_manager.ui.album_insert.AlbumInsertScreenNavigationDestination
import at.stefan_kreiner.apps.collection_album_manager.ui.album_insert.composable
import at.stefan_kreiner.apps.collection_album_manager.ui.album_view.AlbumViewScreenNavigationDestination
import at.stefan_kreiner.apps.collection_album_manager.ui.album_view.composable
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationDestination
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationGraph
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationRoute0
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.UniversalResourceIdentifierPath
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.navigateWith
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.navigation


object AlbumListScreenNavigationGraph : NavigationGraph(AlbumListScreenNavigationDestination) {
    override val route = NavigationRoute0(
        path = UniversalResourceIdentifierPath("albumsGraph")
    )
}


fun AlbumListScreenNavigationGraph.navigation(
    builder: NavGraphBuilder,
    navController: NavController,
    deepLinks: Map<NavigationDestination, List<NavDeepLink>>,
    windowSizeClass: WindowSizeClass,
) {
    Log.d(
        "Navigation",
        "AlbumListScreenNavigationGraph called with backstack ${navController.backQueue}"
    )
    navController.backQueue.forEach {
        Log.d(
            "Navigation",
            "AlbumListScreenNavigationGraph BackStack: " + it.destination.route.toString()
        )
    }
    builder.navigation(this) {
        AlbumListScreenNavigationDestination.composable(
            this,
            navigateToAlbumInsert = {
                AlbumInsertScreenNavigationDestination.route.navigateWith(
                    navController = navController,
                )
            },
            navigateToAlbumViewByIdentifier = { itemId ->
                Log.d("Navigation", "Item id: $itemId")
                AlbumViewScreenNavigationDestination.route.navigateWith(
                    navController = navController,
                    AlbumViewScreenNavigationDestination.Parameters(
                        itemId = itemId
                    )
                )
            },
            deepLinks = deepLinks[AlbumListScreenNavigationDestination] ?: listOf(),
            windowSizeClass = windowSizeClass,
        )
        AlbumViewScreenNavigationDestination.composable(
            this,
            navigateUp = navController::navigateUp,
            deepLinks = deepLinks[AlbumViewScreenNavigationDestination] ?: listOf(),
            windowSizeClass = windowSizeClass,
        )
//        AlbumViewScreenNavigationGraph.navigation(
//            this,
//            navController,
//            deepLinks = deepLinks,
//        )
        AlbumInsertScreenNavigationDestination.composable(
            this, navigateUp = navController::navigateUp,
            navigateToAlbumById = {
                AlbumViewScreenNavigationDestination.route.navigateWith(
                    navController, AlbumViewScreenNavigationDestination.Parameters(
                        it
                    )
                ) {
                    popUpTo(route = AlbumInsertScreenNavigationDestination.route.toString()) {
                        inclusive = true
                    }
                }
            },
            deepLinks = deepLinks[AlbumListScreenNavigationDestination] ?: listOf(),
            windowSizeClass = windowSizeClass,
        )
    }
}