package com.example.recetium.ui.screens.config
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recetium.data.Consumidor
import com.example.recetium.data.Creador
import com.example.recetium.ui.navigation.AppScreens
import com.example.recetium.ui.utils.BottomNavigationButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(navController: NavController, viewModel: ConfigViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Config Screen") },
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
                BottomNavigationButton(icon = Icons.Default.Person, label = "Profile", onClick = {  }, selected = true)
                BottomNavigationButton(icon = Icons.Default.Home, label = "Home", onClick = { navController.navigate(AppScreens.HomeScreen.route) })
                BottomNavigationButton(icon = Icons.Default.Search, label = "Search", onClick = { navController.navigate(AppScreens.SearchScreen.route) })
            }
        }
    ) { innerPadding ->
        BodyContent(Modifier.padding(innerPadding), viewModel, navController)
    }
}

@Composable
fun BodyContent(padding: Modifier, viewModel: ConfigViewModel, navController: NavController) {
    val creadores by viewModel.creadores.observeAsState(emptyList())
    val consumidores by viewModel.consumidores.observeAsState(emptyList())

    Column(modifier = padding.padding(16.dp)) {
        Text(text = "Creadores", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
        creadores.forEach { creador ->
            CreadorCard(creador = creador, navController = navController)
        }

        Text(text = "Consumidores", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 8.dp))
        consumidores.forEach { consumidor ->
            ConsumidorCard(consumidor = consumidor)
        }
    }
}

@Composable
fun CreadorCard(creador: Creador, navController: NavController) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(text = creador.nombre, style = MaterialTheme.typography.bodyLarge)

    }
}

@Composable
fun ConsumidorCard(consumidor: Consumidor) {
    Text(text = consumidor.nombre, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(bottom = 8.dp))
}

