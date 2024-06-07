package com.example.recetium.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.recetium.data.Receta
import com.example.recetium.ui.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    val recipes by viewModel.recipes.observeAsState(emptyList())
    val isChef by viewModel.isChef.observeAsState(true)

    var recipesLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!recipesLoaded) {
            viewModel.loadRecipes()
            recipesLoaded = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home Screen") },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar sesión",
                        modifier = Modifier.clickable {
                            navController.navigate(AppScreens.LoginScreen.route) {
                                popUpTo(AppScreens.LoginScreen.route) { inclusive = true }
                            }
                        }
                    )
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Gray
            ) {
                BottomNavigationButton(icon = Icons.Default.Person, label = "Profile", onClick = { navController.navigate(route = AppScreens.ConfigScreen.route) })
                BottomNavigationButton(icon = Icons.Default.Home, label = "Home", onClick = { /* TODO */ }, selected = true)
                BottomNavigationButton(icon = Icons.Default.Search, label = "Search", onClick = { navController.navigate(route = AppScreens.SearchScreen.route) })
            }
        },
        floatingActionButton = {
            if (isChef) {
                FloatingActionButton(
                    onClick = { navController.navigate(AppScreens.PostRecetasScreen.route) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir Receta")
                }
            }
        },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.End,
        content = { innerPadding ->
            BodyContent(navController, Modifier.padding(innerPadding), recipes)
        }
    )
}

@Composable
fun BottomNavigationButton(icon: ImageVector, label: String, onClick: () -> Unit, selected: Boolean = false) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = if (selected) Color.Blue else Color.Black )
        Text(text = label, color = if (selected) Color.Blue else Color.Black)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItem(recipe: Receta, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = { navController.navigate(AppScreens.CardScreen.route) }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Descripción como título
            Text(
                text = recipe.descripcion,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Imagen
            Image(
                painter = rememberAsyncImagePainter(recipe.strImage),
                contentDescription = null,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            // Botón pequeño
            Button(
                onClick = { /* No action for now */ },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.End),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = "Repost")
            }
        }
    }
}

@Composable
fun BodyContent(
    navController: NavController,
    padding: Modifier,
    recetas: List<Receta>,
) {
    LazyColumn(modifier = padding) {
        items(recetas) { receta ->
            CardItem(receta, navController)
        }
    }
}