package com.example.teachmint.ui.view

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.teachmint.R
import com.example.teachmint.data.model.LocalModel
import com.example.teachmint.data.model.Owner
import com.example.teachmint.data.model.Repository
import com.example.teachmint.ui.viewmodel.LocalViewModel
import com.example.teachmint.ui.viewmodel.RepositoryViewModel
import com.google.gson.Gson


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: RepositoryViewModel? = null,
    localViewModel: LocalViewModel
) {
    val text = remember { mutableStateOf("") }
    val itemsPerPage = 10
    var currentPage by remember { mutableStateOf(0) }
    var totalPage by remember { mutableStateOf(0) }


    val localDataSize = remember { mutableStateOf(0) }
    localViewModel.getSize {
        localDataSize.value = it
    }

    LaunchedEffect(Unit) {
        // By default showing search result for jetpack
        viewModel?.searchRepositories("jetpack")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (viewModel != null) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
                ) {
                    OutlinedTextField(
                        value = text.value,
                        onValueChange = { newText -> text.value = newText },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(text = stringResource(R.string.type_to_search))
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White
                        ),
                        trailingIcon = {
                            Icon(
                                modifier = Modifier.clickable {
                                    viewModel.searchRepositories(text.value)
                                },
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(R.string.search_icon)
                            )
                        },
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            } else {
                Box(modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .background(color = Color.Red)
                    .padding(8.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = stringResource(R.string.please_connect_to_the_internet_and_reopen_the_app),
                        color = Color.White
                    )
                }
            }
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                    .padding(vertical = 50.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }
                if (viewModel != null ){    // if user is connected to internet
                    val paginatedRepositories = viewModel.repositories.chunked(itemsPerPage)
                    totalPage = paginatedRepositories.size
                    items(paginatedRepositories.getOrElse(currentPage) { emptyList() }) { repository ->
                        if (localDataSize.value <= 15) {

                            // storing data to local storage upto 15
                            localViewModel.insertData(
                                LocalModel(
                                    id = repository.id,
                                    name = repository.name,
                                    description = repository.description,
                                    login = repository.owner.login
                                )
                            )
                        }
                        RepositoryItem(repository = repository, navController = navController)
                    }
                } else {    // if user is not connected to internet
                    localViewModel.getAllData()
                    val paginatedRepositories = localViewModel.localData.chunked(itemsPerPage)
                    totalPage = paginatedRepositories.size
                    items(paginatedRepositories.getOrElse(currentPage) { emptyList() }) {
                        val repository = Repository(
                            id = it.id,
                            name = it.name,
                            description = it.description,
                            owner = Owner(
                                login = it.login,
                                avatar_url = ""
                            ),
                            html_url = "",
                            contributors_url = ""
                        )
                        RepositoryItem(repository = repository)
                    }
                }
            }
        },
        bottomBar = {
            Column(
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray)
                        .alpha(0.1f)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (currentPage > 0) currentPage--
                        },
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Previous",
                        tint = if (currentPage > 0) Color.Blue else Color.Gray
                    )
                    Icon(
                        modifier = Modifier.clickable {
                            if (currentPage < totalPage - 1) currentPage++
                        },
                        painter = painterResource(id = R.drawable.ic_arrow_forward),
                        contentDescription = "Next",
                        tint = if (currentPage < totalPage - 1) Color.Blue else Color.Gray
                    )
                }
            }
        }
    )
}


@Composable
fun RepositoryItem(repository: Repository, navController: NavController? = null) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                if (navController == null) {
                    Toast
                        .makeText(context, "Please connect to internet", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val repositoryJson = Uri.encode(Gson().toJson(repository))
                    navController.navigate("detail_screen/$repositoryJson")
                }
            },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(16.dp)) {
            Row {
                Image(
                    painter = rememberImagePainter(
                        data = repository.owner.avatar_url,
                        builder = {
                            placeholder(R.drawable.ic_placeholder)
                            error(R.drawable.ic_error)
                        }),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentScale = ContentScale.Crop,
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.Top,
                ) {
                    Text(
                        text = repository.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = repository.owner.login,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 16.sp,
                            color = Color.Black
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = repository.description ?: "No description available",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp,
                    color = Color.Black
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}