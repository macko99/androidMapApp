package pl.mzlnk.emergencyspot.ui.viewprovider;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pl.mzlnk.emergencyspot.R;
import pl.mzlnk.emergencyspot.model.hospitalstay.HospitalStayDto;
import pl.mzlnk.emergencyspot.ui.fragment.HospitalStayDetailsFragment;
import pl.mzlnk.emergencyspot.ui.view.list.HospitalStayListItemView;

public class HospitalStaysOverviewViewProvider implements ViewProvider<HospitalStayDto, Long, HospitalStayListItemView> {

    @Override
    public List<Map.Entry<Long, HospitalStayListItemView>> provide(List<HospitalStayDto> data,
                                                                   Context context,
                                                                   @Nullable FragmentManager fragmentManager) {
        return data
                .stream()
                .map(stay -> {
                    HospitalStayListItemView view = new HospitalStayListItemView(context);
                    view.setDetails(stay);

                    view.setOnClickListener(v -> {
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.a_main_fragment_container, HospitalStayDetailsFragment.newInstance(stay.getId()))
                                .addToBackStack(null)
                                .commit();
                    });

                    return new AbstractMap.SimpleEntry<>(1L, view);
                })
                .collect(Collectors.toList());
    }

}
