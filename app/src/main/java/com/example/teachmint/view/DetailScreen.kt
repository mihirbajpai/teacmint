package com.example.teachmint.ui.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.teachmint.R
import com.example.teachmint.data.model.Contributor
import com.example.teachmint.data.model.Repository
import com.example.teachmint.ui.viewmodel.RepositoryViewModel

@Composable
fun DetailScreen(repository: Repository, viewModel: RepositoryViewModel) {
    val contributors = viewModel.contributors
    viewModel.getContributors(ownerName = repository.owner.login, repoName = repository.name)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.repository_details), style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 24.sp, color = Color.Gray
                ), maxLines = 2, overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Row {
            Image(
                painter = rememberImagePainter(data = repository.owner.avatar_url, builder = {
                    placeholder(R.drawable.ic_placeholder)
                    error(R.drawable.ic_error)
                }),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 2.dp, color = Color.Black, shape = RoundedCornerShape(8.dp)
                    ),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = repository.name, style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold, fontSize = 30.sp, color = Color.Black
                    ), maxLines = 2, overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.owner, repository.owner.login),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp, color = Color.Black
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column {
            Text(text = stringResource(R.string.description), fontWeight = FontWeight.Bold, color = Color.Black)
            Text(
                text = repository.description ?: stringResource(R.string.no_description_available),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Black
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        ClickableUrlText(
            url = repository.html_url
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(text = stringResource(R.string.contributors), fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(contributors) { contributor ->
                ContributorCard(contributor)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContributorCard(contributor: Contributor) {
    val context = LocalContext.current
    Box {
        Column(modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(contributor.html_url))
                context.startActivity(intent)
            }
            .background(Color.White)
            .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = rememberImagePainter(data = contributor.avatar_url, builder = {
                    placeholder(R.drawable.ic_placeholder)
                    error(R.drawable.ic_error)
                }),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Black, CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = contributor.login,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        Badge(
            containerColor = Color.Magenta,
            contentColor = Color.White,
            modifier = Modifier.padding(4.dp)
        ) {
            Text(text = contributor.contributions.toString())
        }
    }

}

@Composable
fun ClickableUrlText(url: String) {
    val context = LocalContext.current
    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = Color.Blue,
            )
        ) {
            append(url)
        }
        addStringAnnotation(
            tag = stringResource(R.string.url), annotation = url, start = 0, end = url.length
        )
    }

    Column {
        Text(text = stringResource(R.string.url_), fontWeight = FontWeight.Bold, color = Color.Black)
        ClickableText(text = annotatedString, onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW, Uri.parse(annotation.item)
                        )
                    )
                }
        })
    }
}