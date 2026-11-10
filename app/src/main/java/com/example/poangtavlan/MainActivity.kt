package com.example.poangtavlan

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

    val prefs = LocalContext.current.getSharedPreferences(
        "worst_clicker",
        Context.MODE_PRIVATE
    )

    var points by remember {
        mutableLongStateOf(prefs.getLong("points", 0L))
    }
    var pointsPerClick by remember {
        mutableLongStateOf(prefs.getLong("pointsPerClick", 1L))
    }
    var upgradeCost by remember {
        mutableLongStateOf(prefs.getLong("upgradeCost", 50L))
    }
    var prestigeLevel by remember {
        mutableLongStateOf(prefs.getLong("prestigeLevel", 0L))
    }
    var bonusProgress by remember {
        mutableLongStateOf(prefs.getLong("bonusProgress", 0L))
    }

    LaunchedEffect(
        points, pointsPerClick, upgradeCost,
        prestigeLevel, bonusProgress
    ) {
        prefs.edit()
            .putLong("points", points)
            .putLong("pointsPerClick", pointsPerClick)
            .putLong("upgradeCost", upgradeCost)
            .putLong("prestigeLevel", prestigeLevel)
            .putLong("bonusProgress", bonusProgress)
            .apply()
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.05f else 1f,
        label = "coinScale"
    )

    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF303030))
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            fontSize = 41.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 5.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Prestige:\n$prestigeLevel (+${prestigeLevel * 10}% bonus)",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = if (prestigeLevel == 0L) Color.Gray else Color.White
            )

            Text(
                text = "Level:\n${pointsPerClick - 1}",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = Color.White
            )

            Text(
                text = "Points:\n$points",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = Color.White
            )

            Button(
                interactionSource = interactionSource,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    points += pointsPerClick

                    // Beräknar prestige-bonusen i tiondelar, ger hela bonuspoäng
                    // och sparar resten till nästa klick:
                    bonusProgress += pointsPerClick * prestigeLevel
                    points += bonusProgress / 10L
                    bonusProgress %= 10L
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(1f)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    },

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD700),
                    contentColor = Color.Black,
                )
            ) {
                Text(
                    text = "+$pointsPerClick",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(76.dp),
                onClick = {
                    points -= upgradeCost
                    pointsPerClick += 1L
                    upgradeCost += pointsPerClick * 100L

                    if (upgradeCost > 250L) {
                        upgradeCost = (upgradeCost / 100L) * 100L
                    }

                    if (upgradeCost >= 900L) {
                        upgradeCost = ((upgradeCost + 250L) / 500L) * 500L
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5CCB24),
                    contentColor = Color.Black,
                ),
                enabled = (points >= upgradeCost),
            ) {
                Text(
                    lineHeight = 26.sp,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    text = "Upgrade:\n$upgradeCost points"
                )
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(76.dp),
                onClick = {
                    prestigeLevel += 1
                    pointsPerClick = 1L
                    points = 0L
                    upgradeCost = 50L
                    bonusProgress = 0L
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5CCB24),
                    contentColor = Color.Black,
                ),
                enabled = pointsPerClick >= 11L,
            ) {
                Text(
                    lineHeight = 26.sp,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    text = "Prestige"
                )
            }
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