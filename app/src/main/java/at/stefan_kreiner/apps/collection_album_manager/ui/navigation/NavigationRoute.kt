package com.example.collection_album_manager.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgumentBuilder
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navOptions
import androidx.navigation.navigation
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.UniversalResourceIdentifierPath
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.UniversalResourceIdentifierQuery


val NamedNavArgument.encoded: String
    get() = "{${name}}"

abstract class NavigationRoute(
    internal val arguments: List<NamedNavArgument>,
    internal val path: UniversalResourceIdentifierPath
) {
    protected fun Map<String, String>.toUniversalResourceIdentifierQuery(): UniversalResourceIdentifierQuery {
        return if (entries.isEmpty()) ""
        else "?" + entries.joinToString("&") {
            "${it.key}=${it.value}"
        }
    }

    protected fun Collection<NamedNavArgument>.toUniversalResourceIdentifierQuery(): UniversalResourceIdentifierQuery {
        return associate {
            Pair(it.name, it.encoded)
        }.toUniversalResourceIdentifierQuery()
    }


    protected val optionalArguments: List<NamedNavArgument>
        get() = arguments.filter {
            it.argument.isNullable
        }

    override fun toString(): String {
        return path.toString() + optionalArguments.toUniversalResourceIdentifierQuery()
    }

    fun toDestination(parameters: Map<NamedNavArgument, Any?>): String {
        val replacementParameters = parameters.entries.filter {
            it.value != null
        }.associate {
            Pair(it.key, it.value.toString())
        }
        val replacedPath = UniversalResourceIdentifierPath(*path.parts.map { part ->
            val relatedParameter = replacementParameters.entries.find { parameter ->
                part == parameter.key.encoded
            }
            relatedParameter?.value ?: part
        }.toTypedArray()).toString()
        val replacedQuery = optionalArguments.filter {
            replacementParameters.contains(it)
        }.associate {
            Pair(it.name, replacementParameters[it]!!)
        }.toUniversalResourceIdentifierQuery()
        return replacedPath + replacedQuery
    }
}

