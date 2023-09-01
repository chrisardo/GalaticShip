package co.lozano.galacticship;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdaptadorAvatar extends RecyclerView.Adapter<AdaptadorAvatar.ViewHolderAvatar> implements View.OnClickListener{
    private View.OnClickListener listener;
    List<AvatarVo> listaAvatars;
    View vista;
    int posicionMarcada=0;
    private int lastPosition = -1;//animacion
    Activity actividad;
    public AdaptadorAvatar(List<AvatarVo> listaAvatars  ) {
        this.listaAvatars = listaAvatars;
    }
    @NonNull
    @Override
    public ViewHolderAvatar onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        vista=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_grid_imagen,viewGroup,false);
        vista.setOnClickListener(this);
        return new ViewHolderAvatar(vista);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolderAvatar viewHolderAvatar, int i) {
        final int pos=i;
        final ViewHolderAvatar viewHolder=viewHolderAvatar;

        viewHolderAvatar.imgAvatar.setImageResource(listaAvatars.get(i).getAvatarId());

        viewHolderAvatar.cardAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posicionMarcada=pos;
                Utilidades.avatarSeleccion=listaAvatars.get(pos);
                Utilidades.avatarIdSeleccion=pos+1;
                notifyDataSetChanged();
            }
        });
        if (Utilidades.avatarIdSeleccion==0){
            if (posicionMarcada==i){
                viewHolder.barraSeleccion.setBackgroundColor(vista.getResources().getColor(PreferenciasJuego.colorTema));
            }else{
                viewHolder.barraSeleccion.setBackgroundColor(vista.getResources().getColor(R.color.colorBlanco));
            }
        }else{
            //se valida para cuando se tenga la consulta de un jugador registrado y pueda pintar el avatar definido
            if (Utilidades.avatarIdSeleccion-1==pos){
                viewHolder.barraSeleccion.setBackgroundColor(vista.getResources().getColor(PreferenciasJuego.colorTema));
            }else{
                viewHolder.barraSeleccion.setBackgroundColor(vista.getResources().getColor(R.color.colorBlanco));
            }
        }
        setAnimation(viewHolderAvatar.itemView, i);
    }
    @Override
    public int getItemCount() {
        return listaAvatars.size();
    }
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }
    public class ViewHolderAvatar extends RecyclerView.ViewHolder {
        CardView cardAvatar;
        ImageView imgAvatar;
        TextView barraSeleccion;
        public ViewHolderAvatar(@NonNull View itemView) {
            super(itemView);
            cardAvatar=itemView.findViewById(R.id.cardAvatar);
            imgAvatar=itemView.findViewById(R.id.idAvatar);
            barraSeleccion=itemView.findViewById(R.id.barraSeleccionId);
        }
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
}
