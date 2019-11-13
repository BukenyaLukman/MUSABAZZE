package com.mobilesoftware.musabazze;

public class CompanyTransitState {
    private String Destination,Departure,NameOfDriver,VehicleNumberPlate,JourneyPrice,TimeOfDepart,Image,Date;

    public CompanyTransitState() {
    }

    public CompanyTransitState(String destination, String departure, String nameOfDriver, String vehicleNumberPlate, String journeyPrice, String timeOfDepart, String image, String date) {
        Destination = destination;
        Departure = departure;
        NameOfDriver = nameOfDriver;
        VehicleNumberPlate = vehicleNumberPlate;
        JourneyPrice = journeyPrice;
        TimeOfDepart = timeOfDepart;
        Image = image;
        Date = date;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getDeparture() {
        return Departure;
    }

    public void setDeparture(String departure) {
        Departure = departure;
    }

    public String getNameOfDriver() {
        return NameOfDriver;
    }

    public void setNameOfDriver(String nameOfDriver) {
        NameOfDriver = nameOfDriver;
    }

    public String getVehicleNumberPlate() {
        return VehicleNumberPlate;
    }

    public void setVehicleNumberPlate(String vehicleNumberPlate) {
        VehicleNumberPlate = vehicleNumberPlate;
    }

    public String getJourneyPrice() {
        return JourneyPrice;
    }

    public void setJourneyPrice(String journeyPrice) {
        JourneyPrice = journeyPrice;
    }

    public String getTimeOfDepart() {
        return TimeOfDepart;
    }

    public void setTimeOfDepart(String timeOfDepart) {
        TimeOfDepart = timeOfDepart;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
