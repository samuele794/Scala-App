package it.samuele794.scala.android.ui.main

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun HomePage(
    navigator: NavController,
) {
    Scaffold(
        bottomBar = {
            ScalaBottomNavigationBar(navigator)
        }
    ) {

    }
}