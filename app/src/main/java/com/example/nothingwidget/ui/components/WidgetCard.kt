package com.example.nothingwidget.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nothingwidget.ui.theme.BackgroundColor
import com.example.nothingwidget.ui.theme.MutedOutline
import com.example.nothingwidget.ui.theme.PrimaryText
import com.example.nothingwidget.ui.theme.Typography

@Composable
fun WidgetCard(
    title: String,
    sizeLabel: String,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Column(
        modifier = modifier
            .background(BackgroundColor)
            .border(1.dp, PrimaryText, androidx.compose.ui.graphics.RectangleShape)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title.uppercase(), style = Typography.labelSmall, color = PrimaryText)
            Text(text = sizeLabel.uppercase(), style = Typography.labelSmall, color = MutedOutline)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            content = content
        )
    }
}
