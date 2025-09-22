package com.example.nutripekes_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class Startday : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutriPekesScreen()
        }
    }

    @Composable
    fun NutriPekesScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE55B57))
        ) {
            Image(
                painter=painterResource(id=R.drawable.logo),
                contentDescription="Logo",
                modifier=Modifier
                    .align(Alignment.TopEnd)
                    .padding(18.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.menu),
                contentDescription = "Menu",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(22.dp)
                    .size(20.dp)
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
}
