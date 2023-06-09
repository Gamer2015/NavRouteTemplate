package at.stefan_kreiner.apps.collection_album_manager.ui.album_list

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.Modifier
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationDestination
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationRoute0
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.UniversalResourceIdentifierPath
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.composable


object AlbumListScreenNavigationDestination : NavigationDestination() {
    override val route = NavigationRoute0(
        path = UniversalResourceIdentifierPath("albums")
    )
}



fun AlbumListScreenNavigationDestination.composable(
    builder: NavGraphBuilder,
    navigateToAlbumInsert: () -> Unit,
    navigateToAlbumViewByIdentifier: (Long) -> Unit,
    deepLinks: List<NavDeepLink>,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    builder.composable(
        destination = this,
        deepLinks = deepLinks,
    ) {
        AlbumListScreen(
            navigateToAlbumInsert = navigateToAlbumInsert,
            navigateToAlbumView = navigateToAlbumViewByIdentifier,
            windowSizeClass = windowSizeClass,
            modifier = modifier,
        )
    }
}