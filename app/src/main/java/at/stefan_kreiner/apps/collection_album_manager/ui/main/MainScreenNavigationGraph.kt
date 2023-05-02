package at.stefan_kreiner.apps.collection_album_manager.ui.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import at.stefan_kreiner.apps.collection_album_manager.ui.album_list.AlbumListScreenNavigationGraph
import at.stefan_kreiner.apps.collection_album_manager.ui.album_list.navigation
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationGraph
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationGraphNode
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationRoute0
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.UniversalResourceIdentifierPath
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.navigation
import com.example.collection_album_manager.ui.navigation.NavigationRoute


object MainScreenNavigationGraph : NavigationGraph(AlbumListScreenNavigationGraph) {
    override val route = NavigationRoute0(
        path = UniversalResourceIdentifierPath("main")
    )
}

fun MainScreenNavigationGraph.navigation(
    builder : NavGraphBuilder,
    navController: NavController,
) {
    builder.navigation(this) {
        AlbumListScreenNavigationGraph.navigation(
            this,
            navController,
        )
    }
}