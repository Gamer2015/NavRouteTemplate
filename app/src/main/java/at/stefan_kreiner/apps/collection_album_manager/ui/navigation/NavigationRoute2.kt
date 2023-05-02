package at.stefan_kreiner.apps.collection_album_manager.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.navOptions
import com.example.collection_album_manager.ui.navigation.NavigationRoute


interface NavigationParameters2<T1, T2> {
    fun component1(): T1
    fun component2(): T2
}


class NavigationRoute2<T1, T2, Parameters : NavigationParameters2<T1, T2>>(
    private val navArgument1: TypedNamedNavArgument<T1>,
    private val navArgument2: TypedNamedNavArgument<T2>,
    path: UniversalResourceIdentifierPath,
) : NavigationRoute(
    arguments = listOf(
        navArgument1,
        navArgument2,
    ).map {
        it.instance
    },
    path = path
) {
    fun toDestination(parameters: Parameters): String = super.toDestination(
        mapOf(
            navArgument1.instance to parameters.component1(),
            navArgument2.instance to parameters.component2(),
        )
    )
}

fun <T1, T2, Parameters : NavigationParameters2<T1, T2>> NavigationRoute2<T1, T2, Parameters>.navigateWith(
    navController: NavController,
    parameters: Parameters,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    this.navigateWith(
        navController = navController, parameters = parameters, navOptions = navOptions(builder)
    )
}

fun <T1, T2, Parameters : NavigationParameters2<T1, T2>> NavigationRoute2<T1, T2, Parameters>.navigateWith(
    navController: NavController,
    parameters: Parameters,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navController.navigate(
        route = this.toDestination(
            parameters = parameters
        ),
        navOptions = navOptions,
        navigatorExtras = navigatorExtras,
    )
}

//
//private class ExampleRouteParameters(
//    val itemId: Long,
//    val itemName: String,
//) : NavigationParameters2<Long, String?> {
//    override fun argument1() = itemId
//    override fun argument2() = itemName
//}
//
//private val itemIdArg = NavType.LongType.toNamedArgument(ExampleRouteParameters::itemId.name)
//private val itemNameArg =
//    NavType.StringType.toNamedArgument(ExampleRouteParameters::itemName.name)
//
//private val ExampleRoute = NavigationRoute2<Long, String?, ExampleRouteParameters>(
//    itemIdArg,
//    itemNameArg,
//    path = UniversalResourceIdentifierPath("test", itemIdArg.encoded, itemNameArg.encoded)
//)