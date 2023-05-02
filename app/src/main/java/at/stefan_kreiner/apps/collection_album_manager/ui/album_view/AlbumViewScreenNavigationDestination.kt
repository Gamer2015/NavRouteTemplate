package at.stefan_kreiner.apps.collection_album_manager.ui.album_view

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationDestination
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationParameters1
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationRoute1
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.UniversalResourceIdentifierPath
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.composable
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.encoded
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.toNamedArgument

data class AlbumViewScreenNavigationParameters(
    val itemId: Long,
) : NavigationParameters1<Long>

object AlbumViewScreenNavigationDestination : NavigationDestination() {
    val itemIdArg = NavType.LongType.toNamedArgument(AlbumViewScreenNavigationParameters::itemId.name)
    override val route = NavigationRoute1<Long, AlbumViewScreenNavigationParameters>(
        itemIdArg,
        path = UniversalResourceIdentifierPath("albums", "view", itemIdArg.encoded)
    )
}

fun AlbumViewScreenNavigationDestination.composable(
    builder: NavGraphBuilder,
    navigateUp: () -> Unit,
) {
    builder.composable(
        destination = this,
    ) { backStackEntry ->
        val parameters = AlbumViewScreenNavigationParameters(
            itemId = backStackEntry.arguments?.getLong(itemIdArg.name)!!
        )

        AlbumViewScreen(
            itemId = parameters.itemId,
            navigateUp = navigateUp,
        )
    }
}