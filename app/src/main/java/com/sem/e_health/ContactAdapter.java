package com.sem.e_health;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.database.DatabaseReference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MiHolder> {

    Context Mycontext ;
    List <Client> MyList ;
    private ItemClickListener itemClickListener;

    public ContactAdapter(Context mycontext, List<Client> myList) {
        Mycontext = mycontext;
        MyList = myList;
    }

    @NonNull
    @Override
    public MiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item2,parent,false);

                return new MiHolder(view) ;
    }


    @Override
    public void onBindViewHolder(@NonNull MiHolder holder, int position) {
        Client client = MyList.get(position);
        holder.phone.setText(client.getPhone());
        String ch =client.getLastName() + " " +client.getName() ;
        holder.NomPrenom.setText(ch);
        holder.age.setText(client.getAge());



    }
    public void removeItem(int position, DatabaseReference testRef) {
        MyList.remove(position +1 );
        Client client = MyList.get((position));
        String nomP = client.getName() + " " + client.getLastName();
        String nomP2 = client.getName() + " " + client.getLastName()+" TESTS";
        DatabaseReference ref1 = testRef.child("Clients").child(nomP);
        ref1.removeValue();
        DatabaseReference ref2 = testRef.child("Clients TESTS").child(nomP2);
        ref2.removeValue();
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return MyList.size();
    }

    class MiHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView NomPrenom ;
        TextView phone ;
        TextView age ;
        ImageView btnHistory, imgCall;


        public MiHolder(@NonNull View itemView) {
            super(itemView);
            NomPrenom = itemView.findViewById(R.id.nomp);
            phone= itemView.findViewById(R.id.tele);
            age= itemView.findViewById(R.id.agee);
            btnHistory = itemView.findViewById(R.id.btn_history);
            imgCall = itemView.findViewById(R.id.img_call);
            btnHistory.setOnClickListener(this);
            imgCall.setOnClickListener(this);

            //itemView.setOnClickListener(this);



        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
            {
                if (view.getId() == R.id.btn_history)
                itemClickListener.onHistoryItemClickListener(view,getAdapterPosition());
                if (view.getId() == R.id.img_call)
                    itemClickListener.onCallItemClickListener(view, getAdapterPosition());
            }

        }

    }
    public void setClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener=itemClickListener;
    }
    public  interface ItemClickListener{
        void onHistoryItemClickListener(View view, int position);
        void onCallItemClickListener(View view, int position);
    }
}



