package com.example.nutripekes_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripekes_android.ui.theme.NutripekesandroidTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel


class Startday : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StartdayScreen(navController = rememberNavController())
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
            Text(text = "Has pasado 15 minutos dentro de la aplicación. \n + ¡Es hora de un descanso!")
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
fun StartdayScreen(
    navController : NavController,
    viewModel : StartDayViewModel = viewModel()
) {
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
        Image(
            painter=painterResource(id=R.drawable.logo),
            contentDescription="Logo",
            modifier=Modifier
                .align(Alignment.TopEnd)
                .padding(18.dp)
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(22.dp, 22.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.menu),
                contentDescription = "Menu",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { navController.navigate("MenuBarScreen")},
            )

            GuideButton(
                onClick = {
                    navController.navigate("GuideScreen")
                }
            )
        }



        Image(
            painter = painterResource(id=R.drawable.empty_apple),
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
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ){
                FoodCounter(imageRes=R.drawable.verduras_empty, label="Verduras y frutas")
                FoodCounter(imageRes=R.drawable.animal_empty, label="Origen animal")
                FoodCounter(imageRes=R.drawable.leguminosas_empty, label="Leguminosas")
                FoodCounter(imageRes=R.drawable.cereales_empty, label="Cereales")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row (
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ){
                FoodCounter(imageRes = R.drawable.agua_empty, label = "Vasos de agua")
            }
        }
    }
}

@Composable
fun FoodCounter(imageRes: Int, label: String){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(id=imageRes),
            contentDescription = label,
            modifier = Modifier
                .size(80.dp)
        )
        Spacer(
            modifier = Modifier
                .height(8.dp))
        Text(
            text=label,
            color = Color.White,
            fontSize = 12.sp
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