package at.stefan_kreiner.apps.collection_album_manager.ui.album_view

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.Modifier
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationDestination
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationParameters1
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationRoute1
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.UniversalResourceIdentifierPath
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.composable
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.encoded
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.toNamedArgument

object AlbumViewScreenNavigationDestination : NavigationDestination() {
    data class Parameters(
        val itemId: Long,
    ) : NavigationParameters1<Long>

    val itemIdArg = NavType.LongType.toNamedArgument(Parameters::itemId.name)
    override val route = NavigationRoute1<Long, Parameters>(
        itemIdArg,
        path = UniversalResourceIdentifierPath("albums", "viewByIdentifier", itemIdArg.encoded)
    )
}

fun AlbumViewScreenNavigationDestination.composable(
    builder: NavGraphBuilder,
    navigateUp: () -> Unit,
    deepLinks: List<NavDeepLink>,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    builder.composable(
        destination = this,
        deepLinks = deepLinks,
    ) {
        AlbumViewScreen(
            navigateUp = navigateUp,
            windowSizeClass = windowSizeClass,
            modifier = modifier,
        )
    }
}