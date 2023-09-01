package co.lozano.galacticship;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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

public class GameOverActivity extends AppCompatActivity {
    TextView txnombreJugador, txtPunto, txtGaPer;
    String nombre_jugador, GanPerd;
    ImageView imAvatar;
    private String score;

    /*private final String APP_ID = "app8d6d4c273b73487688";
    private final String INTERSTITIAL_ZONE_ID  ="vz438477b5a7654d008d";
    private AdColonyInterstitial adColonyInterstitiall;
    private AdColonyAdOptions adOptions;*/
    private FloatingActionButton interstitialButton;
    private Interstitial interstitialgo_appnext;
    private BannerView banera_appnext;
    int desbloquear1, desbloquear2;

    private View decorView;//Para quitar el estatus bar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
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
        interstitialgo_appnext = new Interstitial(this, "da374833-5714-400f-a5c4-311747d5b1f0");
        interstitialgo_appnext.loadAd();
        interstitialgo_appnext.setMute(true);
        interstitialgo_appnext.setBackButtonCanClose(true);

        banera_appnext = (BannerView) findViewById(R.id.banergo);
        BannerView baner = new BannerView(this);
        baner.setPlacementId("da374833-5714-400f-a5c4-311747d5b1f0");
        banera_appnext.loadAd(new BannerAdRequest());
        baner.setBannerSize(BannerSize.BANNER);

        // NOTE always use test ads during development and testing
        StartAppSDK.setTestAdsEnabled(BuildConfig.DEBUG);
        StartAppSDK.init(this, "202504993", true);
        desbloquear1 = getIntent().getIntExtra("desbloquearNivel1",0);
        desbloquear2 = getIntent().getIntExtra("desbloquearNivel2",0);
        txnombreJugador=(TextView)findViewById(R.id.txNombreJugador);
        txtGaPer=(TextView)findViewById(R.id.txtGanoPer);
        txtPunto=(TextView)findViewById(R.id.txtPuntaje);
        imAvatar=(ImageView)findViewById(R.id.avaImage);
        nombre_jugador = getIntent().getStringExtra("jugador");
        GanPerd = getIntent().getStringExtra("ganoPerd");
        score = getIntent().getExtras().get("score").toString();
        txnombreJugador.setText(""+nombre_jugador);
        txtGaPer.setText(""+GanPerd);
        txtPunto.setText(""+score);
        imAvatar.setImageResource(Utilidades.listaAvatars.get(PreferenciasJuego.avatarId-1).getAvatarId());

        /*AdColony.configure(GameOverActivity.this, APP_ID, INTERSTITIAL_ZONE_ID);
        AdColonyInterstitialListener listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {
                interstitialButton =findViewById(R.id.irinicio);
                interstitialButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adColonyInterstitiall = adColonyInterstitial;
                        adColonyInterstitiall.show();

                        BaseDeDatos();
                        Intent irInici= new Intent(GameOverActivity.this, MainActivity.class);
                        startActivity(irInici);
                    }
                });
            }
        };
        AdColony.requestInterstitial(INTERSTITIAL_ZONE_ID, listener, adOptions);*/
        interstitialButton =(FloatingActionButton)findViewById(R.id.irinicio);
        interstitialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseDeDatos();
                Intent irInici= new Intent(GameOverActivity.this, MainActivity.class);
                irInici.putExtra("desbloquearNivel1", desbloquear1);
                irInici.putExtra("desbloquearNivel2", desbloquear2);
                startActivity(irInici);
                interstitialgo_appnext.showAd();
                StartAppAd.showAd(GameOverActivity.this);
            }
        });
    }
    public void BaseDeDatos(){
        int jugador=PreferenciasJuego.jugadorId;
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(getApplicationContext(),Utilidades.NOMBRE_BD,null,1);
        SQLiteDatabase db=conn.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_ID_JUGADOR,jugador);
        values.put(Utilidades.CAMPO_PUNTOS,score);
        //values.put(Utilidades.CAMPO_NIVEL,niveljuego);
        Long idResultante=db.insert(Utilidades.TABLA_PUNTAJE,Utilidades.CAMPO_ID_JUGADOR,values);
        if(idResultante!=-1){
            Intent irInici= new Intent(GameOverActivity.this, MainActivity.class);
            startActivity(irInici);
            //Toast.makeText(actividad,"Id Registro: "+idResultante,Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(),"El puntaje a sido Registrado con Exito! "+idResultante,Toast.LENGTH_LONG).show();
        }
        db.close();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogSalida);
            builder.setMessage("¿Quieres volver al menú principal?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            BaseDeDatos();
                            Intent Inici= new Intent(GameOverActivity.this, MainActivity.class);
                            Inici.putExtra("desbloquearNivel1", desbloquear1);
                            Inici.putExtra("desbloquearNivel2", desbloquear2);
                            startActivity(Inici);
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}