package br.concy.demo.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.TitleCard
import br.concy.demo.ui.theme.DemoTheme
import br.concy.demo.viewmodel.HomeViewModel
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState
import com.google.android.horologist.compose.material.ResponsiveListHeader

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun HomePage(modifier: Modifier = Modifier) {

    val homeVM: HomeViewModel = viewModel()
    val homeUIState = homeVM.uiState.collectAsState()

    val listState = rememberResponsiveColumnState(
        contentPadding = ScalingLazyColumnDefaults.padding(
            first = ScalingLazyColumnDefaults.ItemType.Text,
            last = ScalingLazyColumnDefaults.ItemType.SingleButton,
        ),
    )

    var heartRate by remember { mutableDoubleStateOf(0.0) }

    LaunchedEffect(homeVM.latestHeartRate) {
        homeVM.latestHeartRate.collect {
            heartRate = it
        }
    }

    ScalingLazyColumn(
        modifier = Modifier.background(MaterialTheme.colors.background),
        columnState = listState
    ) {
        item {
            ResponsiveListHeader {
                Text(
                    text = "Data collector",
                    color = MaterialTheme.colors.onSurfaceVariant,
                    fontSize = 16.sp
                )
            }
        }

        item {
            val color = if (homeUIState.value == HomeUIState.Default)
                MaterialTheme.colors.onSurfaceVariant
                else MaterialTheme.colors.secondary

            TitleCard(
                onClick = {},
                title = {
                    val text = if (homeUIState.value == HomeUIState.Default) "Sensor inactivated" else "Sensor activated"
                    Text(text, color = color)
                },
                titleColor = MaterialTheme.colors.onSurfaceVariant
            ) {
                val text = if (homeUIState.value == HomeUIState.Default) "Active the sensor" else "$heartRate bps"

                Text(
                    modifier = modifier,
                    text = text,
                    textAlign = TextAlign.Center,
                    color = color,
                    fontSize = 12.sp
                )
            }
        }

        item {
            if (homeUIState.value == HomeUIState.Default) {
                Button(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = {
                        homeVM.startCollect()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start data collect"
                    )
                }
            } else {
                Button(
                    modifier = Modifier.padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary
                    ),
                    onClick = {
                        homeVM.stopCollect()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Stop data collect"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    DemoTheme {
        HomePage()
    }
}