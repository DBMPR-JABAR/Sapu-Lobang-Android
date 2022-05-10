package id.go.jabarprov.dbmpr.surveisapulubang.presentation.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.esri.arcgisruntime.data.QueryParameters
import com.esri.arcgisruntime.data.ServiceFeatureTable
import com.esri.arcgisruntime.geometry.Envelope
import com.esri.arcgisruntime.layers.FeatureLayer
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.BasemapStyle
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.mapping.view.LocationDisplay
import com.esri.arcgisruntime.ogc.wfs.WfsFeatureTable
import com.esri.arcgisruntime.symbology.SimpleLineSymbol
import com.esri.arcgisruntime.symbology.SimpleRenderer
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentMapBinding
import id.go.jabarprov.dbmpr.surveisapulubang.utils.LocationUtils
import kotlinx.coroutines.launch

private const val TAG = "MapFragment"

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding

    private val locationDisplay by lazy { binding.mapViewArcgis.locationDisplay }

    private val locationUtils by lazy { LocationUtils(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            initLocation()
        }
    }

    private fun initUI() {
        binding.apply {
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        setUpArcGISMap()
    }

    private fun setUpArcGISMap() {
        val map = ArcGISMap(BasemapStyle.ARCGIS_NAVIGATION)
        binding.mapViewArcgis.map = map
        binding.mapViewArcgis.setViewpoint(
            Viewpoint(-6.921359549350742, 107.61111502699526, 72000.0)
        )

        // Simple renderer for override style in feature layer
        val lineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.rgb(0, 0, 255), 2f)
        val simpleRenderer = SimpleRenderer(lineSymbol)

        val wfsFeatureTable = WfsFeatureTable(
            "https://geo.temanjabar.net/geoserver/wfs?service=wfs&version=2.0.0&request=GetCapabilities",
            "temanjabar:0_rj_prov",
        )

        wfsFeatureTable.featureRequestMode = ServiceFeatureTable.FeatureRequestMode.MANUAL_CACHE

        val featureLayer = FeatureLayer(wfsFeatureTable)
        featureLayer.renderer = simpleRenderer

        featureLayer.addDoneLoadingListener {
            Log.d(TAG, "setUpArcGISMap: ${featureLayer.loadStatus}")
            if (featureLayer.loadError != null) {
                Log.d(TAG, "setUpArcGISMap: ERROR ${featureLayer.loadError.cause}")
            }
        }

        binding.mapViewArcgis.map.operationalLayers.add(featureLayer)

        binding.mapViewArcgis.addNavigationChangedListener {
            if (!it.isNavigating) {
                popoulateFromServer(wfsFeatureTable, binding.mapViewArcgis.visibleArea.extent)
            }
        }
    }

    private fun initLocation() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                if (!locationUtils.isLocationEnabled()) {
                    locationUtils.enableLocationService()
                }
                locationDisplay.autoPanMode = LocationDisplay.AutoPanMode.RECENTER
                locationDisplay.startAsync()
            }
        }
    }

    private fun popoulateFromServer(wfsFeatureTable: WfsFeatureTable, extent: Envelope) {
        val visibleExtentQuery = QueryParameters()
        visibleExtentQuery.geometry = extent
        wfsFeatureTable.populateFromServiceAsync(visibleExtentQuery, false, null)
    }

    override fun onPause() {
        binding.mapViewArcgis.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapViewArcgis.resume()
    }

    override fun onDestroy() {
        binding.mapViewArcgis.dispose()
        super.onDestroy()
    }
}