package com.example.recetium.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recetium.data.Receta
import com.example.recetium.data.Repost
import com.example.recetium.ui.navigation.AppScreens
import com.example.recetium.ui.utils.BottomNavigationButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, viewModel: SearchViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Screen") },
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
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Gray
            ) {
                BottomNavigationButton(icon = Icons.Default.Person, label = "Profile", onClick = { navController.navigate(AppScreens.ConfigScreen.route) })
                BottomNavigationButton(icon = Icons.Default.Home, label = "Home", onClick = { navController.navigate(AppScreens.HomeScreen.route) })
                BottomNavigationButton(icon = Icons.Default.Search, label = "Search", onClick = {}, selected = true)
            }
        }
    ) { innerPadding ->
        BodyContent(Modifier.padding(innerPadding), viewModel, navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyContent(padding: Modifier, viewModel: SearchViewModel, navController: NavController) {
    val query = remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.observeAsState(emptyList())
    val selectedResult = remember { mutableStateOf<String?>(null) }

    Column(modifier = padding.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            OutlinedTextField(
                value = query.value,
                onValueChange = {
                    query.value = it
                    viewModel.search(it)
                },
                label = { Text("Buscar") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Text),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                }
            )
        }

        searchResults.forEach { result ->
            SearchResultCard(result, Modifier.padding(vertical = 4.dp)) {
                selectedResult.value = result
            }
        }

        selectedResult.value?.let { result ->
            DetailContent(result, viewModel, navController)
        }
    }
}

@Composable
fun SearchResultCard(result: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = result, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun DetailContent(result: String, viewModel: SearchViewModel, navController: NavController) {
    val recipes by viewModel.recipes.observeAsState(emptyList())
    val creadores by viewModel.creadores.observeAsState(emptyList())
    val consumidores by viewModel.consumidores.observeAsState(emptyList())
    val reposts by viewModel.reposts.observeAsState(emptyList())

    when {
        recipes.any { it.descripcion == result } -> {
            val receta = recipes.first { it.descripcion == result }
            navController.navigate("card_screen/${receta.idReceta}")
        }
        creadores.any { it.nombre == result } -> {
            val creador = creadores.first { it.nombre == result }
            Column {
                Text("Recetas de ${creador.nombre}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(8.dp))
                recipes.filter { it.creador?.idCreador == creador.idCreador }.forEach { receta ->
                    RecetaCard(receta, navController)
                }
            }
        }
        consumidores.any { it.nombre == result } -> {
            val consumidor = consumidores.first { it.nombre == result }
            // Cargar reposts del consumidor seleccionado
            viewModel.loadRepostsByConsumidor(consumidor.idConsumidor.toLong())

            Column {
                Text("Reposts de ${consumidor.nombre}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(8.dp))
                reposts.forEach { repost ->
                    RepostCard(repost, navController)
                }
            }
        }
    }
}

@Composable
fun RecetaCard(receta: Receta, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate("card_screen/${receta.idReceta}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(receta.descripcion, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun RepostCard(repost: Receta, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate("card_screen/${repost.idReceta}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(repost.descripcion, style = MaterialTheme.typography.bodyLarge)
        }
    }
}