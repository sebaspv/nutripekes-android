package com.example.nutripekes_android

import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripekes_android.ui.theme.NutripekesandroidTheme
import com.example.nutripekes_android.ui.theme.PinkPeke
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.lazy.items

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.height
import androidx.compose.material3.IconButton
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

class InformationPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutripekesandroidTheme {
                InfoPage(navController = rememberNavController())
            }
        }
    }
}

data class InfoCard(
    val titulo: String,
    val content: List<Pair<String, String>>,
    val color: String
)

@Composable
fun mapColor(color: String): Color {
    return when (color) {
        "R" -> Color(0xFFDF1E34)
        "Y" -> Color(0xFFF1981F)
        "G" -> Color(0xFF255A2E)
        else -> Color.Gray
    }
}

@Composable
fun InformationColumns(
    data: InfoCard,
    tts: TextToSpeech?,
    modifier: Modifier = Modifier
) {
    val colorback = mapColor(data.color)
    val cardTextToRead = remember(data) {
        buildString {
            append(data.titulo).append(". ")
            data.content.forEach { (subtitle, text) ->
                append(subtitle).append(". ").append(text).append(". ")
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Column(
        modifier = modifier
            .background(colorback, shape = RoundedCornerShape(20.dp))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = data.titulo,
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.jua_regular)),
            )
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = { tts?.speak(cardTextToRead, TextToSpeech.QUEUE_FLUSH, null, null) },
                modifier = Modifier.size(24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.tts),
                    contentDescription = "Escuchar sección",
                    modifier = Modifier.size(24.dp)
                )
            }
        }


        // Contenido
        Text(
            text = buildAnnotatedString {
                data.content.forEach { (subtitle, text) ->
                    pushStyle(
                        SpanStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    append(subtitle)
                    pop()

                    append("\n")
                    append(text)
                    append("\n")
                }
            },
            fontSize = 12.sp,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.jua_regular))
        )
    }
}

sealed interface InfoUiState {
    object Idle : InfoUiState
    object Loading : InfoUiState
    data class Success(val data: List<InfoCard>) : InfoUiState
    data class Error(val message: String) : InfoUiState
}

private fun mapInfoDbToUiModel(dbItems: List<InfoCardEntity>): List<InfoCard> {
    return dbItems.map { dbItem ->
        val contentPairs = dbItem.content.map { list ->
            Pair(list[0], list[1])
        }
        InfoCard(
            titulo = dbItem.title,
            content = contentPairs,
            color = dbItem.color
        )
    }
}
@Composable
fun InfoPage(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val scrollState = rememberScrollState()
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

    val dao = remember { AppDatabase.getInstance(context).infoDao() }
    val scope = rememberCoroutineScope()
    var apiUiState by remember { mutableStateOf<InfoUiState>(InfoUiState.Idle) }

    val infoFlow = dao.getAllInfo()
    val infoFromDb by infoFlow.collectAsState(initial = emptyList())

    val Apilist = listOf(
        InfoCard(
            titulo = "Componentes de una comida balanceada",
            content = listOf(
                Pair("Frutas y Verduras", "Aportan abundantes vitaminas, minerales, fibra y antioxidantes y ayudan al buen funcionamiento y desarrollo del organismo, fortalecen el sistema inmunológico y mejoran la digestión."),
                Pair("Origen Animal y Leguminosas", "Contienen proteínas, que son esenciales para la formación y reparación de tejidos, y para el crecimiento muscular, también contribuyen al desarrollo físico y al mantenimiento de los músculos, especialmente en los niños."),
                Pair("Cereales", "Son la principal fuente de energía (carbohidratos), fibra, vitaminas y minerales, aparte de que proveen la energía necesaria para las actividades diarias del cuerpo.")
            ),
            color = "R"
        ),
        InfoCard(
            titulo = "Señales de hambre y saciedad",
            content = listOf(
                Pair("Señales de hambre", "El niño puede buscar activamente la comida, abrir la boca, o succionar con más entusiasmo del pecho o biberón."),
                Pair("Señales de saciedad", "El niño puede apartarse, cerrar la boca, no aceptar más comida, o escupir."),
                Pair("Importancia", "Respetar estas señales permite al niño desarrollar un vínculo saludable con la comida, aprendiendo a reconocer sus propias necesidades.")
            ),
            color = "G"
        ),
        InfoCard(
            titulo = "Manejo de la selectividad alimentaria",
            content = listOf(
                Pair("El rol de los padres", "Los padres son el principal modelo a seguir; por lo tanto, deben mostrar y practicar hábitos alimentarios saludables."),
                Pair("Evitar la obligación", "Forzar a un niño a comer puede generar una situación emocional negativa y dañar la relación con la comida."),
                Pair("Establecer rutinas", "Crear un horario regular de comidas y refrigerios ayuda a establecer hábitos.")
            ),
            color = "Y"
        )
    )

    val mainTitleText = remember {
        buildString {
            append("INFORMACIÓN. ")
            Apilist.forEach { card ->
                append(card.titulo).append(". ")
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PinkPeke)
            .verticalScroll(scrollState)
            .padding(8.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    navController.navigate("StartDayScreen")
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.arrow),
                    contentDescription = "Arrow",
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "regresar a niños",
                    color = Color.White
                )
            }

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(90.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ){
            Text(
                text = "INFORMACIÓN",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = { tts?.speak(mainTitleText, TextToSpeech.QUEUE_FLUSH, null, null) },
                modifier = Modifier.size(30.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.tts),
                    contentDescription = "Escuchar página",
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                scope.launch {
                    apiUiState = InfoUiState.Loading
                    apiUiState = try {
                        val apiItems = ApiClient.instance.getInfo().info
                        dao.refreshInfoFromApi(apiItems)
                        InfoUiState.Idle
                    } catch ( e: Exception) {
                        InfoUiState.Error(e.message ?: "Error desconocido")
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            enabled = apiUiState !is InfoUiState.Loading
        ) {
           Text(
               text = "Actualizar información (Requiere conexión a internet)",
               color = PinkPeke,
               fontFamily = FontFamily(Font(R.font.jua_regular)),
               fontSize = 14.sp
           )
        }
        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            contentAlignment = Alignment.TopCenter
        ) {

            if (apiUiState !is InfoUiState.Loading) {

                val listToShow = if (infoFromDb.isEmpty()) {
                    Apilist
                } else {
                    mapInfoDbToUiModel(infoFromDb)
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listToShow.forEach { cardData ->
                        InformationColumns(data = cardData, tts = tts)
                    }
                }
            }

            when (apiUiState) {
                is InfoUiState.Loading -> {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.padding(32.dp))
                }
                is InfoUiState.Error -> {
                    Text(
                        text = "Error al actualizar: ${(apiUiState as InfoUiState.Error).message}",
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                else -> {

                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NutriPekesScreenPreviews() {
    NutripekesandroidTheme {
        InfoPage(navController = rememberNavController())
    }
}