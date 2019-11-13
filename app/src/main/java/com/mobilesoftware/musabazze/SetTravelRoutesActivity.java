package com.mobilesoftware.musabazze;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SetTravelRoutesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference detailsRef;
    private String saveCurrentTime,saveCurrentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);

        detailsRef = FirebaseDatabase.getInstance().getReference().child("BusTravelTripDetails");
        recyclerView = findViewById(R.id.vehicles_details_list_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<CompanyTransitState> options =
                new FirebaseRecyclerOptions.Builder<CompanyTransitState>()
                        .setQuery(detailsRef,CompanyTransitState.class)
                        .build();

        FirebaseRecyclerAdapter<CompanyTransitState,TravelViewHolder> adapter =
                new FirebaseRecyclerAdapter<CompanyTransitState, TravelViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull TravelViewHolder travelViewHolder, int i, @NonNull CompanyTransitState companyTransitState) {
                        travelViewHolder.Departure.setText(companyTransitState.getDeparture());
                        travelViewHolder.Destination.setText(companyTransitState.getDestination());
                        travelViewHolder.driverName.setText(companyTransitState.getNameOfDriver());
                        travelViewHolder.Price.setText(companyTransitState.getJourneyPrice());
                        travelViewHolder.NumberPlate.setText(companyTransitState.getVehicleNumberPlate());

                        Calendar calendar = Calendar.getInstance();

                        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
                        saveCurrentDate = currentDate.format(calendar.getTime());

                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                        saveCurrentTime = currentTime.format(calendar.getTime());

                        if (saveCurrentTime == companyTransitState.getTimeOfDepart() || saveCurrentDate == companyTransitState.getDate()){
                            travelViewHolder.Status.setText("Motion");
                        }else{
                            travelViewHolder.Status.setText("Loading");
                        }
                    }

                    @NonNull
                    @Override
                    public TravelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_vehice_status,parent,false);
                        return new TravelViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class TravelViewHolder extends RecyclerView.ViewHolder{
        private TextView driverName,Price,Destination,Departure,NumberPlate,Status;
        public TravelViewHolder(@NonNull View itemView) {
            super(itemView);
            driverName = itemView.findViewById(R.id.drivers_name_display);
            Destination = itemView.findViewById(R.id.destination_point);
            Departure = itemView.findViewById(R.id.point_of_departure);
            NumberPlate = itemView.findViewById(R.id.number_plate_display);
            Status = itemView.findViewById(R.id.status_of_journey);
            Price = itemView.findViewById(R.id.price_of_journey_display);

        }
    }
}
