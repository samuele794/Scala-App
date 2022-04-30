package it.samuele794.scala.android.ui.main

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import it.samuele794.scala.resources.SharedRes

sealed class MainNavigation(
    @StringRes val title: Int,
    val icon: ImageVector,
    val iconFilled: ImageVector,
    //TODO CHANGE TO DESTINATION OBJ
    val routeName: String,
//    val routeName: DirectionDestination
) {
    object Home : MainNavigation(
        title = SharedRes.strings.home.resourceId,
        icon = Icons.Outlined.Home,
        iconFilled = Icons.Filled.Home,
        routeName = "home"
    )

    object Profile : MainNavigation(
        title = SharedRes.strings.profile.resourceId,
        icon = Icons.Outlined.Person,
        iconFilled = Icons.Filled.Person,
        routeName = "profile"
    )

    object Search : MainNavigation(
        title = SharedRes.strings.search.resourceId,
        icon = Icons.Outlined.Search,
        iconFilled = Icons.Filled.Search,
        routeName = "search"
    )
}

@Composable
fun ScalaBottomNavigationBar(
    navController: NavController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navItems = buildList {
        add(MainNavigation.Home)
        add(MainNavigation.Search)
        add(MainNavigation.Profile)
    }

    BottomNavigation(modifier = Modifier.fillMaxWidth()) {
        navItems.forEach { item ->
            val selected = currentRoute == item.routeName
            BottomNavigationItem(
                selected = selected,
                onClick = {
                    //TODO ADD NAVIGATION
                    //navController.navigate()
                },
                icon = {
                    Icon(
                        if (selected) {
                            item.iconFilled
                        } else {
                            item.icon
                        }, contentDescription = ""
                    )
                },
                label = { Text(text = stringResource(item.title)) },
                alwaysShowLabel = true
            )
        }
    }
}