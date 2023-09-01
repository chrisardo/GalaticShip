package co.lozano.galacticship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.appnext.ads.interstitial.Interstitial;
import com.appnext.base.Appnext;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

public class Ayuda3Activity extends AppCompatActivity {
    //private BannerView bannera_appnext;
    Interstitial inters_appnext;
    int desbloquear1, desbloquear2, ayuda3;
    private View decorView;//Para quitar el estatus bar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda3);
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
        StartAppSDK.setTestAdsEnabled(BuildConfig.DEBUG);
        StartAppSDK.init(this, "202504993", true);
        ayuda3= getIntent().getIntExtra("Ayuda3",0);
        desbloquear1 = getIntent().getIntExtra("desbloquearNivel1",0);
        desbloquear2 = getIntent().getIntExtra("desbloquearNivel2",0);
        /*bannera_appnext = (BannerView) findViewById(R.id.banera);
        BannerView baner = new BannerView(this);
        baner.setPlacementId("da374833-5714-400f-a5c4-311747d5b1f0");
        bannera_appnext.loadAd(new BannerAdRequest());
        baner.setBannerSize(BannerSize.BANNER);
        inters_appnext = new Interstitial(this, "da374833-5714-400f-a5c4-311747d5b1f0");
        inters_appnext.loadAd();
        inters_appnext.setMute(true);
        inters_appnext.setBackButtonCanClose(true);*/
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
    public void volver(View view){
        if (ayuda3 == 3) {
            Intent inte= new Intent(getApplicationContext(), DarkBlack.class);
            inte.putExtra("desbloquearNivel1", desbloquear1);
            inte.putExtra("desbloquearNivel2", desbloquear2);
            startActivity(inte);
        }else {

            Intent inte= new Intent(getApplicationContext(), Nivel3Activity.class);
            inte.putExtra("desbloquearNivel1", desbloquear1);
            inte.putExtra("desbloquearNivel2", desbloquear2);
            startActivity(inte);
        }
    }
}