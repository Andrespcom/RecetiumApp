package com.example.recetium.ui.screens.post

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.recetium.ui.screens.home.HomeViewModel

@Composable
fun PostRecetasScreen(
    viewModel: PostRecetasViewModel = viewModel(),
    homeViewModel: HomeViewModel,
    onPostSuccess: () -> Unit
) {
    val description by viewModel.description.observeAsState("")
    val ingredients by viewModel.ingredients.observeAsState("")
    val imageUri by viewModel.imageUri.observeAsState(null)
    val uploadStatus by viewModel.uploadStatus.observeAsState(false)

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.setImageUri(uri)
    }

    // Inicializar el ViewModel con el HomeViewModel
    LaunchedEffect(Unit) {
        viewModel.initialize(homeViewModel)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Añadir Receta",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        DescriptionField(description) { viewModel.setDescription(it) }
        IngredientsField(ingredients) { viewModel.setIngredients(it) }

        Spacer(modifier = Modifier.height(16.dp))

        ImagePickerButton { imagePickerLauncher.launch("image/*") }

        imageUri?.let {
            ImagePreview(it)
        }

        Spacer(modifier = Modifier.height(16.dp))

        SubmitButton {
            viewModel.postReceta(onPostSuccess)
        }

        if (uploadStatus) {
            Text(text = "Receta subida con éxito", color = Color.Green)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionField(description: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = description,
        onValueChange = onValueChange,
        label = { Text("Descripción") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientsField(ingredients: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = ingredients,
        onValueChange = onValueChange,
        label = { Text("Ingredientes (separados por comas)") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        singleLine = false,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text
        )
    )
}

@Composable
fun ImagePickerButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = "Seleccionar Imagen")
    }
}

@Composable
fun ImagePreview(imageUri: Uri) {
    Image(
        painter = rememberAsyncImagePainter(imageUri),
        contentDescription = null,
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun SubmitButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
        Text(text = "Añadir Receta")
    }
}