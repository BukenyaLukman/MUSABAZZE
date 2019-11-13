package com.mobilesoftware.musabazze;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class CompanyTravelDetailsActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private EditText PointFrom, DestinationPoint,DriverName,NumberPlate,PriceOfJourney;
    private CircleImageView DriverImage;
    private Button TimeOfDepartureBtn, ConfirmBtn;
    private static final int GalleryPick = 1;
    private Uri imageUri;
    private TextView DisplayTime;
    private String Departure,travelKey,saveCurrentDate, saveCurrentTime,Destination, NameOfDriver,
            VehicleNumberPlate,JourneyPrice,TimeOfDepart,downloadImageUrl;
    private ProgressDialog loadingBar;
    private StorageReference DriverImageRef;
    private DatabaseReference BusTravelTrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_travel_details);

       /* String apiKey  = "AIzaSyC5b87GW1jzdGMnCUUeXpMEsMK50kVVaRU";*/
        BusTravelTrip = FirebaseDatabase.getInstance().getReference().child("BusTravelTripDetails");
        DriverImageRef = FirebaseStorage.getInstance().getReference().child("Driver Images");




        initializeFields();
        DriverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        ConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateInfo();
            }
        });

        TimeOfDepartureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timepicker = new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(),"time picker");
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        DisplayTime.setText(hourOfDay+":"+minute);
    }

    private void ValidateInfo() {
       Departure = PointFrom.getText().toString();
       Destination = DestinationPoint.getText().toString();
       NameOfDriver = DriverName.getText().toString();
       VehicleNumberPlate = NumberPlate.toString();
       JourneyPrice = PriceOfJourney.getText().toString();
       TimeOfDepart = DisplayTime.getText().toString();

       if (imageUri == null){
           Toast.makeText(this, "Please Enter Driver's Picture", Toast.LENGTH_SHORT).show();
       }else if (TextUtils.isEmpty(Departure)){
           Toast.makeText(this, "Please provide the point of Departure of the bus", Toast.LENGTH_SHORT).show();
       }else if(TextUtils.isEmpty(Destination)){
           Toast.makeText(this, "Please provide the Destination", Toast.LENGTH_SHORT).show();
       }else if(TextUtils.isEmpty(NameOfDriver)){
           Toast.makeText(this, "Please provide the Driver's Name", Toast.LENGTH_SHORT).show();
       }else if(TextUtils.isEmpty(VehicleNumberPlate)){
           Toast.makeText(this, "Please Enter the Vehicle Number Plate", Toast.LENGTH_SHORT).show();
       }else if(TextUtils.isEmpty(JourneyPrice)){
           Toast.makeText(this, "Please provide the price of the Journey", Toast.LENGTH_SHORT).show();
       }else if(TextUtils.isEmpty(TimeOfDepart)){
           Toast.makeText(this, "Please set Time of Departure", Toast.LENGTH_SHORT).show();
       }else{
           StoreProductInfo();
       }
    }

    private void StoreProductInfo() {
       loadingBar.setTitle("Saving...");
       loadingBar.setMessage("Wait...");
       loadingBar.setCanceledOnTouchOutside(false);
       loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        travelKey = saveCurrentDate + saveCurrentTime;
        
        final StorageReference filePath =  DriverImageRef.child(imageUri.getLastPathSegment() + saveCurrentDate + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imageUri);
        
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               String message  = e.toString();
                Toast.makeText(CompanyTravelDetailsActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CompanyTravelDetailsActivity.this, "Driver Image Uploaded Successfully..", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(CompanyTravelDetailsActivity.this, "Image Url obtained Successfully..", Toast.LENGTH_SHORT).show();

                            saveTravelInfoToDatabase();
                        }
                    }
                });
            }
        });

    }

    private void saveTravelInfoToDatabase() {
        HashMap<String,Object> travelMap = new HashMap<>();
        travelMap.put("Pid",travelKey);
        travelMap.put("Destination",Departure);
        travelMap.put("NameOfDriver",NameOfDriver);
        travelMap.put("VehicleNumberPlate",VehicleNumberPlate);
        travelMap.put("JourneyPrice",JourneyPrice);
        travelMap.put("TimeOfDepart",TimeOfDepart);
        travelMap.put("Departure",Departure);
        travelMap.put("Image",downloadImageUrl);
        travelMap.put("Date",saveCurrentDate);

        BusTravelTrip.child(travelKey).updateChildren(travelMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           Intent intent = new Intent(CompanyTravelDetailsActivity.this, SetTravelRoutesActivity.class);
                           startActivity(intent);

                           loadingBar.dismiss();
                           Toast.makeText(CompanyTravelDetailsActivity.this, "Journey Added Successfully...", Toast.LENGTH_SHORT).show();

                       }
                    }
                });

    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryPick && resultCode == RESULT_OK && data!=null){
            imageUri = data.getData();
            DriverImage.setImageURI(imageUri);
        }
    }

    private void initializeFields() {
        PointFrom = findViewById(R.id.point_from);
        DestinationPoint = findViewById(R.id.point_to);
        DriverName = findViewById(R.id.name_of_driver);
        NumberPlate = findViewById(R.id.number_plate_company);
        PriceOfJourney = findViewById(R.id.price_of_journey);
        DriverImage = findViewById(R.id.driver_image);
        TimeOfDepartureBtn = findViewById(R.id.time_of_departure);
        ConfirmBtn = findViewById(R.id.confirm_journey_company);
        DisplayTime = findViewById(R.id.display_time_view);
        loadingBar = new ProgressDialog(this);
    }
}
