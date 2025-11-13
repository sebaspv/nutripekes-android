import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripekes_android.GuideScreen
import com.example.nutripekes_android.R
import com.example.nutripekes_android.ui.theme.PinkPeke
import com.example.nutripekes_android.ui.theme.GreenPeke
import com.example.nutripekes_android.ui.theme.NutripekesandroidTheme
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.height
import androidx.compose.material3.IconButton
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.Locale


@Composable
fun SideBar(navController: NavController) {
    val juaFontFamily = FontFamily(Font(R.font.jua_regular))

    var PadresExpanded by remember { mutableStateOf(true) }
    var InfoExpanded by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val tts = remember(context) {
        var ttsInstance: TextToSpeech? = null
        val listener = TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsInstance?.language = Locale("es", "ES")
            }
        }
        ttsInstance = TextToSpeech(context, listener)
        ttsInstance
    }

    DisposableEffect(tts) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }
    val padresText = "Padres. Tabla de recomendaciones. Recetario."
    val infoText = "Información. Componentes de una comida balanceada. Señales de hambre y saciedad. Manejo de la selectividad alimentaria."
    val ayudaText = "Ayuda. Guía de Uso."

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.6f)
                .background(GreenPeke)
                .padding(start = 20.dp, end = 20.dp, top = 60.dp, bottom = 20.dp)
        ) {

            // Sección Padres
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { PadresExpanded = !PadresExpanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Padres",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontFamily = juaFontFamily,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { tts?.speak(padresText, TextToSpeech.QUEUE_FLUSH, null, null) }) {
                    Image(
                        painter = painterResource(id = R.drawable.tts),
                        contentDescription = "Escuchar sección Padres",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Icon(
                    imageVector =Icons.Default.KeyboardArrowDown,
                    contentDescription = "expandir info padres",
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .rotate(if (PadresExpanded) 0f else 180f)
                )
            }

            AnimatedVisibility ( visible = PadresExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    SideMenuItem(
                        text = "Tabla de recomendaciones",
                        fontFamily = juaFontFamily,
                        onClick = { navController.navigate("ParentsInfoScreen") }
                    )
                    SideMenuItem(
                        text = "Recetario",
                        fontFamily = juaFontFamily,
                        onClick = { navController.navigate("ParentsInfoScreen") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Sección Información
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { InfoExpanded = !InfoExpanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Información",
                    fontSize = 23.sp,
                    color = Color.White,
                    fontFamily = juaFontFamily,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { tts?.speak(infoText, TextToSpeech.QUEUE_FLUSH, null, null) }) {
                    Image(
                        painter = painterResource(id = R.drawable.tts),
                        contentDescription = "Escuchar sección Información",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "expandir info",
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .rotate(if (InfoExpanded) 0f else 180f)
                )
            }
            AnimatedVisibility(visible = InfoExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    SideMenuItem(
                        text = "Componentes de una comida balanceada",
                        fontFamily = juaFontFamily,
                        onClick = { navController.navigate("InformationPageScreen") }
                    )
                    SideMenuItem(
                        text = "Señales de hambre y saciedad",
                        fontFamily = juaFontFamily,
                        onClick = { navController.navigate("InformationPageScreen") }
                    )
                    SideMenuItem(
                        text = "Manejo de la selectividad alimentaria",
                        fontFamily = juaFontFamily,
                        onClick = { navController.navigate("InformationPageScreen") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ayuda",
                    fontSize = 23.sp,
                    color = Color.White,
                    fontFamily = juaFontFamily,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { tts?.speak(ayudaText, TextToSpeech.QUEUE_FLUSH, null, null) }) {
                    Image(
                        painter = painterResource(id = R.drawable.tts),
                        contentDescription = "Escuchar sección Ayuda",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            SideMenuItem(
                text = "Guía de Uso",
                fontFamily = juaFontFamily,
                onClick = { navController.navigate("GuideScreen") }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.4f)
                .background(PinkPeke)
                .clickable {
                    navController.popBackStack()
                }
        ) {
            Image(
                painter=painterResource(id=R.drawable.logo),
                contentDescription="Logo",
                modifier=Modifier
                    .align(Alignment.TopEnd)
                    .padding(18.dp)
            )
        }
    }
}

@Composable
fun SideMenuItem(text: String, fontFamily: FontFamily, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 19.sp,
        color = Color.White,
        fontFamily = fontFamily,
        modifier = Modifier
            .padding(start = 16.dp, bottom = 8.dp)
            .clickable { onClick() }
    )
}

@Preview(showBackground = true)
@Composable
fun NutriPekesScreenPreviewmenu() {
    NutripekesandroidTheme {
        SideBar(navController = rememberNavController())
    }
}