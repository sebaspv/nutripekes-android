package com.example.nutripekes_android

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import java.util.Locale
import com.example.nutripekes_android.ui.theme.NutripekesandroidTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import android.media.MediaPlayer
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.coroutineScope

class Startday : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StartdayScreen(
                navController = rememberNavController(),
            )
        }
    }
}

data class FoodEditState(
    val label: String,
    val currentCount: Int,
    val maxCount: Int,
    val color: Color,
    val key: Preferences.Key<Int>
)

@Composable
fun StartdayScreen(
    navController : NavController,
    viewModel : StartDayViewModel = viewModel()
) {
    val instructionText = "Bienvenido a Nutripekes. " +
            "Para empezar registra las porciones de alimentos que has consumido el día de hoy, " +
            "por ejemplo, si has comido dos porciones de frutas, presiona el icono de frutas dos veces"
    val context = LocalContext.current

    var isTtsInitialized by remember { mutableStateOf(false) }

    var isSpeakingInstructions by remember { mutableStateOf(false) }

    val tts = remember(context) {
        var ttsInstance: TextToSpeech? = null
        val listener = TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                isTtsInitialized = true
                ttsInstance?.language = Locale("es", "ES")
            }
        }
        ttsInstance = TextToSpeech(context, listener)
        ttsInstance
    }

    val appleChangedSound = remember(context){
        MediaPlayer.create(context, R.raw.manzanasound)
    }

    DisposableEffect(tts) {
        onDispose {
            tts.stop()
            tts.shutdown()
            appleChangedSound?.release()
        }
    }

    // Calcular edad y porciones
    val settingsManager = remember { SettingsManager(context) }
    val coroutineScope = rememberCoroutineScope()
    val birthYear by settingsManager.birthYearFlow.collectAsState(initial = null)

    var showBirthYearDialog by remember { mutableStateOf(false) }
    LaunchedEffect(birthYear) {
        if (birthYear != null) {
            if (birthYear == 0) {
                showBirthYearDialog = true
            }
            else {
                showBirthYearDialog = false
            }
        }
    }

    if (showBirthYearDialog) {
        BirthYearEntryDialog(
            onDismiss = {
                showBirthYearDialog = false
            },
            onSave = { year ->
                coroutineScope.launch {
                    settingsManager.saveBirthYear(year)
                }
                showBirthYearDialog = false
            }
        )
    }


    val age = PortionLogic.calculateAge(birthYear ?: 0)
    val portions = PortionLogic.getPortionsForAge(age)

    val dailyProgress by settingsManager.dailyProgressFlow.collectAsState(initial = DailyProgress())

    val verdurasfrutasCount = dailyProgress.verduras
    val origenanimalCount = dailyProgress.animal
    val leguminosasCount = dailyProgress.leguminosas
    val cerealesCount = dailyProgress.cereales
    val aguaCount = dailyProgress.agua

    //Valores maximos
    val verdurasfrutasMax = portions.verdurasfrutasMax
    val origenanimalMax = portions.origenanimalMax
    val leguminosasMax = portions.leguminosasMax
    val cerealesMax = portions.cerealesMax
    val aguaMax = portions.aguaMax

    // Colores
    val verdurasfrutasColor = Color(0xFF1BCC21)
    val origenanimalColor = Color(0xFFDE1C0E)
    val leguminosasColor = Color(0xFFFA6806)
    val cerealesColor = Color(0xFFFFC107)
    val aguaColor = Color(0xFF1BA6CC)

    var activeFoodEdit by remember { mutableStateOf<FoodEditState?>(null) }

    val tutorialShown by settingsManager.tutorialShownFlow.collectAsState(initial = true)
    var currentTutorialStep by remember { mutableStateOf(1) }

    if (activeFoodEdit != null) {
        val food = activeFoodEdit!!

        FoodEditDialog(
            label = food.label,
            currentCount = food.currentCount,
            maxCount = food.maxCount,
            color = food.color,
            onDismiss = { activeFoodEdit = null },
            onAdd = {
                if (food.currentCount < food.maxCount) {
                    val newCount = food.currentCount + 1
                    coroutineScope.launch {
                        settingsManager.savePortion(food.key, newCount)
                        activeFoodEdit = food.copy(currentCount = newCount)
                    }
                }
            },
            onRemove = {
                if (food.currentCount > 0) {
                    val newCount = food.currentCount - 1
                    coroutineScope.launch {
                        settingsManager.savePortion(food.key, newCount)
                        activeFoodEdit = food.copy(currentCount = newCount)
                    }
                }
            }
        )
    }

    val totalCurrent = verdurasfrutasCount+origenanimalCount+leguminosasCount+cerealesCount+aguaCount
    val totalMax = verdurasfrutasMax+origenanimalMax+leguminosasMax+cerealesMax+aguaMax
    val appleImageRes = when{
        totalCurrent == 0 -> R.drawable.manzana1
        totalMax > 0 && totalCurrent >= totalMax -> R.drawable.manzana5
        totalMax > 0 -> {
            val progressPercent = totalCurrent.toFloat()/totalMax.toFloat()
            when{
                progressPercent <= 0.33f -> R.drawable.manzana2
                progressPercent <= 0.66f -> R.drawable.manzana3
                else -> R.drawable.manzana4
            }
        }

        else -> R.drawable.manzana1
    }

    val isFirstLoad = remember { mutableStateOf(true) }
    LaunchedEffect(appleImageRes) {
        if (isFirstLoad.value){
            isFirstLoad.value=false
        }else{
            appleChangedSound?.start()
        }
    }

    val showPopup by viewModel.showPopup.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when(event){
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.startTimer()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    viewModel.PauseTimer()
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose{
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if(showPopup) {
        BlockingPopup(
            onDismiss = {
                viewModel.dismissPopup()
                navController.popBackStack()
            }
        )
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE55B57))
            .padding(0.dp, 10.dp)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 18.dp, end = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (isTtsInitialized) {
                    if (tts.isSpeaking && isSpeakingInstructions) {
                        tts.stop()
                        isSpeakingInstructions = false
                    } else {
                        tts.speak(instructionText, TextToSpeech.QUEUE_FLUSH, null, null)
                        isSpeakingInstructions = true
                    }
                }
            }) {
                Image(
                    painter = painterResource(id = R.drawable.tts),
                    contentDescription = "Escuchar instrucciones",
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(80.dp)
            )
        }


        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(horizontal = 22.dp, vertical = 22.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.menu),
                contentDescription = "Menu",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { navController.navigate("MenuBarScreen") },
            )

            GuideButton(
                onClick = {
                    navController.navigate("GuideScreen")
                }
            )
        }

        Image(
            painter = painterResource(id=appleImageRes),
            contentDescription = "Manzana",
            modifier = Modifier
                .align(Alignment.Center)
                .size(400.dp)
                .padding(bottom = 100.dp)
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp)
        ){
            FlowRow (
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ){
                FoodCounter(
                    label = "Verduras\ny Frutas",
                    currentCount = verdurasfrutasCount,
                    maxCount = verdurasfrutasMax,
                    color = verdurasfrutasColor,
                    checkImagesRes = R.drawable.check,
                    onClick = {
                        activeFoodEdit = FoodEditState(
                            label = "Verduras y Frutas",
                            currentCount = verdurasfrutasCount,
                            maxCount = verdurasfrutasMax,
                            color = verdurasfrutasColor,
                            key = SettingsManager.VERDURAS_KEY
                        )
                    }
                )
                FoodCounter(
                    label = "Alimentos de\norigen Animal",
                    currentCount = origenanimalCount,
                    maxCount = origenanimalMax,
                    color = origenanimalColor,
                    checkImagesRes = R.drawable.check_red,
                    onClick = {
                        activeFoodEdit = FoodEditState(
                            label = "Alimentos de Origen Animal",
                            currentCount = origenanimalCount,
                            maxCount = origenanimalMax,
                            color = origenanimalColor,
                            key = SettingsManager.ANIMAL_KEY
                        )
                    }
                )
                FoodCounter(
                    label = "Leguminosas",
                    currentCount = leguminosasCount,
                    maxCount = leguminosasMax,
                    color = leguminosasColor,
                    checkImagesRes = R.drawable.check_orange,
                    onClick = {
                        activeFoodEdit = FoodEditState(
                            label = "Leguminosas",
                            currentCount = leguminosasCount,
                            maxCount = leguminosasMax,
                            color = leguminosasColor,
                            key = SettingsManager.LEGUMINOSAS_KEY
                        )
                    }
                )
                FoodCounter(
                    label = "Cereales",
                    currentCount = cerealesCount,
                    maxCount = cerealesMax,
                    color = cerealesColor,
                    checkImagesRes = R.drawable.check_yellow,
                    onClick = {
                        activeFoodEdit = FoodEditState(
                            label = "Cereales",
                            currentCount = cerealesCount,
                            maxCount = cerealesMax,
                            color = cerealesColor,
                            key = SettingsManager.CEREALES_KEY
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row (
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ){
                FoodCounter(
                    label = "Vasos de agua",
                    currentCount = aguaCount,
                    maxCount = aguaMax,
                    color = aguaColor,
                    checkImagesRes = R.drawable.check_blue,
                    onClick = {
                        activeFoodEdit = FoodEditState(
                            label = "Agua",
                            currentCount = aguaCount,
                            maxCount = aguaMax,
                            color = aguaColor,
                            key = SettingsManager.AGUA_KEY
                        )
                    }
                )
            }
        }

        if (!tutorialShown) {
            TutorialOverlay(
                currentStep = currentTutorialStep,
                onNextStep = { currentTutorialStep++ },
                onSkip = {
                    coroutineScope.launch {
                        settingsManager.setTutorialShown(true)
                    }
                    currentTutorialStep = 1
                }
            )
        }
    }
}

@Composable
fun BlockingPopup(
    onDismiss: () -> Unit
){
    AlertDialog(
        onDismissRequest = {
        },
        title = {
            Text(
                text = "Tiempo de descanso"
            )
        },
        text = {
            Text(text = "Has pasado 15 minutos dentro de la aplicación. \n¡Es hora de un descanso!")
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE55B57))
            ) {
                Text("Salir")
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    )
}


@Composable
fun GuideButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(2.dp, Color.Gray, CircleShape)
            .clickable { onClick() }
    ) {
        Text(
            text = "?",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FoodCounter(
    label: String,
    currentCount: Int,
    maxCount: Int,
    color: Color,
    checkImagesRes: Int,
    modifier: Modifier = Modifier,
    onClick : () -> Unit
){
    val progress = if (maxCount>0){
        currentCount.toFloat()/maxCount.toFloat()
    }else{
        0f
    }

    val isComplete = currentCount >= maxCount

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp)
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier= Modifier
                .size(80.dp)
        ){
            CircularProgressIndicator(
                progress = 1f,
                modifier = Modifier.fillMaxSize(),
                color = Color.LightGray.copy(alpha = 0.5f),
                strokeWidth = 8.dp
            )

            CircularProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxSize(),
                color = color,
                strokeWidth = 8.dp
            )

            if (isComplete){
                Image(
                    painter = painterResource(id = checkImagesRes),
                    contentDescription = "Completado",
                    modifier= Modifier.size(48.dp)
                )
            }else {
                Text(
                    text = "$currentCount",
                    color = Color.Black,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(
            modifier = Modifier
                .height(8.dp)
        )

        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NutriPekesScreenPreview2() {
    NutripekesandroidTheme {
        StartdayScreen(navController = rememberNavController())
    }
}