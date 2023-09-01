package co.lozano.galacticship;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.appnext.ads.interstitial.Interstitial;
import com.appnext.banners.BannerAdRequest;
import com.appnext.banners.BannerSize;
import com.appnext.banners.BannerView;
import com.appnext.base.Appnext;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

public class AyudaActivity extends AppCompatActivity {
    //private BannerView bannera_appnext;
    Interstitial inters_appnext;
    int desbloquear1, desbloquear2, ayuda1, puntaje, boolpuntaje=1;
    private View decorView;//Para quitar el estatus bar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemDta());
                }
            }
        });
        //Appnext.init(this);
        // NOTE always use test ads during development and testing
        StartAppSDK.setTestAdsEnabled(BuildConfig.DEBUG);
        StartAppSDK.init(this, "202504993", true);
        ayuda1= getIntent().getIntExtra("Ayuda1",0);
        puntaje= getIntent().getIntExtra("score",0);
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
        if (ayuda1 == 1){
            Intent inte= new Intent(getApplicationContext(), StoneActivity.class);
            inte.putExtra("desbloquearNivel1", desbloquear1);
            inte.putExtra("desbloquearNivel2", desbloquear2);
            inte.putExtra("score", puntaje);
            inte.putExtra("boopuntaje", boolpuntaje);
            startActivity(inte);
            /*inters_appnext.showAd();
            StartAppAd.showAd(AyudaActivity.this);*/
        }else {
            Intent ja= new Intent(getApplicationContext(), JuegoActivity.class);
            ja.putExtra("desbloquearNivel1", desbloquear1);
            ja.putExtra("desbloquearNivel2", desbloquear2);
            ja.putExtra("score", puntaje);
            ja.putExtra("boopuntaje", boolpuntaje);
            startActivity(ja);
            /*inters_appnext.showAd();
            StartAppAd.showAd(AyudaActivity.this);*/
        }
    }
}