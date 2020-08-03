package com.abdullah.graduationproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.abdullah.graduationproject.Activities.DetailsActivity;
import com.abdullah.graduationproject.Activities.MainActivity;
import com.abdullah.graduationproject.Classes.Items;
import com.abdullah.graduationproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WaterAdapter extends RecyclerView.Adapter<WaterAdapter.DeleteItemsAdapterHolder> {

    Context context;
    List<Items> itemsList;
    String ItemType = "Water";

    public WaterAdapter(List<Items> itemsList, Context context) {
        this.itemsList = itemsList;
        this.context = context;
    }

    @NonNull
    @Override
    public DeleteItemsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.store_custom, parent, false);
        return new DeleteItemsAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DeleteItemsAdapterHolder holder, int position) {
        final Items items = itemsList.get(position);
        Picasso.get()
                .load(items.getImage())
                .placeholder(R.mipmap.ic_icon)
                .fit()
                .centerCrop()
                .into(holder.Image);
        holder.Name.setText(items.getName());
        holder.Price.setText(items.getPrice() + " دينار");
        holder.Rating.setText(items.getRating());
        if (MainActivity.SaveSharedPreference.getLogIn(context).equals("true")) {
            FavoriteState(items.getId(), holder);
        }

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toDetailsActivity = new Intent(context, DetailsActivity.class);
                toDetailsActivity.putExtra("Item", ItemType);
                toDetailsActivity.putExtra("Id", items.getId());
                context.startActivity(toDetailsActivity);
            }
        });

        holder.NotFavoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.SaveSharedPreference.getLogIn(context).equals("true")) {
                    AddFavorite(items);
                    holder.NotFavoriteImageView.setVisibility(View.GONE);
                    holder.FavoriteImageView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(context, "يرجى تسجيل الدخول", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.FavoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.SaveSharedPreference.getLogIn(context).equals("true")) {
                    DeleteFavorite(items.getId());
                    holder.NotFavoriteImageView.setVisibility(View.VISIBLE);
                    holder.FavoriteImageView.setVisibility(View.GONE);
                } else {
                    Toast.makeText(context, "يرجى تسجيل الدخول", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void FavoriteState(String id, final DeleteItemsAdapterHolder holder) {
        FirebaseFirestore.getInstance().collection("Users")
                .document(MainActivity.SaveSharedPreference.getPhoneNumber(context))
                .collection("Favorite").document(id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            holder.FavoriteImageView.setVisibility(View.VISIBLE);
                            holder.NotFavoriteImageView.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void AddFavorite(Items items) {
        Map<String, Object> user = new HashMap<>();
        if (MainActivity.SaveSharedPreference.getFavoriteCounter(context).equals("0")) {
            user.put("counter", "0");
            MainActivity.SaveSharedPreference.setFavoriteCounter(context, "1");
        } else {
            long counter = Long.parseLong(MainActivity.SaveSharedPreference.getFavoriteCounter(context));
            user.put("counter", counter);
            MainActivity.SaveSharedPreference.setFavoriteCounter(context, String.valueOf(counter + 1));
        }
        user.put("Category", ItemType);
        user.put("Product Name", items.getName());
        user.put("Provider", items.getProvider());
        user.put("Location", items.getLocation());
        user.put("Price", items.getPrice());
        user.put("Phone Number", items.getPhoneNumber());
        user.put("Description", items.getDescription());
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        user.put("Date", formattedDate);
        user.put("Image Url", items.getImage());

        FirebaseFirestore.getInstance().collection("Users").document(MainActivity.SaveSharedPreference.getPhoneNumber(context))
                .collection("Favorite").document(items.getId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "تمت إضافة المنتج إلى المفضلة", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "حدث خطأ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void DeleteFavorite(String id) {
        FirebaseFirestore.getInstance().collection("Users").document(MainActivity.SaveSharedPreference.getPhoneNumber(context))
                .collection("Favorite").document(id).delete();
        Toast.makeText(context, "تمت إزالة المنتج من المفضلة", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class DeleteItemsAdapterHolder extends RecyclerView.ViewHolder {

        ConstraintLayout constraintLayout;
        ImageView Image;
        TextView Name, Price, Rating;
        ImageView NotFavoriteImageView, FavoriteImageView;

        public DeleteItemsAdapterHolder(@NonNull View itemView) {
            super(itemView);
            Image = itemView.findViewById(R.id.ItemImageView);
            Name = itemView.findViewById(R.id.ItemTextView);
            Price = itemView.findViewById(R.id.PriceTextView);
            Rating = itemView.findViewById(R.id.RatingTextViewStore);
            NotFavoriteImageView = itemView.findViewById(R.id.NotFavoriteImageView);
            FavoriteImageView = itemView.findViewById(R.id.FavoriteImageView);
            constraintLayout = itemView.findViewById(R.id.store_constraint_layout);
        }
    }

}
