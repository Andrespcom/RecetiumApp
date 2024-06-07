package com.example.recetium.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recetium.R
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.example.recetium.ui.navigation.AppScreens
import com.example.recetium.ui.screens.home.HomeViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController, homeViewModel: HomeViewModel) {
    val loginSuccess by viewModel.loginSuccess.observeAsState()
    val isChef by viewModel.isChef.observeAsState()
    val recipes by viewModel.recipes.observeAsState()
    val creador by viewModel.creador.observeAsState()

    if (loginSuccess == true) {
        LaunchedEffect(Unit) {
            homeViewModel.setUserType(isChef ?: false, creador)
            homeViewModel.loadRecipes()
            navController.navigate(AppScreens.HomeScreen.route)
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Login(Modifier.align(Alignment.Center), viewModel)
    }
}

@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel) {
    val username by viewModel.username.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val loginEnabled by viewModel.loginEnabled.observeAsState(false)

    Column(modifier = modifier) {
        HeaderImage(Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.padding(4.dp))
        UsernameField(username) { viewModel.onLoginChange(it, password) }
        Spacer(Modifier.padding(4.dp))
        PasswordField(password) { viewModel.onLoginChange(username, it) }
        Spacer(Modifier.padding(8.dp))
        LoginButton(loginEnabled) { viewModel.onLoginSelected() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameField(username: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        value = username,
        onValueChange = onTextFieldChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Usuario") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color(0xFF636262),
            containerColor = Color(0xFFDEDDDD),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        value = password,
        onValueChange = onTextFieldChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Contraseña") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color(0xFF636262),
            containerColor = Color(0xFFDEDDDD),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun LoginButton(loginEnabled: Boolean, onLoginSelected: () -> Unit) {
    Button(
        onClick = { onLoginSelected() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE6D354),
            disabledContainerColor = Color(0xFFF78058),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        enabled = loginEnabled
    ) {
        Text(text = "Iniciar sesión")
    }
}

@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(R.drawable.logo),
        contentDescription = "Header",
        modifier = modifier
    )
}