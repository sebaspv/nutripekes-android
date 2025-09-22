import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutripekes_android.R
import com.example.nutripekes_android.ui.theme.PinkPeke
import com.example.nutripekes_android.ui.theme.YellowPeke

@Composable
fun ExercisePopup() {
    val juaFontFamily = FontFamily(Font(R.font.jua_regular))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PinkPeke)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(YellowPeke)
                .padding(horizontal = 30.dp, vertical = 40.dp)
        ) {
            Text(
                text = "Un ratito de\nmovimiento hace\nque el juego sea\naún más divertido\ndespués",
                fontSize = 24.sp,
                color = Color.Black,
                fontFamily = juaFontFamily,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PopupPreview() {
    ExercisePopup()
}