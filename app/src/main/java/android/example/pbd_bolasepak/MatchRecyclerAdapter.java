package android.example.pbd_bolasepak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MatchRecyclerAdapter extends RecyclerView.Adapter<MatchRecyclerAdapter.ViewHolder> {
    Context context;
    ArrayList<MatchDetails> listMatch;

    public MatchRecyclerAdapter(Context context, ArrayList<MatchDetails> listMatch){
        this.context = context;
        this.listMatch = listMatch;
    }

    @NonNull
    @Override
    public MatchRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_match, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchRecyclerAdapter.ViewHolder holder, int position) {
        holder.homeName.setText(listMatch.get(position).getHomeName());
        holder.homeSkor.setText(listMatch.get(position).getHomeSkor());

        holder.awayName.setText(listMatch.get(position).getAwayName());
        holder.awaySkor.setText(listMatch.get(position).getAwaySkor());

        holder.tanggal.setText(listMatch.get(position).getDate());

        Picasso.get().load(listMatch.get(position).getHomeBadge()).fit().centerInside().into(holder.homeLogo);
        Picasso.get().load(listMatch.get(position).getAwayBadge()).fit().centerInside().into(holder.awayLogo);
    }

    @Override
    public int getItemCount() {
        return listMatch.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView homeName, homeSkor, awayName, awaySkor, tanggal;
        ImageView homeLogo, awayLogo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            homeName = itemView.findViewById(R.id.teamHome);
            homeLogo = itemView.findViewById(R.id.logoHome);
            homeSkor = itemView.findViewById(R.id.skorHome);

            awayName = itemView.findViewById(R.id.teamAway);
            awayLogo = itemView.findViewById(R.id.logoAway);
            awaySkor = itemView.findViewById(R.id.skorAway);

            tanggal = itemView.findViewById(R.id.date);
        }
    }
}
