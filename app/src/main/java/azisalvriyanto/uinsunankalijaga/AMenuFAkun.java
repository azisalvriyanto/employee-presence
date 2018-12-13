package azisalvriyanto.uinsunankalijaga;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AMenuFAkun extends Fragment {
    public AMenuFAkun() {
        // Required empty public constructor
        //return Fragment();
    }

//    public static AbsensiFragment newInstance(String param1, String param2) {
//        StoreFragment fragment = new StoreFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.l_menu_fakun, null);
    }
}