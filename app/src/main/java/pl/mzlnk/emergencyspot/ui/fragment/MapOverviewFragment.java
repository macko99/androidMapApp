package pl.mzlnk.emergencyspot.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import pl.mzlnk.emergencyspot.R;
import pl.mzlnk.emergencyspot.model.hospital.HospitalDto;
import pl.mzlnk.emergencyspot.model.hospital.HospitalParamsDto;
import pl.mzlnk.emergencyspot.service.network.requests.RetrieveHospitalsHttpRequestParams;
import pl.mzlnk.emergencyspot.ui.dialog.HospitalPreviewDialog;
import pl.mzlnk.emergencyspot.ui.dialog.HospitalsFilterDialog;

import static pl.mzlnk.emergencyspot.EmergencySpotApplication.app;


public class MapOverviewFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private ImageView filter;
    private GoogleMap map;

    private Map<LatLng, HospitalDto> hospitals = new HashMap<>();

    public MapOverviewFragment() {

    }

    @Override
    protected int getInflatedLayoutResId() {
        return R.layout.f_map_overview;
    }

    @Override
    protected void loadViewsFromXml(View rootView) {
        this.filter = rootView.findViewById(R.id.f_map_overview_img_filter);
    }

    @Override
    protected void prepareViews() {
        if (map == null) {
            SupportMapFragment mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(this);

            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.f_map_overview_map, mapFragment)
                    .commit();

        }
    }

    @Override
    protected void prepareListeners() {
        this.filter.setOnClickListener(view -> {
            HospitalsFilterDialog dialog = new HospitalsFilterDialog();
            dialog.setOnSubmitListener(this::onFilterApply);

            dialog.show(getFragmentManager(), "dialog");

        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        this.map.setOnMarkerClickListener(this);

        CameraUpdate initialPoint = CameraUpdateFactory.newLatLngZoom(
                new LatLng(52.360972D, 19.166285D),
                5F
        );

        app.networkService.makeRequestForList(
                new RetrieveHospitalsHttpRequestParams(new HospitalParamsDto()),
                list -> {
                    this.map.clear();
                    this.hospitals.clear();
                    list.forEach(hospital -> {
                        LatLng position = new LatLng(hospital.getLatitude(), hospital.getLongitude());

                        MarkerOptions marker = new MarkerOptions()
                                .position(position)
                                .title(hospital.getName());

                        this.map.addMarker(marker);
                        this.hospitals.put(position, hospital);
                    });
                },
                error -> Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        this.map.moveCamera(initialPoint);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (!this.hospitals.containsKey(marker.getPosition())) {
            return false;
        }

        HospitalDto hospital = this.hospitals.get(marker.getPosition());

        HospitalPreviewDialog dialog = HospitalPreviewDialog.newInstance(hospital);
        dialog.show(getFragmentManager(), "dialog");

        return false;
    }

    private void onFilterApply(HospitalParamsDto params) {
        if (this.map == null) {
            return;
        }

        app.networkService.makeRequestForList(
                new RetrieveHospitalsHttpRequestParams(params),
                list -> {
                    this.map.clear();
                    list.forEach(hospital -> {
                        MarkerOptions marker = new MarkerOptions()
                                .position(new LatLng(hospital.getLatitude(), hospital.getLongitude()))
                                .title(hospital.getName());

                        this.map.addMarker(marker);
                    });
                },
                error -> Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }


}
