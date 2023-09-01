package co.lozano.galacticship;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity2 extends AppCompatActivity {
    ImageView imCohete;
    int desbloquear1=1, desbloquear2=2;
    private View decorView;//Para quitar el estatus bar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemDta());
                }
            }
        });
        imCohete=(ImageView)findViewById(R.id.imCohete);
        //imCohe2=(ImageView)findViewById(R.id.imCohet2);
        TranslateAnimation animation = new TranslateAnimation(0.0f, 1300.0f, 0.0f, 0.0f);
        animation.setDuration(5000);
        animation.setRepeatCount(5);
        animation.setRepeatMode(2);
        animation.setFillAfter(true);
        imCohete.startAnimation(animation);
        imCohete.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor=preferences.edit();
                int bandera=Integer.parseInt(preferences.getString("bandera","0"));
                if (bandera==1){
                    Intent intent=new Intent(SplashActivity2.this, MainActivity.class);
                    intent.putExtra("desbloquearNivel1", desbloquear1);
                    intent.putExtra("desbloquearNivel2", desbloquear2);
                    startActivity(intent);
                }else{
                    editor.putString("bandera","1");
                    editor.commit();
                    /*Intent intent2=new Intent(SplashActivity.this, InicioActivity.class);
                    startActivity(intent2);*/
                    Intent intent=new Intent(SplashActivity2.this, ContenedorInstruccionesActivity.class);
                    startActivity(intent);
                }
                //setAnimation(imCohete, i);*/
                finish();
            }
        },4000);
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
}