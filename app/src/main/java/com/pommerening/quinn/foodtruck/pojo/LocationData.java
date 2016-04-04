package com.pommerening.quinn.foodtruck.pojo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Quinn Pommerening on 3/31/2016.
 */
public class LocationData {
    private static ArrayList<HashMap<String, String>> locationData;
    private static double distance = 0;
    private static ArrayList<String> truckNames;
    private static ArrayList<HashMap<String, String>> ticketOrder;

    public static ArrayList<HashMap<String, String>> getLocationData() {
        return locationData;
    }

    public static void setLocationData(ArrayList<HashMap<String, String>> locationData) {
        LocationData.locationData = locationData;
    }

    public static double getDistance() {
        return distance;
    }

    public static void setDistance(double distance) {
        LocationData.distance = distance;
    }

    public static ArrayList<String> getTruckNames() {
        return truckNames;
    }

    public static void setTruckNames(ArrayList<String> truckNames) {
        LocationData.truckNames = truckNames;
    }

    public static ArrayList<HashMap<String, String>> getTicketOrder() {
        return ticketOrder;
    }

    public static void setTicketOrder(ArrayList<HashMap<String, String>> ticketOrder) {
        LocationData.ticketOrder = ticketOrder;
    }
}
