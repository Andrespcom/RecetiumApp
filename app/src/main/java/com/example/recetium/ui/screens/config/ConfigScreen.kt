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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recetium.data.Consumidor
import com.example.recetium.data.Creador
import com.example.recetium.ui.navigation.AppScreens
import com.example.recetium.ui.screens.home.HomeViewModel
import com.example.recetium.ui.utils.BottomNavigationButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(navController: NavController, homeViewModel: HomeViewModel) {
    val viewModel: ConfigViewModel = remember { ConfigViewModel(homeViewModel) }

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
        BodyContent(Modifier.padding(innerPadding), viewModel)
    }
}

@Composable
fun BodyContent(modifier: Modifier, viewModel: ConfigViewModel) {
    val user by viewModel.user.observeAsState()
    val isCreador = user is Creador

    user?.let {
        Column(modifier = modifier.padding(16.dp)) {
            if (isCreador) {
                CreadorDetails(creador = it as Creador, viewModel = viewModel)
            } else {
                ConsumidorDetails(consumidor = it as Consumidor, viewModel = viewModel)
            }
        }
    } ?: run {
        Text(text = "No user data available", modifier = Modifier.padding(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreadorDetails(creador: Creador, viewModel: ConfigViewModel) {
    Column {
        TextField(
            value = creador.nombre,
            onValueChange = { viewModel.updateCreadorNombre(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
        Button(
            onClick = { viewModel.saveCreador() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsumidorDetails(consumidor: Consumidor, viewModel: ConfigViewModel) {
    Column {
        TextField(
            value = consumidor.nombre,
            onValueChange = { viewModel.updateConsumidorNombre(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
        Button(
            onClick = { viewModel.saveConsumidor() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}