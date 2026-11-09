package com.example.poangtavlan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poangtavlan.ui.theme.ClickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClickerTheme {
                ClickerScreen(name = "Worst Clicker Ever")
            }
        }
    }
}

@Composable
fun ClickerScreen(name: String) {
    var points by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF303030))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 64.dp)
        )

        Text(
            text = "$points points",
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = { points = points + 1 },
            modifier = Modifier.size(160.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFD700),
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "+1",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
fun ClickerScreenPreview() {
    ClickerTheme {
        ClickerScreen(name = "Worst Clicker Ever")
    }
}