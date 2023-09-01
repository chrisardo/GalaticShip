package co.lozano.galacticship;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Utilidades {
    public static final int LIST=1;
    public static int visualizacion=LIST;
    //permite tener la referencia del avatar seleccionado para el momento del registro en la BD
    public static AvatarVo avatarSeleccion=null;
    public static int avatarIdSeleccion=0;

    public static ArrayList<AvatarVo> listaAvatars=null;
    public static ArrayList<JugadorVo> listaJugadores=null;

    public static int correctas, incorrectas, puntaje,nivelJuego;

    public static final String NOMBRE_BD="galacticship_bd";
    //Constantes campos tabla usuario
    public static final String TABLA_JUGADOR="jugador";
    public static final String CAMPO_ID="id";
    public static final String CAMPO_NOMBRE="nombre";
    public static final String CAMPO_GENERO="genero";
    public static final String CAMPO_AVATAR="avatar";

    //Constantes campos tabla puntaje
    public static final String TABLA_PUNTAJE="puntaje_jugador";
    public static final String CAMPO_ID_JUGADOR="id";
    public static final String CAMPO_PUNTOS="puntos";
    public static final String CAMPO_NIVEL="nivel";
    //public static final String CAMPO_DESBLOQUEO="desbloquear";



    public static final String CREAR_TABLA_JUGADOR="CREATE TABLE "+TABLA_JUGADOR+" ("+CAMPO_ID+" INTEGER PRIMARY KEY, "+CAMPO_NOMBRE+" TEXT,"+CAMPO_GENERO+" TEXT,"+CAMPO_AVATAR+" INTEGER)";
    public static final String CREAR_TABLA_PUNTAJES="CREATE TABLE "+TABLA_PUNTAJE+" ("+CAMPO_ID_JUGADOR+" INTEGER, "
            +CAMPO_PUNTOS+" INTEGER,"+" INTEGER,"+CAMPO_NIVEL+" TEXT)";


    public static void obtenerListaAvatars() {
        //se instancian los avatars y se llena la lista
        listaAvatars=new ArrayList<AvatarVo>();
        listaAvatars.add(new AvatarVo(1,R.drawable.m1,"Avatar1"));
        listaAvatars.add(new AvatarVo(2,R.drawable.m2,"Avatar2"));
        listaAvatars.add(new AvatarVo(3,R.drawable.m4,"Avatar3"));
        listaAvatars.add(new AvatarVo(4,R.drawable.m5,"Avatar4"));
        listaAvatars.add(new AvatarVo(5,R.drawable.h1,"Avatar5"));
        listaAvatars.add(new AvatarVo(6,R.drawable.h2,"Avatar6"));
        listaAvatars.add(new AvatarVo(7,R.drawable.h3,"Avatar7"));
        listaAvatars.add(new AvatarVo(8,R.drawable.h4,"Avatar8"));
    }

    public static void consultarListaJugadores(Activity actividad) {
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(actividad,NOMBRE_BD,null,1);
        SQLiteDatabase db=conn.getReadableDatabase();

        JugadorVo jugador=null;
        listaJugadores=new ArrayList<JugadorVo>();
        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_JUGADOR,null);

        while (cursor.moveToNext()){
            jugador=new JugadorVo();
            jugador.setId(cursor.getInt(0));
            jugador.setNombre(cursor.getString(1));
            jugador.setGenero(cursor.getString(2));
            jugador.setAvatar(cursor.getInt(3));

            listaJugadores.add(jugador);
        }

        db.close();
    }
}
