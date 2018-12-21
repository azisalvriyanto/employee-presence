package azisalvriyanto.uinsunankalijaga;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AMenuFAbsensi extends Fragment {
    public AMenuFAbsensi() {
        // Required empty public constructor
        //return Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.l_menu_fabsensi, container, false);

        Button l_b_absen        = (Button) view.findViewById(R.id.l_friwayat_absensi_b_absen);
        Button l_b_izinsakit    = (Button) view.findViewById(R.id.l_friwayat_absensi_b_izinsakit);

        l_b_absen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFragment(new AMenuFAbsensiAbsen());
            }
        });

        l_b_izinsakit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFragment(new AMenuFAbsensiIzinSakit());
            }
        });

        return view;
    }

    private Fragment initFragment(Fragment classFragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.l_menu_flayout, classFragment);
        transaction.commit();
        return classFragment;
    }
}
