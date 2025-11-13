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
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.height
import androidx.compose.material3.IconButton
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.Locale


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
fun GuideSection(
    title: String,
    content: String,
    tts: TextToSpeech?,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .background(Color.White.copy(0.1f), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = {
                tts?.speak("$title. $content", TextToSpeech.QUEUE_FLUSH, null, null)
            }) {
                Image(
                    painter = painterResource(id=R.drawable.tts),
                    contentDescription = "Esccuchar sección",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

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
    val context = LocalContext.current
    val tts = remember (context){
        var ttsInstance: TextToSpeech? = null
        val listener = TextToSpeech.OnInitListener{status ->
            if(status == TextToSpeech.SUCCESS){
                ttsInstance?.language= Locale("es", "ES")
            }
        }
        ttsInstance= TextToSpeech(context, listener)
        ttsInstance
    }

    DisposableEffect(tts) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    val mainTitleText = "Guía de Uso. " +
            "1. Pantalla de inicio. Esta es tu pantalla principal. Aquí puedes ver el progreso diario de tu peque. " +
            "2. Los contadores. En la pantalla principal, verás varios iconos de comida (verduras, cereales, etc.) y un vaso de agua. ¡Toca cada icono para registrar una porción de alimento ingerido, y observa como la manzana cambia a lo largo de cada comida! " +
            "Menú y Navegación. Usa el icono de menú (☰) para navegar a las diferentes secciones de la app. Usa la flecha 'regresar' en la esquina superior izquierda para volver a la pantalla anterior en cualquier momento. " +
            "Ver Información Nutricional. En la barra de menú (tocando el icono ☰ en la pantalla principal), encontrarás la sección 'Información'. Allí podrás leer sobre los componentes de una comida balanceada, señales de hambre, y manejo de la selectividad alimentaria."

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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ){
            Text(
                text = "GUÍA DE USO",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = {
                tts?.speak(mainTitleText, TextToSpeech.QUEUE_FLUSH, null, null)
            }) {
                Image(
                    painter = painterResource(id = R.drawable.tts), // O R.drawable.bocina si lo tienes
                    contentDescription = "Escuchar guía de uso completa",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
        
        GuideSection(
            title = "1. Pantalla de inicio",
            content = "Esta es tu pantalla principal. Aquí puedes ver el progreso diario de tu peque.",
            tts = tts
        )

        GuideSection(
            title = "2. Los contadores",
            content = "En la pantalla principal, verás varios iconos de comida (verduras, cereales, etc.) y un vaso de agua.\n\n" +
                    "¡Toca cada icono para registrar una porción de alimento ingerido, y observa como la manzana cambia a lo largo de cada comida!",
            tts = tts
        )

        GuideSection(
            title = "Menú y Navegación",
            content = "Usa el icono de menú (☰) para navegar a las diferentes secciones de la app.\n\n" +
                    "Usa la flecha 'regresar' en la esquina superior izquierda para volver a la pantalla anterior en cualquier momento.",
            tts = tts
        )

        GuideSection(
            title = "Ver Información Nutricional",
            content = "En la barra de menú (tocando el icono ☰ en la pantalla principal), encontrarás la sección 'Información'.\n\n" +
                    "Allí podrás leer sobre los componentes de una comida balanceada, señales de hambre, y manejo de la selectividad alimentaria.",
            tts = tts
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