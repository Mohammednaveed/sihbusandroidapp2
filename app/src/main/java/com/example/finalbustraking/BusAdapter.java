package com.example.finalbustraking;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {
    private List<Bus> busList = new ArrayList<>();
    private Context context; // Added context for starting the tracking activity

    public void setBusList(List<Bus> busList) {
        this.busList = busList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext(); // Initialize the context
        View view = LayoutInflater.from(context).inflate(R.layout.search_bus, parent, false);
        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        Bus bus = busList.get(position);
        holder.bind(bus);
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public class BusViewHolder extends RecyclerView.ViewHolder {
        private TextView busNumberTextView;
        private TextView startTimeTextView;
        private TextView endTimeTextView;
        private TextView busNameTextView;
        private RelativeLayout myRelativeLayout;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            busNumberTextView = itemView.findViewById(R.id.busnumber);
            startTimeTextView = itemView.findViewById(R.id.starttime);
            endTimeTextView = itemView.findViewById(R.id.endtime);
            busNameTextView = itemView.findViewById(R.id.busname);
            myRelativeLayout = itemView.findViewById(R.id.busclick);
        }

        public void bind(Bus bus) {
            busNumberTextView.setText(bus.getbusnumber());
            startTimeTextView.setText(bus.getStartTime());
            endTimeTextView.setText(bus.getendTime());
            busNameTextView.setText(bus.getbusName());
            myRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start the tracking activity and pass the bus number
                    Intent intent = new Intent(context,tracking.class);
                    intent.putExtra("busnumber", bus.getbusnumber());
                    intent.putExtra("busname", bus.getbusName());
                    intent.putExtra("starttime",bus.getStartTime());
                    intent.putExtra("endtime",bus.getendTime());
                    intent.putExtra("source",bus.getsource());
                    intent.putExtra("destination",bus.getdestination());



                    context.startActivity(intent);



                }
            });
        }
    }
}
