package co.lozano.galacticship;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/*import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;*/
import com.appnext.ads.interstitial.Interstitial;
import com.appnext.banners.BannerAdRequest;
import com.appnext.banners.BannerSize;
import com.appnext.banners.BannerView;
import com.appnext.base.Appnext;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;


public class InforApp extends AppCompatActivity {
    /*private final String APP_ID = "app8d6d4c273b73487688";
    private final String INTERSTITIAL_ZONE_ID  ="vz7ea8371797a14b4988";
    private AdColonyInterstitial adColonyInterstitiall;
    private AdColonyAdOptions adOptions;*/
    private FloatingActionButton interstitialButton;
    private BannerView bannerin_appnext;
    private Interstitial interstitialin_appnext;
    int desbloquear1, desbloquear2;
    private View decorView;//Para quitar el estatus bar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_app);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemDta());
                }
            }
        });
        Appnext.init(this);
        // NOTE always use test ads during development and testing
        bannerin_appnext = (BannerView) findViewById(R.id.banerin);
        interstitialin_appnext = new Interstitial(this, "da374833-5714-400f-a5c4-311747d5b1f0");
        interstitialin_appnext.loadAd();
        interstitialin_appnext.setMute(true);
        interstitialin_appnext.setBackButtonCanClose(true);

        BannerView banerin = new BannerView(this);
        banerin.setPlacementId("da374833-5714-400f-a5c4-311747d5b1f0");
        bannerin_appnext.loadAd(new BannerAdRequest());
        banerin.setBannerSize(BannerSize.BANNER);
        StartAppSDK.setTestAdsEnabled(BuildConfig.DEBUG);
        StartAppSDK.init(this, "202504993", true);

        desbloquear1 = getIntent().getIntExtra("desbloquearNivel1",0);
        desbloquear2 = getIntent().getIntExtra("desbloquearNivel2",0);
        /*AdColony.configure(InforApp.this, APP_ID, INTERSTITIAL_ZONE_ID);
        AdColonyInterstitialListener listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {
                interstitialButton =findViewById(R.id.btnIcoAtras);
                interstitialButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adColonyInterstitiall = adColonyInterstitial;
                        adColonyInterstitiall.show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
        };
        AdColony.requestInterstitial(INTERSTITIAL_ZONE_ID, listener, adOptions);*/

        interstitialButton =findViewById(R.id.btnIcoAtras);
        interstitialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //adColonyInterstitiall = adColonyInterstitial;
                //adColonyInterstitiall.show();
                Intent io = new Intent(getApplicationContext(), OpcionesActivity.class);
                io.putExtra("desbloquearNivel1", desbloquear1);
                io.putExtra("desbloquearNivel2", desbloquear2);
                startActivity(io);
                interstitialin_appnext.showAd();
                StartAppAd.showAd(InforApp.this);
                finish();
            }
        });
    }
    //Desaparecer el statusbar
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            decorView.setSystemUiVisibility(hideSystemDta());
        }
    }
    private int hideSystemDta(){
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }
    //////////////////////////////////////////////////////////////////////
    /*se controla la pulsacion del boton atras*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            Intent mainac = new Intent(getApplicationContext(), OpcionesActivity.class);
            mainac.putExtra("desbloquearNivel1", desbloquear1);
            mainac.putExtra("desbloquearNivel2", desbloquear2);
            startActivity(mainac);
        }
        return super.onKeyDown(keyCode, event);
    }
}