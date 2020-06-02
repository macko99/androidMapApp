package pl.mzlnk.emergencyspot.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import pl.mzlnk.emergencyspot.R;
import pl.mzlnk.emergencyspot.model.hospital.HospitalDetailsDto;
import pl.mzlnk.emergencyspot.service.network.requests.HttpRequestParams;
import pl.mzlnk.emergencyspot.service.network.requests.RetrieveHospitalDetailsHttpRequestParams;
import pl.mzlnk.emergencyspot.ui.view.button.NoIconButton;


public class HospitalDetailsFragment extends BaseNetworkDataReceiverFragment<HospitalDetailsDto> {

    private static final String HOSPITAL_ID_PARAM = "f.hospital.details.hospital.id";

    private TextView hospitalName, hospitalDescription, hospitalAddress;
    private RatingBar averageRating;
    private NoIconButton wardsList;

    private Long hospitalId;

    public static HospitalDetailsFragment newInstance(Long hospitalId) {
        HospitalDetailsFragment fragment = new HospitalDetailsFragment();

        Bundle args = new Bundle();
        args.putLong(HOSPITAL_ID_PARAM, hospitalId);
        fragment.setArguments(args);

        return fragment;
    }

    public HospitalDetailsFragment() {
        super();
    }


    @Override
    protected int getInflatedLayoutResId() {
        return R.layout.f_hospital_details;
    }

    @Override
    protected void readArguments(Bundle arguments) {
        this.hospitalId = arguments.getLong(HOSPITAL_ID_PARAM);
    }

    @Override
    protected void loadViewsFromXml(View rootView) {
        this.hospitalName = rootView.findViewById(R.id.f_hospital_details_text_hospital_name);
        this.hospitalDescription = rootView.findViewById(R.id.f_hospital_details_text_hospital_description);
        this.hospitalAddress = rootView.findViewById(R.id.f_hospital_details_text_hospital_address);
        this.averageRating = rootView.findViewById(R.id.f_hospital_details_bar_rating);
        this.wardsList = rootView.findViewById(R.id.f_hospital_details_btn_wards_list);
    }

    @Override
    protected void prepareListeners() {
        this.wardsList.setOnClickListener(view -> {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.a_main_fragment_container, HospitalWardsOverviewFragment.newInstance(this.hospitalId))
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public HttpRequestParams<HospitalDetailsDto> httpRequestParams() {
        return new RetrieveHospitalDetailsHttpRequestParams(this.hospitalId);
    }

    @Override
    public void onDataReceivedFromNetwork(HospitalDetailsDto data) {
        this.hospitalName.setText(data.getName());
        this.hospitalDescription.setText(data.getDescription());
        this.hospitalAddress.setText(
                String.format(
                        "%s %d,\n%s, %s",
                        data.getAddress().getStreet(),
                        data.getAddress().getStreetNumber(),
                        data.getAddress().getCity(),
                        data.getAddress().getCountry()
                ));

        this.averageRating.setRating(data.getAverageRating().floatValue());
    }
}
