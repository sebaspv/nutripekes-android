package com.example.nutripekes_android

import SideBar
import android.os.Bundle
import android.view.WindowInsets
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nutripekes_android.ui.theme.NutripekesandroidTheme
import com.example.nutripekes_android.ui.theme.PinkPeke
import com.example.nutripekes_android.ui.theme.YellowPeke


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation()
        }
    }
}


@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "MainActivity") {
        composable("MainActivity") {
            NutriPekesScreen(navController = navController)
        }
        composable("StartDayScreen") {
            StartdayScreen(navController = navController)
        }
        composable("MenuBarScreen"){
            SideBar(navController = navController)
        }
        composable("ParentsInfoScreen") {
            ParentsInformation(navController = navController)
        }
        composable("InformationPageScreen"){
            InfoPage(navController = navController)
        }
        composable("GuideScreen"){
            GuideScreen(navController = navController)
        }
    }
}

@Composable
fun NutriPekesScreen(navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PinkPeke),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(5.dp))

        // titulo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(350.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        // boton de inicio
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(YellowPeke) // yellow button
                .padding(horizontal = 80.dp, vertical = 20.dp)
                .clickable { navController.navigate("StartDayScreen")},
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.full_apple),
                    contentDescription = "Apple",
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Iniciar",
                    fontSize = 75.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.jua_regular))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NutriPekesScreenPreview() {
    NutripekesandroidTheme {
        NutriPekesScreen(navController = rememberNavController())
    }
}