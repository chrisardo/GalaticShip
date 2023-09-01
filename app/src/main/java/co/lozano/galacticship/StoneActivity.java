package co.lozano.galacticship;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.appnext.ads.interstitial.Interstitial;
import com.appnext.base.Appnext;

import java.util.Timer;
import java.util.TimerTask;

public class StoneActivity extends AppCompatActivity {
    private SpaceView gameView;
    private Handler handler = new Handler();
    private  final static long Interval = 30;
    Button btncontinuar, btnayuda, btnSalirJuego;
    MediaPlayer mp, mp_great, mp_bueno, mp_bad, mp_vidaextra;
    int desbloquear1 = 1, desbloquear2, Ayuda1=1, score, lifeCounterOfFish;
    private View decorView;//Para quitar el estatus bar
    ImageView btnSalir, btncancelar;
    Interstitial interstitialtipo_appnext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stone);
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
        desbloquear2 = getIntent().getIntExtra("desbloquearNivel2",0);
        score= getIntent().getIntExtra("score",0);
        gameView = new SpaceView(this);
        setContentView(gameView);
        Timer timer = new Timer();
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
    class SpaceView extends View {
        private Bitmap fish[] = new Bitmap[2];
        private int fishX = 50;//Mover al costado
        private int fishY;
        int minFishY, maxFishY;
        private int fishSpeed;
        public boolean tocuh = false;
        private int canvasWidth2,canvasWidth, canvasHeight;
        private int yellowX, yellowY, yellowSpeed = 16;
        private Paint yellowPaint = new Paint();
        private int botiquinX, botiquinY, botiquinSpeed = 15;
        private Paint botiquinPaint = new Paint();
        private int score;
        private int greenX, greenY, greenSpeed=25;
        private Paint greenPaint = new Paint();
        private int lunaX, lunaY, lunaSpeed=16;
        private Paint lunaPaint = new Paint();
        private int meteoritoX, meteoritoY, meteoritoSpeed=16;
        private Paint meteoritoPaint = new Paint();
        private int agujeroX, agujeroY, agujeroSpeed=10;
        private Paint agujeroPaint = new Paint();
        private Paint pausarPaint = new Paint();
        private Paint btnelevarPaint = new Paint();
        private Paint redPaint = new Paint();
        private int fondoX=0, fondoY=0, fondoSpeed=2;
        private Paint fondoPaint = new Paint();
        private int fondo2X=0, fondo2Y=0, fondoSpeed2=2;
        private Paint fondo2Paint = new Paint();
        private Bitmap backgroundImage, backgroundImage2;
        private Paint scorePaint = new Paint();
        private Bitmap life[] = new Bitmap[2];
        Bitmap meteorito, diamante, estrella, luna, botiquin, agujero , pausar, btnelevar;
        private Paint nombrePaint = new Paint();
        private Paint txtvidaPaint = new Paint();
        String jugador;
        int x, y, yp=4, xp= 1100 ,elevarx, elevary, evento, actionDown;
        private Thread thread;
        Canvas canvas;
        //private boolean isPlaying;
        public SpaceView(Context context) {
            super(context);
            fish[0] = BitmapFactory.decodeResource(getResources(), R.drawable.nave);
            fish[1] = BitmapFactory.decodeResource(getResources(), R.drawable.nav);
            backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.fondojuego);
            backgroundImage2 = BitmapFactory.decodeResource(getResources(), R.drawable.fondojuego2);
            yellowPaint.setAntiAlias(false);
            greenPaint.setAntiAlias(false);
            fondoPaint.setAntiAlias(false);
            botiquinPaint.setAntiAlias(false);
            lunaPaint.setAntiAlias(false);
            pausarPaint.setAntiAlias(false);
            btnelevarPaint.setAntiAlias(false);
            agujeroPaint.setAntiAlias(false);
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
            pausar = BitmapFactory.decodeResource(getResources(), R.drawable.pausar);
            meteorito = BitmapFactory.decodeResource(getResources(), R.drawable.meteorito);
            luna = BitmapFactory.decodeResource(getResources(), R.drawable.luna);
            agujero = BitmapFactory.decodeResource(getResources(), R.drawable.agujero);
            diamante = BitmapFactory.decodeResource(getResources(), R.drawable.diamante);
            estrella = BitmapFactory.decodeResource(getResources(), R.drawable.moneda);
            botiquin = BitmapFactory.decodeResource(getResources(), R.drawable.botiquin);
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
            //canvasWidth2 = canvas.getWidth();
            canvasHeight = canvas.getHeight();
            int maxFonX = canvasWidth; //- backgroundImage.getWidth()
            int maxFon2X = canvasWidth; //- backgroundImage.getWidth()
            minFishY = fish[0].getHeight();
            maxFishY = canvasHeight - fish[0].getHeight() * 1;
            fondoX = fondoX - fondoSpeed;//mover de dercha a izquierda
            fondo2X = fondo2X - fondoSpeed2;//mover de dercha a izquierda
            if (fondoX > 0){
                fondoX = -maxFonX;
                if (fondo2X < 0){
                    fondo2X = maxFon2X;
                }
            }
            if (fondoX < -canvasWidth){
                fondoX = -fondoSpeed;
                if (fondo2X < 0){
                    fondo2X = maxFon2X;
                }
            }
            if (fondo2X > canvasWidth){
                fondo2X = -fondoSpeed2;

                if (fondoX > 0){
                    fondoX = -maxFonX;
                }
            }
            /*fondoX = fondoX - fondoSpeed;  //mover de izquierda a derecha
            if (fondoX < 0){
                fondoX = -maxFonX;
            }
            if (fondoX > canvasWidth){
                fondoX = -fondoSpeed;
            }*/

            /*fondoX = fondoX - fondoSpeed;  //mover de izquierda a derecha
            if (fondoX < 0){
                fondoX = -maxFonX;
            }
            if (fondoX > canvasWidth){
                fondoX = -fondoSpeed;
            }*/
            canvas.drawBitmap(backgroundImage, 0,0, null);
            //canvas.drawBitmap(backgroundImage2, fondo2X,0, null);
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
            yellowX = yellowX - yellowSpeed;
            if (hitBallChecker(yellowX, yellowY)) {
                mp_great = MediaPlayer.create(getContext(), R.raw.wonderful);
                mp_great.start();
                if (mp_bueno != null){ mp_bueno.release(); }
                score = score + 10;
                yellowX = -100;
            }
            if (yellowX <0){
                yellowX = canvasWidth + 21;
                yellowY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
            }
            //canvas.drawCircle(yellowX, yellowY, 25, yellowPaint);
            canvas.drawBitmap(estrella, yellowX, yellowY, yellowPaint);
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
                    Intent gameOverIntent = new Intent(getContext(), GameOver2.class);
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
            canvas.drawText("Vidas : ", 930, 150, txtvidaPaint);
            canvas.drawBitmap(pausar, xp, yp, pausarPaint);
            canvas.drawBitmap(btnelevar, 1130, 550, btnelevarPaint);
            for (int i=0; i<3; i++){
                x= (int)(1130 + life[0].getWidth() * 1.5*i);//500
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
                        Intent gameOverIntent = new Intent(getContext(), GameOver2.class);
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
                agujeroX = agujeroX - agujeroSpeed;
                if (hitBallChecker(agujeroX, agujeroY)) {
                    //score = score + 10;
                    //mp_bad.start();
                    if (mp_bueno != null){ mp_bueno.release(); }
                    if (mp != null){ mp.release(); }
                    if (mp_vidaextra != null){ mp_vidaextra.release(); }
                    if (mp_great != null){ mp_bueno.release(); }
                    agujeroX = -100;
                    Intent gameOverIntent = new Intent(getContext(), GameOver2.class);
                    gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    gameOverIntent.putExtra("score", score);
                    gameOverIntent.putExtra("vida", lifeCounterOfFish);
                    gameOverIntent.putExtra("ganoPerd", "¡Mundo Stone superado!");
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
            evento=event.getAction();
            actionDown = MotionEvent.ACTION_DOWN;
            int getx = (int) event.getX();
            int gety = (int) event.getY();
            elevarx = (int) event.getX();
            elevary = (int) event.getY();
            if (evento == actionDown){
                mp.start();
                if (getx >= 1108 && getx <= 1236 && gety >= 22 && gety <=71){
                    //Toast.makeText(getContext(), "Pausar", Toast.LENGTH_SHORT).show();
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
            decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if (visibility == 0){
                        decorView.setSystemUiVisibility(hideSystemDta());
                    }
                }
            });
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
                    /*Intent inte= new Intent(getApplicationContext(), AyudaActivity.class);
                    inte.putExtra("desbloquearNivel1", desbloquear1);
                    inte.putExtra("desbloquearNivel2", desbloquear2);
                    inte.putExtra("Ayuda1", Ayuda1);
                    inte.putExtra("score", score);
                    startActivity(inte);*/
                    ventanaayuda();
                }
            });
            btnSalirJuego.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.release();
                    dialoPause.dismiss();
                    Intent inte= new Intent(getApplicationContext(), TipoJuego.class);
                    inte.putExtra("desbloquearNivel1", desbloquear1);
                    inte.putExtra("desbloquearNivel2", desbloquear2);
                    startActivity(inte);
                    interstitialtipo_appnext.showAd();
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
                    dialogPause.dismiss();
                    Intent inte= new Intent(getApplicationContext(), TipoJuego.class);
                    inte.putExtra("desbloquearNivel1", desbloquear1);
                    inte.putExtra("desbloquearNivel2", desbloquear2);
                    startActivity(inte);
                    interstitialtipo_appnext.showAd();
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