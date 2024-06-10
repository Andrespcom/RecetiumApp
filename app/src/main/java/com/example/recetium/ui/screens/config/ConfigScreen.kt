package com.example.recetium.ui.screens.config
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recetium.data.Consumidor
import com.example.recetium.data.Creador
import com.example.recetium.data.Receta
import com.example.recetium.ui.navigation.AppScreens
import com.example.recetium.ui.screens.home.HomeViewModel
import com.example.recetium.ui.utils.BottomNavigationButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(navController: NavController, homeViewModel: HomeViewModel) {
    val viewModel: ConfigViewModel = viewModel { ConfigViewModel(homeViewModel) }
    val user by viewModel.user.observeAsState()
    val isBanned by viewModel.isBanned.observeAsState(false)

    Log.d("ConfigScreen", "ConfigScreen loaded with user: $user, isBanned: $isBanned")

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
        content = { innerPadding ->
            if (user == null || isBanned) {
                BannedUserContent(Modifier.padding(innerPadding))
            } else {
                BodyContent(Modifier.padding(innerPadding), viewModel, navController)
            }
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Gray
            ) {
                BottomNavigationButton(icon = Icons.Default.Person, label = "Profile", onClick = {  }, selected = true)
                BottomNavigationButton(icon = Icons.Default.Home, label = "Home", onClick = { navController.navigate(route = AppScreens.HomeScreen.route) })
                BottomNavigationButton(icon = Icons.Default.Search, label = "Search", onClick = { navController.navigate(route = AppScreens.SearchScreen.route) })
            }
        }
    )
}

@Composable
fun BannedUserContent(modifier: Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Has sido baneado o el usuario no está disponible",
            color = Color.Red,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun BodyContent(modifier: Modifier, viewModel: ConfigViewModel, navController: NavController) {
    val user by viewModel.user.observeAsState()
    val recetas by viewModel.recetas.observeAsState(emptyList())
    val isCreador = user is Creador

    user?.let {
        Column(modifier = modifier.padding(16.dp)) {
            if (isCreador) {
                CreadorDetails(creador = it as Creador, viewModel = viewModel)
                Text("Recetas", style = MaterialTheme.typography.bodyMedium)
                recetas.forEach { receta ->
                    RecetaCard(receta = receta, navController = navController, viewModel = viewModel)
                }
            } else {
                ConsumidorDetails(consumidor = it as Consumidor, viewModel = viewModel)
                Text("Reposts", style = MaterialTheme.typography.bodyMedium)
                recetas.forEach { repost ->
                    RepostCard(repost = repost, navController = navController, viewModel = viewModel)
                }
            }
        }
    } ?: run {
        Text(text = "No user data available", modifier = Modifier.padding(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreadorDetails(creador: Creador, viewModel: ConfigViewModel) {
    val nombre by viewModel.nombre.observeAsState(creador.nombre)
    val contrasena by viewModel.contrasena.observeAsState(creador.contraseñaCreador)

    Column {
        TextField(
            value = nombre,
            onValueChange = { viewModel.updateNombre(it) },
            label = { Text("Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        TextField(
            value = contrasena,
            onValueChange = { viewModel.updateContrasena(it) },
            label = { Text("Contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = { viewModel.saveUser() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsumidorDetails(consumidor: Consumidor, viewModel: ConfigViewModel) {
    val nombre by viewModel.nombre.observeAsState(consumidor.nombre)
    val contrasena by viewModel.contrasena.observeAsState(consumidor.contraseñaConsumidor)

    Column {
        TextField(
            value = nombre,
            onValueChange = { viewModel.updateNombre(it) },
            label = { Text("Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        TextField(
            value = contrasena,
            onValueChange = { viewModel.updateContrasena(it) },
            label = { Text("Contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = { viewModel.saveUser() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}

@Composable
fun RecetaCard(receta: Receta, navController: NavController, viewModel: ConfigViewModel) {
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteReceta(receta.idReceta.toLong())
                    showDialog.value = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("No")
                }
            },
            title = { Text("Delete Recipe") },
            text = { Text("Seguro?", textAlign = TextAlign.Center) }
        )
    }

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
            IconButton(onClick = { showDialog.value = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Recipe")
            }
        }
    }
}

@Composable
fun RepostCard(repost: Receta, navController: NavController, viewModel: ConfigViewModel) {
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteRepost(repost.idReceta.toLong())
                    showDialog.value = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("No")
                }
            },
            title = { Text("Delete Repost") },
            text = { Text("Seguro?", textAlign = TextAlign.Center) }
        )
    }

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
            IconButton(onClick = { showDialog.value = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Repost")
            }
        }
    }
}