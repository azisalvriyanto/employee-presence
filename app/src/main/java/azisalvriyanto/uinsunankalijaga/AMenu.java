package azisalvriyanto.uinsunankalijaga;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import azisalvriyanto.uinsunankalijaga.Adapter.AdapterRiwayat;
import azisalvriyanto.uinsunankalijaga.Model.ModelRiwayat;

public class AMenu extends AppCompatActivity {

    private TextView mTextMessage;
    //tambahan
    private ArrayList<ModelRiwayat> riwayatList;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private AdapterRiwayat adapterRiwayat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_menu);

        setTitle("Absensi");
        initFragment(new AMenuFAbsensi());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.l_b_absensi:
                    setTitle("Absensi");
                    initFragment(new AMenuFAbsensi());
                    return true;
                case R.id.l_b_riwayat:
                    setTitle("Riwayat");
                    initFragment(new AMenuFRiwayat());
                    return true;
                case R.id.l_b_akun:
                    setTitle("Absensi");
                    initFragment(new AMenuFAkun());
                    return true;
                default:
                    setTitle("Absensi");
                    initFragment(new AMenuFAbsensi());
                    return true;
            }
        }
    };

    private void initFragment(Fragment classFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.l_menu_flayout, classFragment);
        transaction.commit();
    }
}
