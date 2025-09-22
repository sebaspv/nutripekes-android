import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutripekes_android.R
import com.example.nutripekes_android.ui.theme.PinkPeke
import com.example.nutripekes_android.ui.theme.GreenPeke


@Composable
fun SideBar() {
    val juaFontFamily = FontFamily(Font(R.font.jua_regular))

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.4f)
                .background(GreenPeke)
                .padding(start = 20.dp, end = 20.dp, top = 60.dp, bottom = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Padres",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontFamily = juaFontFamily,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "expandir info padres",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            SideMenuItem(text = "Tabla de recomendaciones", fontFamily = juaFontFamily)
            SideMenuItem(text = "Recetario", fontFamily = juaFontFamily)

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Información",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontFamily = juaFontFamily,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "expandir info",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            SideMenuItem(text = "Componentes de una comida balanceada", fontFamily = juaFontFamily)
            SideMenuItem(text = "Señales de hambre y saciedad", fontFamily = juaFontFamily)
            SideMenuItem(text = "Manejo de la selectividad alimentaria", fontFamily = juaFontFamily)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.4f)
                .background(PinkPeke)
        ) {
        }
    }
}

@Composable
fun SideMenuItem(text: String, fontFamily: FontFamily) {
    Text(
        text = text,
        fontSize = 19.sp,
        color = Color.White,
        fontFamily = fontFamily,
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SideBarPreview() {
    SideBar()
}