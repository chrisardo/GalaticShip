package co.lozano.galacticship;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import co.lozano.galacticship.interfaces.IComunicaFragments;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link co.lozano.galacticship.RankingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Activity actividad;
    View vista;
    IComunicaFragments iComunicaFragments;
    RecyclerView recyclerJugadores;
    JugadorVo jugadorSeleccionado;
    ImageButton btnAyuda, btnBuscar;
    FloatingActionButton btnAtras;
    TextView txtMensaje;
    private static ArrayList<ResultadosVo> listaResultados;
    Spinner comboTipoResultados;
    public RankingFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RankingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RankingFragment newInstance(String param1, String param2) {
        RankingFragment fragment = new RankingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_ranking, container, false);
        comboTipoResultados=vista.findViewById(R.id.comboTipoResultados);
        txtMensaje=vista.findViewById(R.id.txtSinDatos);
        ArrayAdapter<CharSequence> adapterTipoRes=ArrayAdapter.createFromResource(actividad,R.array.combo_resultados,R.layout.spinner_item_christian);
        comboTipoResultados.setAdapter(adapterTipoRes);
        comboTipoResultados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int jugadorId= PreferenciasJuego.jugadorId;
                String tipoConsulta=comboTipoResultados.getSelectedItem().toString();
                //String nivelJuego=comboNivel.getSelectedItem().toString();
                String consulta="";
                if(!tipoConsulta.equals("Individual")){
                    consulta="select j.id,j.nombre,j.genero,j.avatar,p.puntos [puntajes]from "+ Utilidades.TABLA_JUGADOR+" j,"
                            + Utilidades.TABLA_PUNTAJE+" p where j.id=p.id"
                            +" order by p.puntos desc";
                }else {
                    consulta="select j.id,j.nombre,j.genero,j.avatar,p.puntos from "+ Utilidades.TABLA_JUGADOR+" j," +
                            ""+ Utilidades.TABLA_PUNTAJE+" p where j.id=p.id and j.id="+jugadorId
                            +" order by p.puntos DESC";
                }
                consultarResultados(consulta);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        recyclerJugadores =vista.findViewById(R.id.recyclerJugadoresId);
        recyclerJugadores.setLayoutManager(new LinearLayoutManager(this.actividad));
        //recyclerJugadores.setHasFixedSize(true);";
        btnAtras=vista.findViewById(R.id.btnIcoAtras);
        btnAyuda=vista.findViewById(R.id.btnAyuda);
        btnAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSimpleDialog().show();
            }
        });
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iComunicaFragments.mostrarMenu();
            }
        });
        consultarResultados("select j.id,j.nombre,j.genero,j.avatar,p.puntos [puntajes] from "+ Utilidades.TABLA_JUGADOR+" j,"+ Utilidades.TABLA_PUNTAJE+
                " p where j.id=p.id and j.id="+ PreferenciasJuego.jugadorId +" order by p.puntos DESC");
        return vista;
    }
    private void consultarResultados(String consulta) {
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(actividad, Utilidades.NOMBRE_BD,null,1);
        SQLiteDatabase db=conn.getReadableDatabase();
        ResultadosVo resultados=null;
        listaResultados=new ArrayList<ResultadosVo>();
        //select * from usuarios
        Cursor cursor=db.rawQuery(consulta,null);
        while (cursor.moveToNext()){
            resultados=new ResultadosVo();
            resultados.setId(cursor.getInt(0));
            resultados.setNombre(cursor.getString(1));
            resultados.setGenero(cursor.getString(2));
            resultados.setAvatar(cursor.getInt(3));
            resultados.setPuntos(cursor.getInt(4));
            //resultados.setNivel(cursor.getString(5));

            listaResultados.add(resultados);
        }

        if (listaResultados.size()>0){
            txtMensaje.setText("Se encontraron "+listaResultados.size()+" resultados");
        }else{
            txtMensaje.setText("No hay puntajes asociados!");
        }

        db.close();
        llenarAdaptadorJugadores();
    }
    private void llenarAdaptadorJugadores() {
        //se asigna la lista de jugadores por defecto
        final AdaptadorResultados miAdaptadorJugadores=new AdaptadorResultados(listaResultados);
        miAdaptadorJugadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    Toast.makeText(actividad,"Avatar "+Utilidades.listaJugadores.get(recyclerJugadores.getChildAdapterPosition(view)).getNombre(),Toast.LENGTH_SHORT).show();
                jugadorSeleccionado= Utilidades.listaJugadores.get(recyclerJugadores.getChildAdapterPosition(view));
            }

        });

        recyclerJugadores.setAdapter(miAdaptadorJugadores);
    }
    public AlertDialog createSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(actividad,R.style.DialogSalida);

        builder.setTitle("Ayuda")
                .setMessage("Defina los parámetros de consulta para conocer el Ranking de GalacticShip")
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

        return builder.create();
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.actividad= (Activity) context;
            iComunicaFragments= (IComunicaFragments) this.actividad;
        }
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}