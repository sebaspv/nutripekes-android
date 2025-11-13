package com.example.nutripekes_android

import android.R.id.message
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripekes_android.ui.theme.NutripekesandroidTheme
import com.example.nutripekes_android.ui.theme.PinkPeke
import org.intellij.lang.annotations.JdkConstants
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.height
import androidx.compose.material3.IconButton
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

class ParentsInfo : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutripekesandroidTheme {
                ParentsInformation(navController = rememberNavController())
            }
        }
    }
}

//Tabla de recomendaciones
@Composable
fun RecomendacionesTable(
    modifier: Modifier = Modifier,
    tts: TextToSpeech?
) {
    val headerColor = Color(0xFFD9D9D9)
    val borderColor = Color.Gray
    val rowHeight = 56.dp

    val headers = listOf("Edad", "Peso (kg)", "Talla (cm)", "IMC (kg/m2)")
    val rows = listOf(
        listOf("3 años", "14 - 15 kg", "95 - 96 cm", "15.8 - 16.0"),
        listOf("4 años", "16 - 17 kg", "102 - 104 cm", "15.7 - 16.1"),
        listOf("5 años", "18 - 19 kg", "109 - 111 cm", "15.8 - 16.3"),
        listOf("6 años", "20 - 21 kg", "115 - 117 cm", "15.9 - 16.4"),
        listOf("7 años", "22 - 24 kg", "121 - 123 cm", "16.0 - 16.7"),
        listOf("8 años", "25 - 27 kg", "128 - 130 cm", "16.3 - 17.0"),
        listOf("9 años", "28 - 32 kg", "133 - 135 cm", "16.8 - 17.5"),
        listOf("10 años", "32 - 36 kg", "138 - 142 cm", "17.2 - 18.0")
    )

    val titleText = "Tabla de recomendaciones"
    val headersText = headers.joinToString(", ") // "Edad, Peso (kg), Talla (cm), ..."

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Gray)
            .border(2.dp, borderColor, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = titleText,
                    fontSize = 24.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.jua_regular)),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = {
                    tts?.speak("$titleText. $headersText", TextToSpeech.QUEUE_FLUSH, null, null)
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.tts),
                        contentDescription = "Escuchar título y cabeceras",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFD9D9D9))
            ) {
                // Header
                Row(modifier = Modifier.fillMaxWidth()) {
                    headers.forEachIndexed { index, title ->
                        val weight = when(index) {
                            0 -> 0.8f
                            1 -> 1.1f
                            2 -> 1.1f
                            3 -> 1.0f
                            else -> 1f
                        }
                        Box(
                            modifier = Modifier
                                .weight(weight)
                                .height(rowHeight)
                                .background(headerColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Divider(color = borderColor, thickness = 3.dp)

                // Filas de contenido
                rows.forEachIndexed { rowIndex, fila ->
                    val rowText = remember(fila, headers) {
                        headers.zip(fila).joinToString(". ") { (header, data) -> "$header, $data" }
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        fila.forEachIndexed { index, celda ->
                            val weight = when(index) {
                                0 -> 0.8f
                                1 -> 1.1f
                                2 -> 1.1f
                                3 -> 1.0f
                                else -> 1f
                            }
                            Box(
                                modifier = Modifier
                                    .weight(weight)
                                    .height(rowHeight),
                                contentAlignment = Alignment.Center
                            ) {
                                if (index == 0) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text( // Texto de la celda
                                            text = celda,
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.weight(1f)
                                        )
                                        // Bocina para la fila
                                        IconButton(
                                            onClick = { tts?.speak(rowText, TextToSpeech.QUEUE_FLUSH, null, null) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.bocina),
                                                contentDescription = "Escuchar fila $rowIndex",
                                                modifier = Modifier.size(16.dp) // Icono pequeño
                                            )
                                        }
                                    }
                                }else{
                                    Text(
                                        text = celda,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                    if (rowIndex != rows.lastIndex) {
                        Divider(color = borderColor, thickness = 1.dp)
                    }
                }
            }
        }
    }
}

data class RecipeCardData(
    val name: String,
    val ingredients: List<String>,
    val imageUrl: String,
    val instructions: String
)

sealed interface RecipeUiState {
    object Idle : RecipeUiState
    object Loading : RecipeUiState
    data class Error(val message : String) : RecipeUiState
    object Success : RecipeUiState
}

private fun mapDbModelToUiModel(dbItems: List<RecipeWithIngredients>): List<RecipeCardData> {
    return dbItems.map { dbItem ->
        RecipeCardData(
            name = dbItem.recipe.name,
            instructions = dbItem.recipe.instructions,
            imageUrl = dbItem.recipe.imgUrl,
            ingredients = dbItem.ingredients.map { it.name }
        )
    }
}

//private fun mapResponseToUiModel ( apiItems: List<RecipeApiResponseItem>): List<RecipeCardData> {
//    return apiItems.map { item ->
//        RecipeCardData(
//            name = item.name,
//            ingredients = item.ingredients,
//            instructions = item.instructions,
//            imageUrl = item.image
//        )
//    }
//}
//Recetario
@Composable
fun DefaultRecetario(
    modifier: Modifier = Modifier,
    tts: TextToSpeech?
) {
    val borderColor = Color.Gray
    val titleText = "Recetario"
    val instructionsText = "Instrucciones: " +
            "1. En un bowl mezcla los ingredientes secos: harina integral, harina de avena, polvo para hornear, canela y sal. " +
            "2. En otro bowl bate el huevo, la leche, el aceite y la vainilla. " +
            "3. Une las dos mezclas y revuelve hasta que quede una masa suave (no batir demasiado). " +
            "4. Calienta un sartén antiadherente, engrásalo ligeramente con aceite en spray o unas gotas de aceite con servilleta.Vierte ¼ de taza de mezcla por cada hot cake. " +
            "5. Cocina a fuego medio hasta que salgan burbujitas, voltea y cocina 1–2 minutos más."

    val ingredientsText = "Ingredientes: " +
            "1 taza (100 g) de harina integral de trigo. " +
            "½ taza (50 g) de harina de avena (puedes moler avena en la licuadora). " +
            "1 cucharadita de polvo para hornear. " +
            "½ cucharadita de canela en polvo (opcional). " +
            "1 pizca de sal. " +
            "1 taza (240 ml) de leche baja en grasa o bebida vegetal sin azúcar. " +
            "1 huevo. " +
            "1 cucharada de aceite de oliva. " +
            "1 cucharadita de extracto de vainilla. " +
            "1 a 2 cucharadas de miel de abeja o maple natural (opcional, para dar dulzor)."

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Gray)
            .border(2.dp, borderColor, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = titleText,
                    fontSize = 24.sp,
                    color = Color.Yellow,
                    fontFamily = FontFamily(Font(R.font.jua_regular)),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = { tts?.speak(titleText, TextToSpeech.QUEUE_FLUSH, null, null) }) {
                    Image(
                        painter = painterResource(id = R.drawable.tts),
                        contentDescription = "Escuchar título",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Contenido principal: instrucciones a la izquierda, imagen e ingredientes a la derecha
            Row(modifier = Modifier.fillMaxWidth()) {
                // Columna de instrucciones
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(text = "Hotcakes")
                    Text(text = "Instrucciones:", fontSize = 18.sp, color = Color.Red, /*...*/)
                    Text(text = "1. En un bowl mezcla los ingredientes secos...", /*...*/)
                    
                    Spacer(Modifier.width(8.dp))
                    
                    IconButton(onClick = { tts?.speak(instructionsText, TextToSpeech.QUEUE_FLUSH, null, null) }) {
                            Image(
                                painter = painterResource(id = R.drawable.tts),
                                contentDescription = "Escuchar instrucciones",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                }

                // Columna de imagen + ingredientes
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.hotcakes),
                        contentDescription = "Hotcakes",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Text(
                            text = "Ingredientes:",
                            fontSize = 18.sp,
                            color = Color.Red,
                            fontFamily = FontFamily(Font(R.font.jua_regular)),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        IconButton(onClick = { tts?.speak(ingredientsText, TextToSpeech.QUEUE_FLUSH, null, null) }) {
                            Image(
                                painter = painterResource(id = R.drawable.tts),
                                contentDescription = "Escuchar ingredientes",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Text(
                        text = ingredientsText.replace("Ingredientes: ", ""),
                        fontSize = 14.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.jua_regular))
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeCardItem(data: RecipeCardData, modifier: Modifier = Modifier) {
    val borderColor = Color.Gray

    Box(modifier = Modifier
        .clip(RoundedCornerShape(20.dp))
        .background(Color.Gray)
        .border(2.dp, borderColor, RoundedCornerShape(20.dp))
        .padding(16.dp)
    ) {
        Column {
            Text(
                text = data.name,
                fontSize = 24.sp,
                color = Color.Yellow,
                fontFamily = FontFamily(Font(R.font.jua_regular)),
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = "Instrucciones: ",
                        fontSize = 18.sp,
                        color = Color.Red,
                        fontFamily = FontFamily(Font(R.font.jua_regular))
                    )

                    Text(
                        text = data.instructions,
                        fontSize = 12.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.jua_regular))
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    AsyncImage(
                        model = data.imageUrl,
                        contentDescription = data.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Text(
                        text = "Ingredientes:",
                        fontSize = 18.sp,
                        color = Color.Red,
                        fontFamily = FontFamily(Font(R.font.jua_regular)),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = data.ingredients.joinToString(separator = "\n") { "- $it" },
                        fontSize = 14.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.jua_regular))
                    )
                }
            }
        }
    }
}
//Contenido de la pagina
@Composable
fun ParentsInformation(modifier: Modifier = Modifier, navController: NavController) {
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
    val dao = remember { AppDatabase.getInstance(context).recipeDao() }
    val scope = rememberCoroutineScope()

    var apiUiState by remember { mutableStateOf<RecipeUiState>(RecipeUiState.Idle) }
    var filterText by remember { mutableStateOf("") }

     val recipesFlow: Flow<List<RecipeWithIngredients>> = remember(filterText) {
         if (filterText.isBlank()) {
             dao.getAllRecipes()
         } else {
             val ingredientsToSearch = filterText.split(",")
                 .map { it.trim().lowercase() }
                 .filter { it.isNotEmpty() }
             dao.getRecipesByIngredients(ingredientsToSearch)
         }
     }

    val recipesFromDb by recipesFlow.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .background(PinkPeke)
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

        // titulo
        Text(
            text = "Padres",
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.jua_regular)),
            fontSize = 70.sp,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        RecomendacionesTable(
            tts = tts
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                scope.launch {
                    apiUiState = RecipeUiState.Loading
                    apiUiState = try {
                        val apiItems = ApiClient.instance.getRecipes()
                        dao.refreshRecipesFromApi(apiItems)
                        RecipeUiState.Idle
                    } catch (e: Exception) {
                        RecipeUiState.Error(e.message ?: "Error desconocido")
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            enabled = apiUiState !is RecipeUiState.Loading
        ) {
            Text(
                text = "Actualizar recetario (Requiere internet)",
                color = PinkPeke,
                fontFamily = FontFamily(Font(R.font.jua_regular)),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = filterText,
            onValueChange = { filterText = it },
            label = { Text("Filtrar por ingrediente (ej: pollo, arroz)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .background(Color.White, RoundedCornerShape(8.dp)),
            singleLine = true
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {

            if (apiUiState !is RecipeUiState.Loading) {

                val recipesToShow = mapDbModelToUiModel(recipesFromDb)

                if (recipesToShow.isEmpty()) {
                    if (filterText.isBlank()) {

                        DefaultRecetario(
                        tts = tts
                        )
                    } else {

                        Text(
                            text = "No se encontraron recetas con '$filterText'",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        recipesToShow.forEach { recipeData ->
                            RecipeCardItem(data = recipeData)
                        }
                    }
                }
            }

            when (apiUiState) {
                is RecipeUiState.Loading -> {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.padding(32.dp))
                }
                is RecipeUiState.Error -> {
                    Text(
                        text = "Error al actualizar: ${(apiUiState as RecipeUiState.Error).message}",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                }
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun NutriPekesScreenPreviewparents() {
    NutripekesandroidTheme {
        ParentsInformation(navController = rememberNavController())
    }
}