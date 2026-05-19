package com.example.hptuners.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage

@Composable
fun LoadingAsyncImage(url: String, id: String, modifier: Modifier = Modifier) {
    SubcomposeAsyncImage(
        model = url,
        loading = { CircularProgressIndicator(modifier = Modifier.wrapContentSize()) },
        error = { Text("Failed to get the image :(") },
        contentDescription = "Picture for Cat ID: $id",
        modifier = modifier
            .heightIn(60.dp)
            .widthIn(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.surfaceDim), // Defines thickness and color
                shape = RoundedCornerShape(12.dp)
            )
    )
}