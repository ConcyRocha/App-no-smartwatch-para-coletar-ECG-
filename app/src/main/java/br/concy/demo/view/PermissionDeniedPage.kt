package br.concy.demo.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun PermissionDeniedPage(modifier: Modifier = Modifier) {

    val listState = rememberResponsiveColumnState(
        contentPadding = ScalingLazyColumnDefaults.padding(
            first = ScalingLazyColumnDefaults.ItemType.Text,
            last = ScalingLazyColumnDefaults.ItemType.Text,
        ),
    )

    ScalingLazyColumn(
        modifier = modifier.fillMaxSize(),
        columnState = listState
    ) {
        item {
            Text(
                text = "Permission denied",
                fontSize = 24.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
        item {
            Text(
                text = "Application needs to access the body sensors and your health data to work",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
