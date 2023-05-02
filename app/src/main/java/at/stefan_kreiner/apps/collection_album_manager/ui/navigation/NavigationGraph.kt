package at.stefan_kreiner.apps.collection_album_manager.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation


abstract class NavigationGraph(
    val startDestination : NavigationGraphNode,
) : NavigationGraphNode


fun NavGraphBuilder.navigation(
    graph: NavigationGraph,
    builder: NavGraphBuilder.() -> Unit,
) {
    navigation(
        route = graph.route.toString(),
        startDestination = graph.startDestination.route.toString(),
        builder = builder
    )
}