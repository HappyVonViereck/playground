package com.happy.vonviereck

import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun victoryScreen(
    onNextButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fontL = FontFamily(
        Font(R.font.lustigfont)
    )
    Box(modifier = Modifier.fillMaxSize()){

        Image(
            painter = painterResource(id = R.drawable.freiemice),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box() {
            Image(
                painter = painterResource(id = R.drawable.bildvictory),
                contentDescription = "Victory"
            )
        }
        Text(text = "Die Maus hat den Käse gefunden yay",  textAlign = TextAlign.Center, fontFamily = fontL, fontSize = 30.sp, color = btnStyle.fontColor)
        Button(modifier=Modifier.padding(top=200.dp),
            onClick = { onNextButtonClicked() },
            colors = ButtonDefaults.buttonColors(containerColor = btnStyle.bgColor)
        ) {
            Text(text = "Nochmal spielen",color = btnStyle.fontColor)
        }

        val openDialog = remember { mutableStateOf(false) }
        Button(
            onClick = { openDialog.value = true  },
            colors = ButtonDefaults.buttonColors(containerColor = btnStyle.bgColor))
            { Text("Sehe Statistiken zum letzen Labyrinth",color = btnStyle.fontColor) }
        if (openDialog.value) {
            BasicAlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onDismissRequest.
                    openDialog.value = false
                }
            ) {
                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = AlertDialogDefaults.TonalElevation,
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Dieses Feature ist noch nicht fertig entwickelt, bitte kaufen Sie die Vollversion dafür",color = btnStyle.fontColor)


                        //Daten die man anzeigen könnte
//                            Text(   text = """
//                                     🏁 Statistiken zum letzten Labyrinth:
//                                     🧱 Mauern: ${vm.anzahlMauern}
//                                     🟩  Boden: ${vm.anzahlFreieTiles}
//                                     🐭 Schritte der Maus: ${vm.schritteDerMaus}
//                                           """.trimIndent()
//                            )
                        Spacer(modifier = Modifier.height(24.dp))
                        TextButton(
                            onClick = { openDialog.value = false },
                            modifier = Modifier.align(Alignment.End),
                        ) {
                            Text("Danke!", fontFamily = fontL,color = btnStyle.fontColor)
                        }
                    }
                }
            }
        }
    }
}
