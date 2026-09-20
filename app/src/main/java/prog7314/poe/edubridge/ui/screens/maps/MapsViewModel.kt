package prog7314.poe.edubridge.ui.screens.maps

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import prog7314.poe.edubridge.ui.model.SchoolLocation
import prog7314.poe.edubridge.ui.sample.SampleData

class MapsViewModel : ViewModel() {

    private val _location = MutableStateFlow(SampleData.schoolLocation)
    val location: StateFlow<SchoolLocation> = _location.asStateFlow()

    fun geoUri(): String {
        val loc = _location.value
        val label = Uri.encode(loc.name)
        return "geo:${loc.latitude},${loc.longitude}?q=${loc.latitude},${loc.longitude}($label)"
    }
}
