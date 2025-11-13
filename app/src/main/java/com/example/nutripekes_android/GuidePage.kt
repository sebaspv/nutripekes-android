package com.example.nutripekes_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripekes_android.ui.theme.NutripekesandroidTheme


class GuideScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutripekesandroidTheme {
                GuideScreen(navController = rememberNavController())
            }
        }
    }
}

@Composable
fun GuideSection(title: String, content: String, modifier: Modifier = Modifier){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .background(Color.White.copy(0.1f), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = content,
            fontSize = 14.sp,
            color = Color.White,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun GuideScreen(navController : NavController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE55B57))
            .verticalScroll(scrollState)
            .padding(8.dp, 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.Start)
                .clickable { navController.popBackStack() }
                .padding(8.dp, 12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.arrow),
                contentDescription = "Back",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "regresar",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        Text(
            text = "GUÍA DE USO",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        GuideSection(
            title = "\uD83D\uDCCA 1. Pantalla de inicio",
            content = "Esta es tu pantalla principal. Aquí puedes ver el progreso diario de tu peque."
        )

        GuideSection(
            title = "🍎 2. Los contadores",
            content = "En la pantalla principal, verás varios iconos de comida (verduras, cereales, etc.) y un vaso de agua.\n\n" +
                    "¡Toca cada icono para registrar una porción de alimento ingerido, y observa como la manzana cambia a lo largo de cada comida!"
        )

        GuideSection(
            title = "☰ Menú y Navegación",
            content = "Usa el icono de menú (☰) para navegar a las diferentes secciones de la app.\n\n" +
                    "Usa la flecha 'regresar' en la esquina superior izquierda para volver a la pantalla anterior en cualquier momento."
        )

        GuideSection(
            title = "ℹ️ Ver Información Nutricional",
            content = "En la barra de menú (tocando el icono ☰ en la pantalla principal), encontrarás la sección 'Información'.\n\n" +
                    "Allí podrás leer sobre los componentes de una comida balanceada, señales de hambre, y manejo de la selectividad alimentaria."
        )

    }
}


@Preview(showBackground = true)
@Composable
fun NutriPekesScreenPreviewguide() {
    NutripekesandroidTheme {
        GuideScreen(navController = rememberNavController())
    }
}