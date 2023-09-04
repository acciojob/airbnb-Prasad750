package com.driver.repository;


import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class HotelManagementRepository {

    HashMap<String, Hotel> hotelDb;
    HashMap<Integer, User> userDb;

    HashMap<String, List<Facility>> hotelFacilitiesDB;

    HashMap<String, Booking> bookingDB;

    HashMap<String,List<String>> hotelBookingDB;

    public HotelManagementRepository()
    {
        hotelDb=new HashMap<>();
        userDb=new HashMap<>();
        hotelFacilitiesDB=new HashMap<>();
        bookingDB=new HashMap<>();
        hotelBookingDB=new HashMap<>();


    }

    public String addHotel(Hotel hotel) {

        if(hotel==null) return "FAILURE";

        String hotelName=hotel.getHotelName();
        if(hotelDb.containsKey(hotelName)) return "FAILURE";


        hotelDb.put(hotelName,hotel);

        List<Facility> facilities=hotel.getFacilities();

        hotelFacilitiesDB.put(hotelName,facilities);

        return "SUCCESS";

    }

    public Integer addUser(User user) {
        int aadharCardNo=user.getaadharCardNo();
        userDb.put(aadharCardNo,user);
        return aadharCardNo;
    }

    public String getHotelWithMostFacilities() {
        String hotelName="";
        int max=0;

        for(String hotel:hotelFacilitiesDB.keySet())
        {
            if(max<hotelFacilitiesDB.get(hotel).size())
            {
                max=hotelFacilitiesDB.get(hotel).size();
                hotelName=hotel;
            }
            else if(max==hotelFacilitiesDB.get(hotel).size() && max!=0)
            {
                if (hotelName.compareTo(hotel)>0)
                {
                    hotelName=hotel;
                }
            }
        }

        return hotelName;
    }

    public int bookARoom(Booking booking) {

        bookingDB.put(booking.getBookingId(),booking);

        Hotel hotel=hotelDb.get(booking.getHotelName());

        if(hotel.getAvailableRooms()<booking.getNoOfRooms())
        {
            return -1;
        }
        hotel.setAvailableRooms(hotel.getAvailableRooms()-booking.getNoOfRooms());
        int totalAmount= hotel.getPricePerNight() * booking.getNoOfRooms();
        booking.setAmountToBePaid(totalAmount);

        List<String> bookingList=new ArrayList<>();

        if(hotelBookingDB.containsKey(booking.getHotelName()))
        {
            bookingList=hotelBookingDB.get(booking.getHotelName());
        }

        bookingList.add(booking.getBookingId());
        hotelBookingDB.put(booking.getHotelName(),bookingList);

        return totalAmount;

    }

    public int getBookings(Integer aadharCard) {
        int cnt=0;

        for(String bookingId:bookingDB.keySet())
        {
            if(aadharCard==bookingDB.get(bookingId).getBookingAadharCard())
            {
                cnt++;
            }
        }

        return cnt;
    }


    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {

        List<Facility> currFacilitiesList=hotelFacilitiesDB.get(hotelName);

        for (Facility f : newFacilities)
        {
            if(!currFacilitiesList.contains(f))
            {
                currFacilitiesList.add(f);
            }
        }

        hotelDb.get(hotelName).setFacilities(currFacilitiesList);
        hotelFacilitiesDB.put(hotelName,currFacilitiesList);

        return hotelDb.get(hotelName);
    }
}