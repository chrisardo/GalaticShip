package co.lozano.galacticship;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.appnext.ads.interstitial.Interstitial;
import com.appnext.banners.BannerAdRequest;
import com.appnext.banners.BannerSize;
import com.appnext.banners.BannerView;
import com.appnext.base.Appnext;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

public class TipoJuego extends AppCompatActivity {
    LinearLayout lStone, lUniverso, lDark;
    ImageView imDesbloUnive, imDesblodark;
    int desbloquear1, desbloquear2;
    private BannerView bannertipo_appnext;
    //Interstitial interstitialtipo_appnext;
    Cursor cursor;
    ResultadosVo resultados;
    SharedPreferences preferencia;
    private View decorView;//Para quitar el estatus bar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_juego);
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
        bannertipo_appnext = (BannerView) findViewById(R.id.banerop);
        /*interstitialtipo_appnext = new Interstitial(this, "da374833-5714-400f-a5c4-311747d5b1f0");
        interstitialtipo_appnext.loadAd();
        interstitialtipo_appnext.setMute(true);
        interstitialtipo_appnext.setBackButtonCanClose(true);*/

        BannerView banertipo = new BannerView(this);
        banertipo.setPlacementId("da374833-5714-400f-a5c4-311747d5b1f0");
        bannertipo_appnext.loadAd(new BannerAdRequest());
        banertipo.setBannerSize(BannerSize.BANNER);
        imDesbloUnive = (ImageView)findViewById(R.id.imDesbloUniverse);
        imDesblodark = (ImageView)findViewById(R.id.imDesbloDark);
        lStone =(LinearLayout) findViewById(R.id.Lstone);
        lUniverso =(LinearLayout) findViewById(R.id.LUniverse);
        lDark =(LinearLayout) findViewById(R.id.LDark);
        desbloquear1 = getIntent().getIntExtra("desbloquearNivel1",0);
        desbloquear2 = getIntent().getIntExtra("desbloquearNivel2",0);
        lStone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), StoneActivity.class);
                i.putExtra("desbloquearNivel2", desbloquear2);
                startActivity(i);
                finish();
            }
        });
        if (desbloquear1 == 1){
            imDesbloUnive.setVisibility(View.INVISIBLE);
            lUniverso.setClickable(false);
            lUniverso.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent iu = new Intent(getApplicationContext(), UniverseoActivity.class);
                    iu.putExtra("desbloquearNivel1", desbloquear1);
                    startActivity(iu);
                }
            });
        }else{
            imDesbloUnive.setVisibility(View.VISIBLE);
            lUniverso.setClickable(false);
            lUniverso.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Juega Stone para desblquear: Universe", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (desbloquear2 == 2){
            imDesblodark.setVisibility(View.INVISIBLE);
            lDark.setClickable(false);
            lDark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ldark = new Intent(getApplicationContext(), DarkBlack.class);
                    ldark.putExtra("desbloquearNivel1", desbloquear1);
                    ldark.putExtra("desbloquearNivel2", desbloquear2);
                    startActivity(ldark);
                }
            });
        }else{
            imDesblodark.setVisibility(View.VISIBLE);
            lDark.setClickable(false);
            lDark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Juega Universe para desbloquear: Dark Black", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
    public void btnregresar(View view){
        Intent main = new Intent(getApplicationContext(), MainActivity.class);
        main.putExtra("desbloquearNivel1", desbloquear1);
        main.putExtra("desbloquearNivel2", desbloquear2);
        startActivity(main);
        //interstitialtipo_appnext.showAd();
        StartAppAd.showAd(TipoJuego.this);
    }
    /*se controla la pulsacion del boton atras*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            Intent mainac = new Intent(getApplicationContext(), MainActivity.class);
            mainac.putExtra("desbloquearNivel1", desbloquear1);
            mainac.putExtra("desbloquearNivel2", desbloquear2);
            startActivity(mainac);
        }
        return super.onKeyDown(keyCode, event);
    }
}