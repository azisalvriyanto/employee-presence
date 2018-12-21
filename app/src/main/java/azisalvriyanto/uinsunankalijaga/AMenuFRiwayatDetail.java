package azisalvriyanto.uinsunankalijaga;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class AMenuFRiwayatDetail extends AppCompatActivity {
    private static final String TAG = "AMenuFRiwayatDetail";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "asw | ");
        setContentView(R.layout.l_menu_friwayat_detail);

        getIncomingIntent();
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("friwayat_detail_waktu")){
            Log.d(TAG, "getIncomingIntent | ");

            String waktu = getIntent().getStringExtra("friwayat_detail_waktu");

            TextView tv_waktu = findViewById(R.id.l_friwayat_detail_nama);
            tv_waktu.setText(waktu);
        }
    }


    /*private void setImage(String imageUrl){
        ImageView image = findViewById(R.id.l_friwayat_detail_foto);
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(image);
    }*/
}
