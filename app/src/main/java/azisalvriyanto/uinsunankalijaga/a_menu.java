package azisalvriyanto.uinsunankalijaga;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class a_menu extends AppCompatActivity {

    private TextView mTextMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_menu);

        setTitle("Absensi");
        initFragment(new a_menu_fabsensi());

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
                    initFragment(new a_menu_fabsensi());
                    return true;
                case R.id.l_b_riwayat:
                    setTitle("Riwayat");
                    initFragment(new a_menu_friwayat());
                    return true;
                case R.id.l_b_izin:
                    setTitle("Izin");
                    initFragment(new a_menu_fizin());
                    return true;
                default:
                    setTitle("Absensi");
                    initFragment(new a_menu_fabsensi());
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
