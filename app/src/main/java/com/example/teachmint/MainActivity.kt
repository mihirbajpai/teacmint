package com.example.teachmint

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.teachmint.data.local.LocalDatabase
import com.example.teachmint.data.model.Repository
import com.example.teachmint.repository.LocalRepository
import com.example.teachmint.ui.theme.TeachMintTheme
import com.example.teachmint.ui.view.DetailScreen
import com.example.teachmint.ui.view.MainScreen
import com.example.teachmint.ui.viewmodel.LocalViewModel
import com.example.teachmint.ui.viewmodel.LocalViewModelFactory
import com.example.teachmint.ui.viewmodel.RepositoryViewModel
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeachMintTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    AppContainer()
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun AppContainer() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Using Room Database for local data
    val userDao = LocalDatabase.getDatabase(LocalContext.current).localDao()
    val userRepository = LocalRepository(userDao)
    val localViewModel: LocalViewModel = viewModel(factory = LocalViewModelFactory(userRepository))

    val isConnected = remember { mutableStateOf(isNetworkAvailable(context)) }

    val viewModel: RepositoryViewModel = viewModel()
    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(
                navController = navController,
                viewModel = if (isConnected.value) viewModel else null,
                localViewModel = localViewModel
            )
        }
        composable(
            "detail_screen/{repository}",
            arguments = listOf(navArgument("repository") { type = NavType.StringType })
        ) { backStackEntry ->
            val repositoryJson = backStackEntry.arguments?.getString("repository")
            val repository = Gson().fromJson(repositoryJson, Repository::class.java)
            DetailScreen(repository = repository, viewModel = viewModel)
        }
    }
}

// Checking that user is connected with internet or not
fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}