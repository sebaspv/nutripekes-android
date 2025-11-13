package com.example.nutripekes_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import android.speech.tts.TextToSpeech
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import java.util.Locale
import androidx.compose.foundation.layout.FlowRow

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

@Composable
fun StartdayScreen(
    navController : NavController
) {
    val instructionText = "Bienvenido a Nutripekes. Para empezar registra las porciones de alimentos que has consumido el día de hoy, por ejemplo, si has comido dos porciones de frutas, presiona el icono de frutas dos veces"
    val context = LocalContext.current
    val tts = remember(context) {
        lateinit var ttsInstance: TextToSpeech
        val listener = TextToSpeech.OnInitListener{status ->
            if (status == TextToSpeech.SUCCESS){
                ttsInstance.language= Locale("es", "ES")
            }
        }
        ttsInstance= TextToSpeech(context, listener)
        ttsInstance
    }

    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    var verdurasfrutasCount by remember { mutableStateOf(0) }
    var origenanimalCount by remember { mutableStateOf(0) }
    var leguminosasCount by remember { mutableStateOf(0) }
    var cerealesCount by remember { mutableStateOf(0) }
    var aguaCount by remember { mutableStateOf(0) }

    val verdurasfrutasMax = 5
    val verdurasfrutasColor = Color(0xFF1BCC21)
    val origenanimalMax = 3
    val origenanimalColor = Color(0xFFDE1C0E)
    val leguminosasMax = 2
    val leguminosasColor = Color(0xFFFA6806)
    val cerealesMax = 6
    val cerealessColor = Color(0xFFFFC107)
    val aguaMax = 6
    val aguaColor = Color(0xFF1BA6CC)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE55B57))
    ) {

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top=18.dp, end= 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(onClick = {
                tts.speak(instructionText, TextToSpeech.QUEUE_FLUSH, null, null)
            }){
                Image(
                    painter=painterResource(id=R.drawable.bocina),
                    contentDescription = "Escuchar instrucciones",
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter=painterResource(id=R.drawable.logo),
                contentDescription="Logo",
                modifier=Modifier.size(80.dp)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.menu),
            contentDescription = "Menu",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top=45.dp, start = 18.dp)
                .size(20.dp)
                .clickable { navController.navigate("MenuBarScreen")},
        )

        Image(
            painter = painterResource(id=R.drawable.empty_apple),
            contentDescription = "Manzana",
            modifier = Modifier
                .align(Alignment.Center)
                .size(400.dp)
                .padding(bottom = 80.dp)
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
                        if (verdurasfrutasCount < verdurasfrutasMax) verdurasfrutasCount++
                    }
                )
                FoodCounter(
                    label = "Alimentos de\norigen Animal",
                    currentCount = origenanimalCount,
                    maxCount = origenanimalMax,
                    color = origenanimalColor,
                    checkImagesRes = R.drawable.check_red,
                    onClick = {
                        if (origenanimalCount < origenanimalMax) origenanimalCount++
                    }
                )
                FoodCounter(
                    label = "Leguminosas",
                    currentCount = leguminosasCount,
                    maxCount = leguminosasMax,
                    color = leguminosasColor,
                    checkImagesRes = R.drawable.check_orange,
                    onClick = {
                        if (leguminosasCount < leguminosasMax) leguminosasCount++
                    }
                )
                FoodCounter(
                    label = "Cereales",
                    currentCount = cerealesCount,
                    maxCount = cerealesMax,
                    color = cerealessColor,
                    checkImagesRes = R.drawable.check_yellow,
                    onClick = {
                        if (cerealesCount < cerealesMax) cerealesCount++
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
                        if (aguaCount < aguaMax) aguaCount++
                    }
                )
            }
        }
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
            .clickable(onClick=onClick)
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
            fontSize = 12.sp
        )
    }
}