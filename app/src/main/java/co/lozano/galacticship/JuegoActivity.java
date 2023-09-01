package co.lozano.galacticship;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import java.util.Timer;
import java.util.TimerTask;

public class JuegoActivity extends AppCompatActivity {
    private  FlyingSpaceView gameView;
    private Handler handler = new Handler();
    Timer timer;
    private  final static long Interval = 30;
    Button btncontinuar, btnayuda, btnSalirJuego;
    MediaPlayer mp, mp_great, mp_bueno, mp_bad, mp_vidaextra;
    int desbloquear1, desbloquear2, lifeCounterOfFish, score;
    ImageView btnSalir, btncancelar;
    private View decorView;//Para quitar el estatus bar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemDta());
                }
            }
        });
        // NOTE always use test ads during development and testing
        StartAppSDK.setTestAdsEnabled(BuildConfig.DEBUG);
        StartAppSDK.init(this, "202504993", true);
        desbloquear1 = getIntent().getIntExtra("desbloquearNivel1",0);
        desbloquear2 = getIntent().getIntExtra("desbloquearNivel2",0);
        gameView = new FlyingSpaceView(this);
        setContentView(gameView);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        gameView.invalidate();
                    }
                });
            }
        }, 0, Interval);
        mp = MediaPlayer.create(this, R.raw.juego);
        mp_bad = MediaPlayer.create(this, R.raw.bad);
        if (!mp.isPlaying()){
            mp.start();
        }
    }
    class FlyingSpaceView extends View {
        private Bitmap fish[] = new Bitmap[2];
        private int fishX = 50;
        private int fishY;
        private int fishSpeed;
        public boolean tocuh = false;
        private int canvasWidth, canvasHeight;
        private int monedaX, monedaY, monedaSpeed = 20;
        private Paint monedaPaint = new Paint();
        private int botiquinX, botiquinY, botiquinSpeed = 15;
        private Paint botiquinPaint = new Paint();
        private int greenX, greenY, greenSpeed=25;
        private Paint greenPaint = new Paint();
        private int lunaX, lunaY, lunaSpeed=26;
        private Paint lunaPaint = new Paint();
        private int meteoritoX, meteoritoY, meteoritoSpeed=22;
        private Paint meteoritoPaint = new Paint();
        private int agujeroX, agujeroY, agujeroSpeed=10;
        private Paint agujeroPaint = new Paint();
        private Paint pausarPaint = new Paint();
        private Paint btnelevarPaint = new Paint();
        private Paint redPaint = new Paint();
        private Bitmap backgroundImage;
        private Paint scorePaint = new Paint();
        private Bitmap life[] = new Bitmap[2];
        Bitmap meteorito, diamante, moneda, luna, botiquin, agujero, pausar, btnelevar ;
        private Paint nombrePaint = new Paint();
        private Paint txtvidaPaint = new Paint();
        String jugador;
        int x, y, yp=5, xp= 1100;
        private Thread thread;
        int radio= 4;
        //private boolean isPlaying;
        public FlyingSpaceView(Context context) {
            super(context);
            fish[0] = BitmapFactory.decodeResource(getResources(), R.drawable.nave);
            fish[1] = BitmapFactory.decodeResource(getResources(), R.drawable.nav);
            backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.fondojuego);
            monedaPaint.setAntiAlias(false);
            greenPaint.setAntiAlias(false);
            botiquinPaint.setAntiAlias(false);
            lunaPaint.setAntiAlias(false);
            agujeroPaint.setAntiAlias(false);
            pausarPaint.setAntiAlias(false);
            redPaint.setAntiAlias(false);
            scorePaint.setColor(Color.WHITE);
            scorePaint.setTextSize(60);
            scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
            scorePaint.setAntiAlias(true);
            life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.hearts);
            life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_grey);
            fishY = 950;
            score=0;
            lifeCounterOfFish=3;
            meteorito = BitmapFactory.decodeResource(getResources(), R.drawable.meteorito);
            luna = BitmapFactory.decodeResource(getResources(), R.drawable.luna);
            agujero = BitmapFactory.decodeResource(getResources(), R.drawable.agujero);
            diamante = BitmapFactory.decodeResource(getResources(), R.drawable.diamante);
            moneda = BitmapFactory.decodeResource(getResources(), R.drawable.moneda);
            botiquin = BitmapFactory.decodeResource(getResources(), R.drawable.botiquin);
            pausar = BitmapFactory.decodeResource(getResources(), R.drawable.pausar);
            btnelevar = BitmapFactory.decodeResource(getResources(), R.drawable.btnelevar);
            nombrePaint.setColor(Color.WHITE);
            nombrePaint.setTextSize(60);
            nombrePaint.setTypeface(Typeface.DEFAULT_BOLD);
            txtvidaPaint.setAntiAlias(true);
            txtvidaPaint.setColor(Color.WHITE);
            txtvidaPaint.setTextSize(60);
            txtvidaPaint.setTypeface(Typeface.DEFAULT_BOLD);
            txtvidaPaint.setAntiAlias(true);
            mp_vidaextra = MediaPlayer.create(getContext(), R.raw.vidaextra);
        }
        @Override
        protected void onDraw (Canvas canvas){
            super.onDraw(canvas);
            canvasWidth = canvas.getWidth();
            canvasHeight = canvas.getHeight();
            canvas.drawBitmap(backgroundImage, 0,0, null);
            int minFishY = fish[0].getHeight();
            int maxFishY = canvasHeight - fish[0].getHeight() * 1;
            fishY = fishY + fishSpeed;
            if (fishY < minFishY){
                fishY = minFishY;
            }
            if (fishY > maxFishY){
                fishY = maxFishY;
            }
            fishSpeed = fishSpeed + 2;
            if (tocuh){
                canvas.drawBitmap(fish[1], fishX, fishY, null);
                tocuh = false;
            }else {
                canvas.drawBitmap(fish[0], fishX, fishY, null);
            }
            monedaX = monedaX - monedaSpeed;
            if (hitBallChecker(monedaX, monedaY)) {
                mp_great = MediaPlayer.create(getContext(), R.raw.wonderful);
                mp_great.start();
                if (mp_bueno != null){ mp_bueno.release(); }
                score = score + 10;
                monedaX = -100;
            }
            if (monedaX <0){
                monedaX = canvasWidth + 21;
                monedaY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
            }
            //canvas.drawCircle(yellowX, yellowY, 25, yellowPaint);
            canvas.drawBitmap(moneda, monedaX, monedaY, monedaPaint);
            greenX = greenX - greenSpeed;
            if (hitBallChecker(greenX, greenY)) {
                mp_bueno = MediaPlayer.create(getContext(), R.raw.bueno);
                mp_bueno.start();
                if (mp_great != null){ mp_great.release(); }
                score = score + 20;
                greenX = -100;
            }
            if (greenX <0){
                greenX = canvasWidth + 21;
                greenY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
            }
            //canvas.drawCircle(greenX, greenY, 25, greenPaint);
            canvas.drawBitmap(diamante, greenX, greenY, greenPaint);

            meteoritoX = meteoritoX - meteoritoSpeed;
            if (hitBallChecker(meteoritoX, meteoritoY)) {
                //score = score + 20;
                mp_bad.start();
                if (mp_bueno != null){ mp_bueno.release(); }
                if (mp_great != null){ mp_bueno.release(); }
                meteoritoX = -100;
                lifeCounterOfFish--;
                if (lifeCounterOfFish ==0){
                    mp.release();
                    //Toast.makeText(getContext(), "Perdiste el juego", Toast.LENGTH_LONG).show();
                    Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
                    gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    gameOverIntent.putExtra("score", score);
                    gameOverIntent.putExtra("jugador",jugador);
                    gameOverIntent.putExtra("ganoPerd", "Perdiste el juego!! :(");
                    gameOverIntent.putExtra("desbloquearNivel1", desbloquear1);
                    gameOverIntent.putExtra("desbloquearNivel2", desbloquear2);
                    getContext().startActivity(gameOverIntent);
                }
            }

            if (meteoritoX <0){
                meteoritoX = canvasWidth + 21;
                meteoritoY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
            }
            // canvas.drawCircle(redX, redY, 30, redPaint);
            jugador=PreferenciasJuego.nickName;
            canvas.drawBitmap(meteorito,meteoritoX, meteoritoY,meteoritoPaint);
            canvas.drawText("Jugador : " + jugador, 35, 70, nombrePaint);
            canvas.drawText("Puntaje : " + score, 35, 160, scorePaint);
            canvas.drawText("Vidas : ", 905, 150, txtvidaPaint);
            canvas.drawBitmap(pausar, xp, yp, pausarPaint);
            canvas.drawBitmap(btnelevar, 1130, 550, btnelevarPaint);

            for (int i=0; i<3; i++){
                x= (int)(1100 + life[0].getWidth() * 1.5*i);//500
                y=100;//30
                if (i<lifeCounterOfFish){
                    canvas.drawBitmap(life[0], x, y, null);
                }else{
                    canvas.drawBitmap(life[1], x, y, null);
                }
            }
            if (score >= 320){
                meteoritoSpeed = 25;
            }
            if (score>=420 && score<=460){
                botiquinX = botiquinX - botiquinSpeed;
                if (hitBallChecker(botiquinX, botiquinY)) {
                    mp_vidaextra.start();
                    lifeCounterOfFish++;
                    canvas.drawBitmap(life[0], x, y, null);
                    botiquinX = -100;
                }
                if (botiquinX <0){
                    botiquinX = canvasWidth + 21;
                    botiquinY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
                }
                canvas.drawBitmap(botiquin, botiquinX, botiquinY, botiquinPaint);
            }
            if (score >= 620){
                lunaX = lunaX - lunaSpeed;
                if (hitBallChecker(lunaX, lunaY)) {
                    //score = score + 20;
                    mp_bad.start();
                    lunaX = -100;
                    lifeCounterOfFish--;
                    if (lifeCounterOfFish ==0){
                        //Toast.makeText(getContext(), "Perdiste el juego", Toast.LENGTH_LONG).show();
                        mp.release();
                        Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
                        gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        gameOverIntent.putExtra("score", score);
                        gameOverIntent.putExtra("jugador",jugador);
                        gameOverIntent.putExtra("ganoPerd", "Perdiste el juego!! :(");
                        gameOverIntent.putExtra("desbloquearNivel1", desbloquear1);
                        gameOverIntent.putExtra("desbloquearNivel2", desbloquear2);
                        getContext().startActivity(gameOverIntent);
                    }
                }
                if (lunaX <0){
                    lunaX = canvasWidth + 21;
                    lunaY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
                }
                canvas.drawBitmap(luna,lunaX, lunaY,greenPaint);
            }
            if (score >= 920){
                lunaSpeed = 25;
            }
            if (score>=820 && score<=860){
                botiquinX = botiquinX - botiquinSpeed;
                if (hitBallChecker(botiquinX, botiquinY)) {
                    mp_vidaextra.start();
                    lifeCounterOfFish++;
                    canvas.drawBitmap(life[0], x, y, null);
                    botiquinX = -100;
                }
                if (botiquinX <0){
                    botiquinX = canvasWidth + 21;
                    botiquinY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
                }
                canvas.drawBitmap(botiquin, botiquinX, botiquinY, botiquinPaint);
            }
            if(score>=1200){
                Toast.makeText(getContext(), "Topate con unos de losagujero negros para pasar al siguiente nivel", Toast.LENGTH_SHORT).show();
                agujeroX = agujeroX - agujeroSpeed;
                if (hitBallChecker(agujeroX, agujeroY)) {
                    //score = score + 10;
                    //mp_bad.start();
                    if (mp_bueno != null){ mp_bueno.release(); }
                    if (mp != null){ mp.release(); }
                    if (mp_vidaextra != null){ mp_vidaextra.release(); }
                    if (mp_great != null){ mp_bueno.release(); }
                    agujeroX = -100;
                    //Toast.makeText(getContext(), "Nivel 2", Toast.LENGTH_LONG).show();
                    Intent gameOverIntent = new Intent(getContext(), ActivityJuego2.class);
                    gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    gameOverIntent.putExtra("score", score);
                    gameOverIntent.putExtra("vida", lifeCounterOfFish);
                    gameOverIntent.putExtra("desbloquearNivel1", desbloquear1);
                    gameOverIntent.putExtra("desbloquearNivel2", desbloquear2);
                    getContext().startActivity(gameOverIntent);
                }
                if (agujeroX <0){
                    agujeroX = canvasWidth + 21;
                    agujeroY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
                }
                //canvas.drawCircle(yellowX, yellowY, 25, yellowPaint);
                canvas.drawBitmap(agujero, agujeroX, agujeroY, agujeroPaint);
            }
        }
        public boolean hitBallChecker(int x, int y){
            if (fishX < x && x< (fishX + fish[0].getWidth()) && fishY < y && y<(fishY + fish[0].getHeight())){
                return true;
            }
            return false;
        }
        @Override
        public boolean onTouchEvent(MotionEvent event){
            int getx = (int) event.getX();
            int gety = (int) event.getY();
            int elevarx = (int) event.getX();
            int elevary = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                mp.start();
                if (getx >= 1108 && getx <= 1236 && gety >= 22 && gety <=71){
                    //Toast.makeText(getContext(), "Pausar", Toast.LENGTH_SHORT).show();
                    //handler.removeCallbacks((Runnable) timer);
                    mp.pause();
                    ventana();

                }
                if (elevarx >= 1139 && elevarx <= 1267 && elevary >= 556 && elevary <=677){
                    //Toast.makeText(getContext(), "elevar", Toast.LENGTH_SHORT).show();
                    //elevar();
                    tocuh = true;
                    fishSpeed = -22;
                }
                //Toast.makeText(getContext(), "Alto y largo: "+ getx + " " + gety, Toast.LENGTH_SHORT).show();
            }
            return  true;
        }
        private void ventana() {
            AlertDialog.Builder build = new AlertDialog.Builder(getContext(), R.style.DialogSalida);
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.dialogjuego, null);
            build.setView(v);
            btncontinuar=v.findViewById(R.id.btnContinua);
            btnayuda=v.findViewById(R.id.btnAyud);
            btnSalirJuego = v.findViewById(R.id.btnsalirJuego);
            btncancelar = v.findViewById(R.id.btnCancelar);
            AlertDialog dialoPause=build.create();
            btncancelar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.start();
                    dialoPause.dismiss();
                }
            });
            btncontinuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    score=0;
                    lifeCounterOfFish=3;
                    mp.start();
                    dialoPause.dismiss();
                }
            });
            btnayuda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ventanaayuda();
                }
            });
            btnSalirJuego.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.release();
                    Intent inte= new Intent(getApplicationContext(), MainActivity.class);
                    inte.putExtra("desbloquearNivel1", desbloquear1);
                    inte.putExtra("desbloquearNivel2", desbloquear2);
                    startActivity(inte);
                    dialoPause.dismiss();
                    StartAppAd.showAd(JuegoActivity.this);
                }
            });
            dialoPause.show();
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
    /*se controla la pulsacion del boton atras*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            mp.pause();
            AlertDialog.Builder build = new AlertDialog.Builder(this, R.style.DialogSalida);
            LayoutInflater inflater = this.getLayoutInflater();
            View v = inflater.inflate(R.layout.dialogjuego, null);
            build.setView(v);
            btncontinuar=v.findViewById(R.id.btnContinua);
            btnayuda=v.findViewById(R.id.btnAyud);
            btnSalirJuego = v.findViewById(R.id.btnsalirJuego);
            btncancelar = v.findViewById(R.id.btnCancelar);
            final  AlertDialog dialogPause=build.create();
            btncancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.start();
                    dialogPause.dismiss();
                }
            });
            btncontinuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    score=0;
                    lifeCounterOfFish=3;
                    mp.start();
                    dialogPause.dismiss();
                }
            });
            btnayuda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ventanaayuda();
                }
            });
            btnSalirJuego.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.release();
                    Intent inte= new Intent(getApplicationContext(), MainActivity.class);
                    inte.putExtra("desbloquearNivel1", desbloquear1);
                    inte.putExtra("desbloquearNivel2", desbloquear2);
                    startActivity(inte);
                    dialogPause.dismiss();
                    StartAppAd.showAd(JuegoActivity.this);
                }
            });
            dialogPause.show();
        }
        return super.onKeyDown(keyCode, event);
    }
    private void ventanaayuda() {
        AlertDialog.Builder alerbuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View vie = inflater.inflate(R.layout.ayudaitem, null);
        alerbuilder.setView(vie);
        btnSalir=vie.findViewById(R.id.btnSalir);
        final  AlertDialog dialogayuda=alerbuilder.create();
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogayuda.cancel();
            }
        });
        dialogayuda.show();
    }
}