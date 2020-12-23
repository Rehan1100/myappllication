package com.example.app.UserPanel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.ProductDetails.Productdetails;
import com.example.app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Adapter extends RecyclerView.Adapter<Adapter.Viewholder> implements Filterable {
    private List<ModelCLass> modelCLassList;
    List<ModelCLass> modelCLassListAll;
    Context context;
    public String Roll;

    public Adapter(List<ModelCLass> modelCLassList, Context context) {
        this.modelCLassList = modelCLassList;
        this.modelCLassListAll=new ArrayList<>(modelCLassList);
        this.context=context;
    }



    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rows, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {
        final String title = modelCLassList.get(position).getTitle();
        final String disc = modelCLassList.get(position).getDic();
        final String image = modelCLassList.get(position).getImage();
        final String id = modelCLassList.get(position).getPid();


        holder.setData(title,disc,image);


        SharedPreferences getShared2 = context.getApplicationContext().getSharedPreferences("RollName", MODE_PRIVATE);
        Roll = getShared2.getString("Roll", "Save a note and it will show up here");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Roll.equals("admin"))
                {

                    CharSequence option[] = new CharSequence[]
                            {
                                    "Edit",
                                    "Delete"

                            };

                    androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Cart Option");
                    builder.setItems(option, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which==0) {
                                Intent intent = new Intent(context, Productdetails.class);
                                intent.putExtra("pid",modelCLassList.get(position).getPid());
                                context.startActivity(intent);
                            }
                            if (which == 1) {
                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                DatabaseReference node = db.getReference("Products");

                                Query query = node.orderByChild("pid").equalTo(modelCLassList.get(position).getPid());
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (snapshot.exists()) {
                                            for (DataSnapshot datas : snapshot.getChildren()) {
                                                String key = datas.getKey();
                                                node.child(key).removeValue();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }
                    });
                    builder.show();

                }else{

                    Intent intent= new Intent(context.getApplicationContext(), Productdetails.class);
                    intent.putExtra("pid",modelCLassList.get(position).getPid());
                    intent.putExtra("image",modelCLassList.get(position).getImage());
                    //        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
                    context.startActivity(intent);
                }
/*

               Intent intent= new Intent(context.getApplicationContext(), ProductActivity.class);
               intent.putExtra("pid",modelCLassList.get(position).getPid());
               intent.putExtra("image",modelCLassList.get(position).getImage());
        //        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
*/
            }
        });



    }

    @Override
    public int getItemCount() {
        return modelCLassList.size();
    }

    @Override
    public Filter getFilter() {

        return filter;
    }

    Filter filter=new Filter() {

        //Run on BackgroundThread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<ModelCLass> filteredlist= new ArrayList<>();
            if (constraint.toString().isEmpty())
            {
                filteredlist.addAll(modelCLassListAll);
            }else
            {
                for (ModelCLass data : modelCLassListAll)
                {

                   if (data.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                       filteredlist.add(data);
                   }

                }
            }


            FilterResults filterResults= new FilterResults();
            filterResults.values=filteredlist;
            return filterResults;

        }


        //Run on UIThread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            modelCLassList.clear();
            modelCLassList.addAll((Collection<? extends ModelCLass>) results.values);
            notifyDataSetChanged();

        }
    };

    class Viewholder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView discription;
        private ImageView imageView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.Title);
            discription = itemView.findViewById(R.id.Discreption);
            imageView=itemView.findViewById(R.id.ProductImage);





        }


        private void setData(String title1, String disc1,String image1) {
            title.setText(title1);
            discription.setText(disc1);
            Picasso.get().load(image1).into(imageView);


        }
    }


}
