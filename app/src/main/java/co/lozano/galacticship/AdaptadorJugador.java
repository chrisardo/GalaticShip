package co.lozano.galacticship;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdaptadorJugador extends RecyclerView.Adapter<AdaptadorJugador.ViewHolderJugador> implements View.OnClickListener{
    private View.OnClickListener listener;
    List<JugadorVo> listaJugador;
    View vista;
    ArrayList<JugadorVo> arrayList;
    private int lastPosition = -1;//animacion

    public AdaptadorJugador(List<JugadorVo> listaJugador) {
        this.listaJugador = listaJugador;
    }
    @NonNull
    @Override
    public ViewHolderJugador onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        vista= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_jugador,viewGroup,false);
        vista.setOnClickListener(this);
        return new ViewHolderJugador(vista);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolderJugador viewHolderJugador, int i) {
        //se resta uno ya que buscamos la lista de elementos que inicia en la pos 0
        viewHolderJugador.imgAvatar.setImageResource(Utilidades.listaAvatars.get(listaJugador.get(i).getAvatar()-1).getAvatarId());
        viewHolderJugador.txtNombre.setText(listaJugador.get(i).getNombre());
        if (listaJugador.get(i).getGenero().equals("M")){
            viewHolderJugador.txtGenero.setText("Masculino");
        }else{
            viewHolderJugador.txtGenero.setText("Femenino");
            viewHolderJugador.txtGenero.setTextColor(vista.getResources().getColor(R.color.purple_200));
        }
        setAnimation(viewHolderJugador.itemView, i);
    }
    public void setAnimation(View viewToAnimate, int position){
        if (position > lastPosition){
            ScaleAnimation animation = new ScaleAnimation(0.0f,1.0f,0.0f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(1500);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }

    }
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
    @Override
    public int getItemCount() {
        return listaJugador.size();
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public class ViewHolderJugador extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView txtNombre;
        TextView txtGenero;
        public ViewHolderJugador(@NonNull View itemView) {
            super(itemView);
            imgAvatar=itemView.findViewById(R.id.idAvatar);
            txtNombre=itemView.findViewById(R.id.idNombre);
            txtGenero=itemView.findViewById(R.id.idGenero);
        }
    }
    //filter
    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        listaJugador.clear();
        if (charText.length()==0){
            listaJugador.addAll(arrayList);
        }
        else {
            for (JugadorVo model : arrayList){
                if (model.getNombre().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    listaJugador.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }
}

