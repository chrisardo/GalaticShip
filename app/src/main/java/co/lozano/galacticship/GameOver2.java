package co.lozano.galacticship;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appnext.ads.interstitial.Interstitial;
import com.appnext.base.Appnext;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

public class GameOver2 extends AppCompatActivity {
    TextView txnombreJugador, txtPunto, txtGaPer;
    String nombre_jugador, GanPerd, jugador;
    ImageView imAvatar;
    private String score;
    /*private final String APP_ID = "app8d6d4c273b73487688";
    private final String INTERSTITIAL_ZONE_ID  ="vz438477b5a7654d008d";
    private AdColonyInterstitial adColonyInterstitiall;
    private AdColonyAdOptions adOptions;*/
    private FloatingActionButton interstitialButton;
    int desbloqueo;
    private Interstitial interstitialgo_appnext;
    //private BannerView banera_appnext;
    SharedPreferences preferencia;
    int desbloquear1, desbloquear2;
    private View decorView;//Para quitar el estatus bar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over2);
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
        /*banera_appnext = (BannerView) findViewById(R.id.banergo);
        BannerView baner = new BannerView(this);
        baner.setPlacementId("da374833-5714-400f-a5c4-311747d5b1f0");
        banera_appnext.loadAd(new BannerAdRequest());
        baner.setBannerSize(BannerSize.BANNER);*/
        // NOTE always use test ads during development and testing
        StartAppSDK.setTestAdsEnabled(BuildConfig.DEBUG);
        StartAppSDK.init(this, "202504993", true);
        txnombreJugador=(TextView)findViewById(R.id.txNombreJugador);
        txtGaPer=(TextView)findViewById(R.id.txtGanoPer);
        txtPunto=(TextView)findViewById(R.id.txtPuntaje);
        imAvatar=(ImageView)findViewById(R.id.avaImage);
        nombre_jugador = getIntent().getStringExtra("jugador");
        GanPerd = getIntent().getStringExtra("ganoPerd");
        score = getIntent().getExtras().get("score").toString();
        desbloquear1 = getIntent().getIntExtra("desbloquearNivel1",0);
        desbloquear2 = getIntent().getIntExtra("desbloquearNivel2",0);
        jugador=PreferenciasJuego.nickName;
        txnombreJugador.setText(""+jugador);
        txtGaPer.setText(""+GanPerd);
        txtPunto.setText(""+score);
        imAvatar.setImageResource(Utilidades.listaAvatars.get(PreferenciasJuego.avatarId-1).getAvatarId());

        /*preferencia = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putInt("desbloquearNivel1", desbloquear1);
        editor.putInt("desbloquearNivel2", desbloquear2);
        editor.commit();*/
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
                /*preferencia = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencia.edit();
                editor.putInt("desbloquearNivel1", desbloquear1);
                editor.putInt("desbloquearNivel2", desbloquear2);
                editor.commit();*/
                BaseDeDatos();
                Intent irtipo= new Intent(GameOver2.this, TipoJuego.class);
                irtipo.putExtra("desbloquearNivel1", desbloquear1);
                irtipo.putExtra("desbloquearNivel2", desbloquear2);
                startActivity(irtipo);
                interstitialgo_appnext.showAd();
                StartAppAd.showAd(GameOver2.this);
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
        //values.put(Utilidades.CAMPO_DESBLOQUEO, desbloqueo);
        //values.put(Utilidades.CAMPO_NIVEL,niveljuego);
        Long idResultante=db.insert(Utilidades.TABLA_PUNTAJE,Utilidades.CAMPO_ID_JUGADOR,values);
        if(idResultante!=-1){
            Intent irInici= new Intent(GameOver2.this, TipoJuego.class);
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
            builder.setMessage("¿Quieres volver a elegir mundo para jugar?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            BaseDeDatos();
                            Intent Inici= new Intent(GameOver2.this, TipoJuego.class);
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