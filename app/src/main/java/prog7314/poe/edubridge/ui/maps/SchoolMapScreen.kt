package prog7314.poe.edubridge.ui.screens.maps

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

private const val TAG = "SchoolMap"

/**
 * School location map — User Defined Feature 3.
 * @author Member 4
 */
@Composable
fun SchoolMapScreen(onBack: () -> Unit) {

    // EduBridge High School — Johannesburg
    val schoolLocation = remember { LatLng(-26.2041, 28.0473) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(schoolLocation, 14f)
    }

    LaunchedEffect(Unit) {
        Log.d(TAG, "SchoolMapScreen composed")
    }

    Column(Modifier.fillMaxSize()) {

        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) { Text("← Back") }
            Text("School Location", style = MaterialTheme.typography.titleLarge)
        }

        Box(Modifier.fillMaxSize()) {
s
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = {
                    Log.d(TAG, "Map loaded successfully")
                },
                properties = MapProperties(
                    isBuildingEnabled = true,
                    isMyLocationEnabled = false
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = false
                )
            ) {
                Marker(
                    state = MarkerState(position = schoolLocation),
                    title = "EduBridge High School",
                    snippet = "123 School Street, Johannesburg"
                )
            }

            // Fallback text so you know the screen rendered
            Text(
                "If map is blank, check Logcat tag 'SchoolMap'",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}