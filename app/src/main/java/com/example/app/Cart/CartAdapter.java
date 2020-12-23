package com.example.app.Cart;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.ProductDetails.Productdetails;
import com.example.app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {
    private List<cart> cartList;
    List<cart> cartsCLassListAll;
    Context context;
    private String LoginUserEmail;

    public CartAdapter(List<cart> modelCLassList, Context context) {
        this.cartList = modelCLassList;
        this.cartsCLassListAll = new ArrayList<>(modelCLassList);
        this.context = context;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartrows, parent, false);

        return new Viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        final String title = cartList.get(position).getTitle();
        final String quan = cartList.get(position).getQuantity();
        final String Price = cartList.get(position).getPrice();


        SharedPreferences getShared = context.getSharedPreferences("loginName", MODE_PRIVATE);
        LoginUserEmail = getShared.getString("Email", "Save a note and it will show up here");


        holder.setData(title, quan,Price);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            intent.putExtra("pid",cartList.get(position).getPid());
                            context.startActivity(intent);
                        }
                        if (which == 1) {
                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                            DatabaseReference node = db.getReference("Carts");

                            Query query = node.orderByChild("email").equalTo(LoginUserEmail);
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

              /*  Intent intent= new Intent(context,Popup.class);
                intent.putExtra("pid",cartList.get(position).getPid());
                context.startActivity(intent);*/
            }
        });

    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }
/*
    @Override
    public Filter getFilter() {

        return filter;
    }

    Filter filter=new Filter() {

        //Run on BackgroundThread

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<cart> filteredlist= new ArrayList<>();
            if (constraint.toString().isEmpty())
            {
                filteredlist.addAll(cartsCLassListAll);
            }else
            {
                for (cart data : cartsCLassListAll)
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
*/

/*

        //Run on UIThread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            cartList.clear();
            cartList.addAll((Collection<? extends cart>) results.values);
            notifyDataSetChanged();

        }
    };
*/

    class Viewholder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView quantity;
        private TextView price;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.productname);
            quantity = itemView.findViewById(R.id.productquantity);
            price = itemView.findViewById(R.id.ProdcutPrice);


        }


        private void setData(String title1, String quan,String Price) {
            title.setText("Product Name\t\t:"+title1);
            quantity.setText("Quantity\t\t:"+quan);
            price.setText("Price\t\t:"+Price);
            /* Picasso.get().load(image1).into(imageView);
             */


        }
    }
  }



