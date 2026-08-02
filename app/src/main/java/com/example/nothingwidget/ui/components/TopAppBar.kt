package com.example.nothingwidget.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nothingwidget.ui.theme.BackgroundColor
import com.example.nothingwidget.ui.theme.PrimaryText
import com.example.nothingwidget.ui.theme.SpaceMono

@Composable
fun NothingTopAppBar(
    title: String,
    showBack: Boolean = false,
    onBack: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(BackgroundColor)
            .border(1.dp, PrimaryText, androidx.compose.ui.graphics.RectangleShape)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showBack) {
            IconButton(onClick = onBack, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PrimaryText)
            }
            Spacer(modifier = Modifier.width(16.dp))
        } else {
            Icon(Icons.Default.GridView, contentDescription = "Grid", tint = PrimaryText, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
        }

        Text(
            text = title.uppercase(),
            color = PrimaryText,
            fontFamily = SpaceMono,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            letterSpacing = 2.sp,
            modifier = Modifier.weight(1f)
        )
    }
}
