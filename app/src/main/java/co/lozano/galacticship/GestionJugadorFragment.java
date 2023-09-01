package co.lozano.galacticship;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appnext.banners.BannerAdRequest;
import com.appnext.banners.BannerSize;
import com.appnext.banners.BannerView;
import com.appnext.base.Appnext;

import co.lozano.galacticship.interfaces.IComunicaFragments;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link co.lozano.galacticship.GestionJugadorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GestionJugadorFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    int eventoEliminar=0;
    Activity actividad;
    View vista;
    IComunicaFragments iComunicaFragments;
    RecyclerView recyclerAvatars,recyclerJugadores;
    ImageButton btnAtras,btnAyuda;
    ImageView btnSalir;
    TextView barraSeleccion,separador;
    //FloatingActionsMenu grupoBotones;
    Button fabActualizar,fabEliminar,fabConfirmar;
    EditText campoNick;
    RadioButton radioM,radioF;
    JugadorVo jugadorSeleccionado;
    Toolbar toolbar;
    AdaptadorJugador miAdaptadorJugadores;

    private BannerView bannergj_appnext;
    //private GestioJuga gestioJuga;

    public GestionJugadorFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GestionJugadorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static co.lozano.galacticship.GestionJugadorFragment newInstance(String param1, String param2) {
        co.lozano.galacticship.GestionJugadorFragment fragment = new co.lozano.galacticship.GestionJugadorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable  Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
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
        vista=inflater.inflate(R.layout.fragment_gestion_jugador, container, false);
        Appnext.init(getContext());
        recyclerJugadores =vista.findViewById(R.id.recyclerJugadoresId);
        btnAtras=vista.findViewById(R.id.btnIcoAtras);
        bannergj_appnext = vista.findViewById(R.id.banergj);
        BannerView bannergj = new BannerView(getContext());
        bannergj.setPlacementId("da374833-5714-400f-a5c4-311747d5b1f0");
        bannergj_appnext.loadAd(new BannerAdRequest());
        bannergj.setBannerSize(BannerSize.BANNER);
        recyclerJugadores.setLayoutManager(new LinearLayoutManager(this.actividad));
        recyclerJugadores.setHasFixedSize(true);
        btnAyuda=vista.findViewById(R.id.btnAyuda);
        btnAyuda.setOnClickListener(this);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventoEliminar==0){
                    iComunicaFragments.mostrarMenu();
                }else{
                    Toast.makeText(actividad,"Seleccione un Jugador", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //toolbar = vista.findViewById(R.id.toolbar);
        //toolbar.inflateMenu(R.menu.menu);
        /*toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onOptionsItemSelected(item);
                return false;
            }
        });*/
        llenarAdaptadorJugadores();
        return vista;
    }
    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //menu.clear();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                // Here is where we are going to implement the filter logic
                if (TextUtils.isEmpty(newText)){
                    miAdaptadorJugadores.filter("");
                    //recyclerJugadores.clearTextFilter();
                }
                else {
                    miAdaptadorJugadores.filter(newText);
                }
                return true;
            }

        });
    }*/
    // Antes de validar la eliminación, verificar porqué despues de seleccionado el elemento ya no deja seleccionar otro con el cambio de barra
    private void eliminarJugador() {
        if (campoNick.getText().toString()!=null && !campoNick.getText().toString().trim().equals("")){
           ConexionSQLiteHelper conn=new ConexionSQLiteHelper(actividad,Utilidades.NOMBRE_BD,null,1);
            SQLiteDatabase db=conn.getWritableDatabase();
            int idResultante= db.delete(Utilidades.TABLA_JUGADOR, Utilidades.CAMPO_ID+"="+jugadorSeleccionado.getId(), null);
            if(idResultante!=-1){
                //Toast.makeText(actividad,"Id Registro: "+idResultante,Toast.LENGTH_SHORT).show();
                Toast.makeText(actividad,"El Jugador a sido Eliminado con Exito!",Toast.LENGTH_SHORT).show();
                campoNick.setText("");
                radioF.setChecked(false);
                radioM.setChecked(false);
                recyclerJugadores.scrollToPosition(jugadorSeleccionado.getId());
                recyclerAvatars.scrollToPosition(0);
                Utilidades.consultarListaJugadores(actividad);
                PreferenciasJuego.nickName="NA";
                PreferenciasJuego.avatarId=1;
                SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(actividad);
                PreferenciasJuego.asignarPreferenciasJugador(preferences,actividad);
                //se eliminan los puntajes
                int idResultante2= db.delete(Utilidades.TABLA_PUNTAJE, Utilidades.CAMPO_ID_JUGADOR+"="+jugadorSeleccionado.getId(), null);
                if(idResultante2==-1){
                    Toast.makeText(actividad,"No se pudieron eliminar los puntajes!",Toast.LENGTH_SHORT).show();
                }
                eventoEliminar=1;

            }else
                Toast.makeText(actividad,"No se pudo Eliminar el Jugador! ",Toast.LENGTH_SHORT).show();


            db.close();
        }else{
            Toast.makeText(actividad,"Debe seleccionar un Jugador para poder eliminarlo",Toast.LENGTH_SHORT).show();
        }
    }
    private void llenarAdaptadorJugadores() {
        //se asigna la lista de jugadores por defecto
        miAdaptadorJugadores=new AdaptadorJugador(Utilidades.listaJugadores);
        miAdaptadorJugadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //grupoBotones.collapse();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View v = inflater.inflate(R.layout.perfilusuario, null);
                builder.setView(v);
                recyclerAvatars =v.findViewById(R.id.recyclerAvatarsId);
                campoNick=v.findViewById(R.id.campoNickName);
                radioM=v.findViewById(R.id.radioM);
                radioF=v.findViewById(R.id.radioF);
                jugadorSeleccionado=Utilidades.listaJugadores.get(recyclerJugadores.getChildAdapterPosition(view));
                campoNick.setText(jugadorSeleccionado.getNombre());
                if(jugadorSeleccionado.getGenero().equals("M")){
                    radioM.setChecked(true);
                }else{
                    radioF.setChecked(true);
                }
                PreferenciasJuego.jugadorId=jugadorSeleccionado.getId();
                Utilidades.avatarSeleccion=Utilidades.listaAvatars.get(jugadorSeleccionado.getAvatar()-1);
                llenarAdaptadorAvatars(jugadorSeleccionado.getAvatar());
                btnSalir=v.findViewById(R.id.btnSalir);
                fabActualizar=v.findViewById(R.id.idFabActualizar);
                fabEliminar=v.findViewById(R.id.idFabEliminar);
                fabConfirmar=v.findViewById(R.id.idFabConfirmar);
                recyclerAvatars.setLayoutManager(new GridLayoutManager(getActivity(),4));
                recyclerAvatars.setHasFixedSize(true);
                final  AlertDialog dialogGes=builder.create();
                fabActualizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //dialogGes.cancel();
                        //actualizarJugador();
                        String genero="";
                        if (radioM.isChecked()==true){
                            genero="M";
                        }else if(radioF.isChecked()==true){
                            genero="F";
                        }else{
                            genero="No seleccionado";
                        }
                        if (!genero.equals("No seleccionado") && campoNick.getText().toString()!=null && !campoNick.getText().toString().trim().equals("")){
                            String nickName=campoNick.getText().toString();
                            int avatarId=Utilidades.avatarSeleccion.getId();
                            ConexionSQLiteHelper conn=new ConexionSQLiteHelper(actividad,Utilidades.NOMBRE_BD,null,1);
                            SQLiteDatabase db=conn.getWritableDatabase();
                            ContentValues values=new ContentValues();
                            values.put(Utilidades.CAMPO_NOMBRE,nickName);
                            values.put(Utilidades.CAMPO_GENERO,genero);
                            values.put(Utilidades.CAMPO_AVATAR,avatarId);
                            int idResultante=db.update(Utilidades.TABLA_JUGADOR, values, Utilidades.CAMPO_ID+"="+jugadorSeleccionado.getId(), null);
                            if(idResultante!=-1){
                                Toast.makeText(actividad,"El Jugador a sido Actualizado con Exito!",Toast.LENGTH_SHORT).show();
                                recyclerJugadores.scrollToPosition(jugadorSeleccionado.getId()-1);//asigno la posicion del elemento seleccionado -1 ya que en la bd guarda el elemento real pero en la lista inicia en 0
                                Utilidades.consultarListaJugadores(actividad);
                                dialogGes.cancel();
                            }else
                                Toast.makeText(actividad,"No se pudo Actualizado el Jugador! ",Toast.LENGTH_SHORT).show();
                            db.close();
                        }else{
                            Toast.makeText(actividad,"Verifique los datos para realizar la actualización",Toast.LENGTH_SHORT).show();
                        }
                        llenarAdaptadorJugadores();
                    }
                });
                fabConfirmar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (campoNick.getText().toString()!=null && !campoNick.getText().toString().trim().equals("")){
                            PreferenciasJuego.nickName=campoNick.getText().toString();
                            PreferenciasJuego.avatarId=Utilidades.avatarSeleccion.getId();
                            SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(actividad);
                            PreferenciasJuego.asignarPreferenciasJugador(preferences,actividad);
                            // grupoBotones.collapse();
                            eventoEliminar=0;
                            iComunicaFragments.mostrarMenu();
                            dialogGes.cancel();
                        }else{
                            Toast.makeText(actividad,"Verifique los datos para realizar la selección",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                fabEliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //dialogGes.cancel();
                        if (campoNick.getText().toString()!=null && !campoNick.getText().toString().trim().equals("")){
                            //dialogoEliminar().show();
                            //dialogGes.show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(actividad,R.style.DialogSalida);
                            builder.setTitle("Advertencia!!!")
                                    .setMessage("¿Está seguro que desea eliminar a "+jugadorSeleccionado.getNombre().toUpperCase()+" y toda su información?")
                                    .setNegativeButton("CANCELAR",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            })
                                    .setPositiveButton("ACEPTAR",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    eliminarJugador();
                                                    llenarAdaptadorJugadores();
                                                    dialogGes.cancel();
                                                }
                                            });
                            builder.show();
                            builder.create();
                        }else{
                            Toast.makeText(actividad,"Debe seleccionar un Jugador para poder eliminarlo",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialogGes.show();
                btnSalir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(actividad,"Salir",Toast.LENGTH_SHORT).show();
                        dialogGes.dismiss();
                    }
                });
                //llenarAdaptadorAvatars(0);
            }

        });
        //llenarAdaptadorAvatars(0);
        recyclerJugadores.setAdapter(miAdaptadorJugadores);
    }
    private void llenarAdaptadorAvatars(int avatarId) {
        // Toast.makeText(actividad,"reinicia adapter ",Toast.LENGTH_SHORT).show();
        Utilidades.avatarIdSeleccion=avatarId;
        //se asigna la lista de avatars por defecto
        final AdaptadorAvatar miAdaptadorAvatars=new AdaptadorAvatar(Utilidades.listaAvatars);
        recyclerAvatars.scrollToPosition(avatarId-1);//asigno la posicion del elemento seleccionado -1 ya que en la bd guarda el elemento real pero en la lista inicia en 0
        recyclerAvatars.setAdapter(miAdaptadorAvatars);
    }
    public AlertDialog createSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(actividad,R.style.DialogSalida);
        builder.setTitle("Ayuda")
                .setMessage("Elija el jugador de la lista de Jugadores disponible, si lo desea puede modificar su información (Nombre, Género, Imagen) y posteriormente " +
                        " podrá actualizarlo, eliminarlo o seleccionarlo para Jugar.")
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAyuda:
                createSimpleDialog().show();
                break;
        }
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
