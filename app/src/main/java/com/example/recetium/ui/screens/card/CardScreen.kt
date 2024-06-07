package com.example.recetium.ui.screens.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardScreen(navController: NavController){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("RECETA") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Atrás",
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        }
                    )
                }
            )
        }
    ) { innerPadding ->

        // Agregar relleno al contenido principal utilizando el contentPadding proporcionado por Scaffold
        BodyContent(Modifier.padding(innerPadding))
    }
}



@Composable
fun BodyContent(

    padding: Modifier,
    /*navController: NavController,
    recetas: List<Receta>,
    isChef: Boolean*/
) {

}