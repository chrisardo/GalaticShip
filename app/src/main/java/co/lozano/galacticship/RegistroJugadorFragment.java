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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appnext.banners.BannerAdRequest;
import com.appnext.banners.BannerSize;
import com.appnext.banners.BannerView;
import com.appnext.base.Appnext;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import co.lozano.galacticship.interfaces.IComunicaFragments;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link co.lozano.galacticship.RegistroJugadorFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class RegistroJugadorFragment extends Fragment {

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
    RecyclerView recyclerAvatars;
    ImageView imgeAvatar;
    ImageButton btnAtras,btnAyuda;
    FloatingActionButton fabRegistro;
    EditText campoNick;
    RadioButton radioM,radioF;
    //private BannerView bannerj_appnext;

    public RegistroJugadorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistroJugadorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistroJugadorFragment newInstance(String param1, String param2) {
        RegistroJugadorFragment fragment = new RegistroJugadorFragment();
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
        vista=inflater.inflate(R.layout.fragment_registro_jugador, container, false);
        Appnext.init(getContext());
        recyclerAvatars =vista.findViewById(R.id.recyclerAvatarsId);
        //imgeAvatar=vista.findViewById(R.id.imageView_Personaje);
        btnAtras=vista.findViewById(R.id.btnIcoAtras);
        fabRegistro=vista.findViewById(R.id.idFabRegistro);
        campoNick=vista.findViewById(R.id.campoNickName);
        radioM=vista.findViewById(R.id.radioM);
        radioF=vista.findViewById(R.id.radioF);
        /*bannerj_appnext = vista.findViewById(R.id.banerj);
        BannerView bannerj = new BannerView(getContext());
        bannerj.setPlacementId("da374833-5714-400f-a5c4-311747d5b1f0");
        bannerj_appnext.loadAd(new BannerAdRequest());
        bannerj.setBannerSize(BannerSize.BANNER);*/
        recyclerAvatars.setLayoutManager(new GridLayoutManager(this.actividad,4));
        recyclerAvatars.setHasFixedSize(true);
        btnAyuda=vista.findViewById(R.id.btnAyuda);
        btnAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                createSimpleDialog().show();
            }
        });
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iComunicaFragments.mostrarMenu();
                campoNick.setText("");
            }
        });
        fabRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarJugador();
            }
        });
        //se asigna la lista de avatars por defecto
        final AdaptadorAvatar miAdaptador=new AdaptadorAvatar(Utilidades.listaAvatars);
        recyclerAvatars.setAdapter(miAdaptador);
        return vista;
    }
    private void registrarJugador() {
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
            Long idResultante=db.insert(Utilidades.TABLA_JUGADOR,Utilidades.CAMPO_ID,values);
            if(idResultante!=-1){
                PreferenciasJuego.jugadorId=Integer.parseInt(idResultante+"");
                Toast.makeText(actividad,"El Jugador a sido Registrado con Exito!",Toast.LENGTH_SHORT).show();
                //    Toast.makeText(actividad,"Id Registro resultante: "+idResultante,Toast.LENGTH_LONG).show();
                PreferenciasJuego.nickName=campoNick.getText().toString();
                PreferenciasJuego.avatarId=Utilidades.avatarSeleccion.getId();
                SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(actividad);
                PreferenciasJuego.asignarPreferenciasJugador(preferences,actividad);
                campoNick.setText("");
                iComunicaFragments.mostrarMenu();
            }else
                Toast.makeText(actividad,"No se pudo Registrar el Jugador! ",Toast.LENGTH_SHORT).show();

            db.close();

        }else{
            Toast.makeText(actividad,"Verifique los datos de registro",Toast.LENGTH_SHORT).show();
        }

    }

    public AlertDialog createSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(actividad,R.style.DialogSalida);

        builder.setTitle("Ayuda")
                .setMessage("Ingrese el nombre del jugador, Género y seleccione la imagen del jugador de la lista de imagénes disponibles, posteriormente presione el botón para confirmar el registro.")
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