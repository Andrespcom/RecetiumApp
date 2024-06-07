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
        BodyContent(Modifier.padding(innerPadding), viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyContent(padding: Modifier, viewModel: SearchViewModel) {
    val query = remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.observeAsState(emptyList())

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
            SearchResultCard(result, Modifier.padding(vertical = 4.dp))
        }
    }
}

@Composable
fun SearchResultCard(result: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { /* Acción al hacer clic en la tarjeta */ },
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