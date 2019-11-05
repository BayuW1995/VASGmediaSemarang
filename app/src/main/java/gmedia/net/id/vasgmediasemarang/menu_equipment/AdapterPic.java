package gmedia.net.id.vasgmediasemarang.menu_equipment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import gmedia.net.id.vasgmediasemarang.R;

public class AdapterPic extends RecyclerView.Adapter<AdapterPic.ViewHolderPic> {

    private Equipment activity;
    private List<MasterModelPIC> listPic;

    AdapterPic(Equipment activity, List<MasterModelPIC> listPic){
        this.activity = activity;
        this.listPic = listPic;
    }

    @NonNull
    @Override
    public ViewHolderPic onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderPic(LayoutInflater.from(activity).
                inflate(R.layout.item_pilih_pic, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPic holder, int position) {
        holder.bind(listPic.get(position));
    }

    @Override
    public int getItemCount() {
        return listPic.size();
    }

    class ViewHolderPic extends RecyclerView.ViewHolder{
        View layout_parent;
        TextView txt_nama, txt_email;

        ViewHolderPic(View itemView) {
            super(itemView);
            layout_parent = itemView.findViewById(R.id.layout_parent);
            txt_nama = itemView.findViewById(R.id.txt_nama);
            txt_email = itemView.findViewById(R.id.txt_email);
        }

        void bind(final MasterModelPIC p){
            txt_nama.setText(p.getNama());
            txt_email.setText(p.getEmail());

            layout_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.updatePic(p);
                }
            });
        }
    }
}
