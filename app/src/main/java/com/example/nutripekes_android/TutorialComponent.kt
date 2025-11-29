package com.example.nutripekes_android

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TutorialOverlay(
    currentStep: Int,
    onNextStep: () -> Unit,
    onSkip: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f))
            .clickable(enabled = false) {}
    ) {
     if (currentStep < 3) {
            Text(
                text = "Omitir",
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 40.dp, end = 20.dp)
                    .clickable { onSkip() }
            )
        }

        if (currentStep == 1) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 100.dp, start = 20.dp, end = 40.dp)
            ) {
                Text(
                    text = "⬆\nMenú de Padres",
                    color = Color.Yellow,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Aquí encontrarás el recetario, la tabla de recomendaciones y opciones para configurar la edad.",
                    color = Color.White,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                TutorialNextButton(onNextStep)
            }
        }

        if (currentStep == 2) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 100.dp, end = 20.dp, start = 40.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Ayuda Auditiva\n⬆",
                    color = Color.Yellow,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Toca este botón cuando necesites escuchar las instrucciones en voz alta.",
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                )
                Spacer(modifier = Modifier.height(16.dp))
                TutorialNextButton(onNextStep)
            }
        }

        if (currentStep == 3) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 150.dp, start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Registra tus Alimentos",
                    color = Color.Yellow,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Toca los círculos de abajo para agregar porciones durante el día. ¡Intenta llenar la manzana!",
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "⬇",
                    color = Color.Yellow,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onSkip,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("¡Entendido!", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun TutorialNextButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Text("Siguiente", color = Color.Black)
        Spacer(modifier = Modifier.width(4.dp))
        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.Black)
    }
}