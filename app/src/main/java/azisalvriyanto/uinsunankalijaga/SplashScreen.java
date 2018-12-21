package azisalvriyanto.uinsunankalijaga;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;


public class SplashScreen extends AppCompatActivity {
     private static int splashInterval = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, AMasuk.class);
                startActivity(intent);

                this.finish();
            }

            private void finish() {
            }
        }, splashInterval);
    }
}
