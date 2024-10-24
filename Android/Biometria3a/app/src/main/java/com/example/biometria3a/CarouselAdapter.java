package com.example.biometria3a;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {

    private List<ItemCarosel> items;

    public CarouselAdapter(List<ItemCarosel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carousel, parent, false);
        return new CarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        ItemCarosel currentItem = items.get(position);
        holder.imageView.setImageResource(currentItem.getImageResId());
        holder.titleTextView.setText(currentItem.getTitle());
        holder.textView.setText(currentItem.getText());


        // Mostrar/Ocultar botones según el item
        if (position == items.size() - 1) { // Si es el último elemento
            holder.loginButton.setVisibility(View.VISIBLE);
            holder.registerButton.setVisibility(View.VISIBLE);
        } else { // Si no es el último elemento
            holder.loginButton.setVisibility(View.GONE);
            holder.registerButton.setVisibility(View.GONE);
        }
        // Configura los botones
        holder.loginButton.setOnClickListener(v -> {
            // Navegar a la actividad de Login
            // Puedes reemplazar LoginActivity.class con la clase correspondiente
            Intent intent = new Intent(holder.itemView.getContext(), LoginActivity.class);
            holder.itemView.getContext().startActivity(intent);
        });

        holder.registerButton.setOnClickListener(v -> {
            // Navegar a la actividad de Registro
            // Puedes reemplazar RegisterActivity.class con la clase correspondiente
            Intent intent = new Intent(holder.itemView.getContext(), RegistroActivity.class);
            holder.itemView.getContext().startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class CarouselViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        TextView titleTextView;
        Button loginButton;
        Button registerButton;

        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            loginButton = itemView.findViewById(R.id.loginButton);
            registerButton = itemView.findViewById(R.id.registerButton);
        }
    }
}