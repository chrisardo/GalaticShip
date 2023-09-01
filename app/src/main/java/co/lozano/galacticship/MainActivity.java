package co.lozano.galacticship;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.appnext.ads.interstitial.Interstitial;
import com.appnext.base.Appnext;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import co.lozano.galacticship.interfaces.IComunicaFragments;

public class MainActivity extends AppCompatActivity implements IComunicaFragments, InicioFragment.OnFragmentInteractionListener,
        RegistroJugadorFragment.OnFragmentInteractionListener,GestionJugadorFragment.OnFragmentInteractionListener,
        RankingFragment.OnFragmentInteractionListener{
    Fragment fragmentInicio,registroJugadorFragment,gestionJugadorFragment, rankingFragment, conteregistrofragment ;
    AlertDialog.Builder dialog; //Mensaje emergente
    CardView cardRegistro, cardSeleccion;
    ImageView btnSalir;
    Interstitial interstitialtipo_appnext;
    //Toolbar toolbar;
    int desbloquear1, desbloquear2, desbloqueo1, desbloqueo2;
    //Contraseña de apk: ertdfgxc
    private View decorView;//Para quitar el estatus bar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        interstitialtipo_appnext = new Interstitial(this, "da374833-5714-400f-a5c4-311747d5b1f0");
        interstitialtipo_appnext.loadAd();
        interstitialtipo_appnext.setMute(true);
        interstitialtipo_appnext.setBackButtonCanClose(true);
        // NOTE always use test ads during development and testing
        StartAppSDK.setTestAdsEnabled(BuildConfig.DEBUG);
        StartAppSDK.init(this, "202504993", true);
        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar();*/
        desbloquear1 = getIntent().getIntExtra("desbloquearNivel1",0);
        desbloquear2 = getIntent().getIntExtra("desbloquearNivel2",0);
        /*desbloqueo1 = getIntent().getIntExtra("desbloqueoNivel1",0);
        desbloqueo2 = getIntent().getIntExtra("desbloqueoNivel2",0);*/
        SharedPreferences preferences= android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        PreferenciasJuego.obtenerPreferencias(preferences,getApplicationContext());
        Utilidades.obtenerListaAvatars();//llena la lista de avatars para ser utilizada
        Utilidades.consultarListaJugadores(this);
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,Utilidades.NOMBRE_BD,null,1);
        fragmentInicio =new InicioFragment();
        registroJugadorFragment=new RegistroJugadorFragment();
        gestionJugadorFragment=new GestionJugadorFragment();
        rankingFragment=new RankingFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragments, fragmentInicio).commit();
    }
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
    @Override
    public void mostrarMenu() {
        Utilidades.obtenerListaAvatars();//llena la lista de avatars para ser utilizada
        Utilidades.consultarListaJugadores(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragments,fragmentInicio).commit();
    }
    public void onClick(View view) {
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
    }
    @Override
    public void iniciarJuego() {
        if (Utilidades.listaJugadores!=null && Utilidades.listaJugadores.size()>0)
        {
            Intent irJuego= new Intent(this, JuegoActivity.class);
            irJuego.putExtra("jugador", PreferenciasJuego.nickName);
            irJuego.putExtra("genero", PreferenciasJuego.NICKNAME);
            irJuego.putExtra("img", PreferenciasJuego.avatarId);
            irJuego.putExtra("desbloquearNivel1", desbloquear1);
            irJuego.putExtra("desbloquearNivel2", desbloquear2);
            startActivity(irJuego);
            //crearDialogoGestionUsuario();
        }else{
            Toast.makeText(getApplicationContext(),"Debe registrar un Jugador",Toast.LENGTH_SHORT).show();
        }
    }
    /*@Override
    public void llamarAjustes() {
        //   Toast.makeText(getApplicationContext(),"Ajustes desde la actividad",Toast.LENGTH_SHORT).show();
        Intent inte= new Intent(this, AyudaActivity.class);
        startActivity(inte);
    }*/
    @Override
    public void consultarRanking() {
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragments,rankingFragment).commit();
        interstitialtipo_appnext.showAd();
    }
    @Override
    public void consultarInstrucciones() {
        Intent opc = new Intent(MainActivity.this, OpcionesActivity.class);
        opc.putExtra("desbloquearNivel1", desbloquear1);
        opc.putExtra("desbloquearNivel2", desbloquear2);
        startActivity(opc);
        StartAppAd.showAd(MainActivity.this);
    }
    @Override
    public void gestionarUsuario() {
        createSimpleDialog();
    }
    @Override
    public void consultarInformacion() {
        if (Utilidades.listaJugadores!=null && Utilidades.listaJugadores.size()>0)
        {
            Intent irtipoJuego= new Intent(this, TipoJuego.class);
            irtipoJuego.putExtra("jugador", PreferenciasJuego.nickName);
            irtipoJuego.putExtra("genero", PreferenciasJuego.NICKNAME);
            irtipoJuego.putExtra("img", PreferenciasJuego.avatarId);
            irtipoJuego.putExtra("desbloquearNivel1", desbloquear1);
            irtipoJuego.putExtra("desbloquearNivel2", desbloquear2);
            startActivity(irtipoJuego);
            interstitialtipo_appnext.showAd();
            //crearDialogoGestionUsuario();
        }else{
            Toast.makeText(getApplicationContext(),"Debe registrar un Jugador",Toast.LENGTH_SHORT).show();
        }
    }
    private void createSimpleDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.gestiojugador, null);
        builder.setView(v);
        btnSalir=v.findViewById(R.id.btnSalir);
        cardRegistro=v.findViewById(R.id.cardRegistro);
        cardSeleccion=v.findViewById(R.id.cardSeleccion);
        final  AlertDialog dialogGes=builder.create();
        cardRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragments,registroJugadorFragment).commit();
                dialogGes.cancel();
            }
        });
        cardSeleccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilidades.listaJugadores!=null && Utilidades.listaJugadores.size()>0)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragments,gestionJugadorFragment).commit();
                    dialogGes.cancel();
                }else{
                    Toast.makeText(getApplicationContext(),"Debe registrar un Jugador",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialogGes.show();
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGes.dismiss();
            }
        });
    }
    /*se controla la pulsacion del boton atras*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogSalida);
            builder.setMessage("¿Desea salir de la aplicación juego GalacticShip?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
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