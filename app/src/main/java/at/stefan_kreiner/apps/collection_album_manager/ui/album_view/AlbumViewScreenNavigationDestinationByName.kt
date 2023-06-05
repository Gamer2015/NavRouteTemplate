package at.stefan_kreiner.apps.collection_album_manager.ui.album_view

import android.util.Log
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

object AlbumViewScreenNavigationDestinationByName : NavigationDestination() {
    data class Parameters(
        val itemName: String,
    ) : NavigationParameters1<String?>

    val itemNameArg = NavType.StringType.toNamedArgument(Parameters::itemName.name)
    override val route = NavigationRoute1<String?, Parameters>(
        itemNameArg,
        path = UniversalResourceIdentifierPath("albums", "viewByName", itemNameArg.encoded)
    )
}

fun AlbumViewScreenNavigationDestinationByName.composable(
    builder: NavGraphBuilder,
    navigateUp: () -> Unit,
    deepLinks: List<NavDeepLink>,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    Log.d("Navigation", "AlbumViewScreenNavigationDestinationByName Deep Links: $deepLinks")
    deepLinks.forEach {
        Log.d(
            "Navigation", "AlbumViewScreenNavigationDestinationByName Deep Link: ${it.uriPattern}"
        )
    }
    builder.composable(
        destination = this,
        deepLinks = deepLinks,
    ) { backStackEntry ->
        val parameters = AlbumViewScreenNavigationDestinationByName.Parameters(
            itemName = backStackEntry.arguments?.getString(itemNameArg.name)!!
        )
        Log.d(
            "Navigation",
            "AlbumViewScreenNavigationDestinationByName(itemName=${parameters.itemName})"
        )

        AlbumViewScreenByName(
            albumName = parameters.itemName,
            navigateUp = navigateUp,
            windowSizeClass = windowSizeClass,
            modifier = modifier,
        )
    }
}