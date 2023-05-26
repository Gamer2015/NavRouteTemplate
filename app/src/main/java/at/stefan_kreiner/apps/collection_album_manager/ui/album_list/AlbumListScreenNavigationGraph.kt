package at.stefan_kreiner.apps.collection_album_manager.ui.album_list

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import at.stefan_kreiner.apps.collection_album_manager.ui.album_insert.AlbumInsertScreenNavigationDestination
import at.stefan_kreiner.apps.collection_album_manager.ui.album_insert.composable
import at.stefan_kreiner.apps.collection_album_manager.ui.album_view.AlbumViewScreenNavigationDestinationByIdentifier
import at.stefan_kreiner.apps.collection_album_manager.ui.album_view.AlbumViewScreenNavigationDestinationByName
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
) {
    Log.d("Navigation", "AlbumListScreenNavigationGraph called with backstack ${navController.backQueue}")
    navController.backQueue.forEach {
        Log.d("Navigation", "AlbumListScreenNavigationGraph BackStack: " + it.destination.route.toString())
    }
    builder.navigation(this) {
        AlbumListScreenNavigationDestination.composable(this, navigateToAlbumInsert = {
            AlbumInsertScreenNavigationDestination.route.navigateWith(
                navController = navController,
            )
        }, navigateToAlbumViewByIdentifier = { itemId ->
            Log.d("Navigation", "Item id: $itemId")
            AlbumViewScreenNavigationDestinationByIdentifier.route.navigateWith(
                navController = navController,
                AlbumViewScreenNavigationDestinationByIdentifier.Parameters(
                    itemId = itemId
                )
            )
        }, deepLinks = deepLinks[AlbumListScreenNavigationDestination] ?: listOf()
        )
        AlbumViewScreenNavigationDestinationByIdentifier.composable(
            this,
            navigateUp = navController::navigateUp,
            deepLinks = deepLinks[AlbumViewScreenNavigationDestinationByIdentifier] ?: listOf()
        )
        AlbumViewScreenNavigationDestinationByName.composable(
            this,
            navigateUp = navController::navigateUp,
            deepLinks = deepLinks[AlbumViewScreenNavigationDestinationByName] ?: listOf()
        )
//        AlbumViewScreenNavigationGraph.navigation(
//            this,
//            navController,
//            deepLinks = deepLinks,
//        )
        AlbumInsertScreenNavigationDestination.composable(
            this, navigateUp = navController::navigateUp, navigateToAlbumById = {
                AlbumViewScreenNavigationDestinationByIdentifier.route.navigateWith(
                    navController, AlbumViewScreenNavigationDestinationByIdentifier.Parameters(
                        it
                    )
                ) {
                    popUpTo(route = AlbumInsertScreenNavigationDestination.route.toString()) {
                        inclusive = true
                    }
                }
            }, deepLinks = deepLinks[AlbumListScreenNavigationDestination] ?: listOf()
        )
    }
}