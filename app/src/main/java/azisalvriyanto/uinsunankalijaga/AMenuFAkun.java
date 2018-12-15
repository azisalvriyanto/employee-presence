package azisalvriyanto.uinsunankalijaga;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class AMenuFAkun extends Fragment {
    Button b_keluar;

    public AMenuFAkun() {
        // Required empty public constructor
        //return Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.l_menu_fakun, container, false);

        b_keluar = view.findViewById(R.id.l_fakun_b_keluar);
        b_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set LoggedIn status to false
                String pegawaiNIP = SaveSharedPreference.getNIP(getActivity().getApplicationContext());
                Toast.makeText(getActivity().getApplicationContext(), pegawaiNIP, Toast.LENGTH_SHORT).show();

                SaveSharedPreference.setLoggedIn(getActivity().getApplicationContext(), false, "Default");
                Intent intent = new Intent(getActivity().getApplicationContext(), AMasuk.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;
    }
}
