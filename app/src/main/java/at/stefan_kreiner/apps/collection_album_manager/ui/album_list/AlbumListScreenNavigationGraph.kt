package at.stefan_kreiner.apps.collection_album_manager.ui.album_list

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import at.stefan_kreiner.apps.collection_album_manager.ui.album_insert.AlbumInsertScreenNavigationDestination
import at.stefan_kreiner.apps.collection_album_manager.ui.album_insert.composable
import at.stefan_kreiner.apps.collection_album_manager.ui.album_view.AlbumViewScreenNavigationDestination
import at.stefan_kreiner.apps.collection_album_manager.ui.album_view.AlbumViewScreenNavigationParameters
import at.stefan_kreiner.apps.collection_album_manager.ui.album_view.composable
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationGraph
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationRoute0
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.UniversalResourceIdentifierPath
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.navigation
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.navigateWith


object AlbumListScreenNavigationGraph : NavigationGraph(AlbumListScreenNavigationDestination) {
    override val route = NavigationRoute0(
        path = UniversalResourceIdentifierPath("albumsGraph")
    )
}


fun AlbumListScreenNavigationGraph.navigation(
    builder: NavGraphBuilder,
    navController: NavController,
) {
    builder.navigation(this) {
        AlbumListScreenNavigationDestination.composable(
            this,
            navigateToAlbumInsert = {
                AlbumInsertScreenNavigationDestination.route.navigateWith(
                    navController = navController,
                )
            },
            navigateToAlbumView = { itemId ->
                Log.d("Navigation", "Item id: $itemId")
                AlbumViewScreenNavigationDestination.route.navigateWith(
                    navController = navController,
                    AlbumViewScreenNavigationParameters(
                        itemId = itemId
                    )
                )
            },
        )
        AlbumViewScreenNavigationDestination.composable(
            this,
            navigateUp = navController::navigateUp
        )
        AlbumInsertScreenNavigationDestination.composable(
            this,
            navigateUp = navController::navigateUp
        )
    }
}