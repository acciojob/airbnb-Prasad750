package com.driver.repository;


import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class HotelManagementRepository {

    HashMap<String, Hotel> hotelDb;
    HashMap<Integer, User> userDb;
    HashMap<String, Booking> bookingDB;

    public HotelManagementRepository()
    {
        hotelDb=new HashMap<>();
        userDb=new HashMap<>();
        bookingDB=new HashMap<>();



    }

    public String addHotel(Hotel hotel) {

        if(Objects.isNull(hotel) || hotel.getHotelName()==null)
        {
            return "FAILURE";
        }

        String hotelName=hotel.getHotelName();
        if(hotelDb.containsKey(hotelName))
        {
            return "FAILURE";
        }


        hotelDb.put(hotelName,hotel);
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

        for(String hotel:hotelDb.keySet())
        {
            if(max<hotelDb.get(hotel).getFacilities().size())
            {
                max=hotelDb.get(hotel).getFacilities().size();
                hotelName=hotel;
            }
            else if(max==hotelDb.get(hotel).getFacilities().size() && max!=0)
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

        String id= UUID.randomUUID().toString();
        booking.setBookingId(id);
        bookingDB.put(id,booking);

        Hotel hotel=hotelDb.get(booking.getHotelName());

        if(hotel.getAvailableRooms()<booking.getNoOfRooms())
        {
            return -1;
        }
        hotel.setAvailableRooms(hotel.getAvailableRooms()-booking.getNoOfRooms());
        int totalAmount= hotel.getPricePerNight() * booking.getNoOfRooms();
        booking.setAmountToBePaid(totalAmount);

        return totalAmount;

    }

    public int getBookings(Integer aadharCard) {
        int ans=0;
        for(String id:bookingDB.keySet())
        {
            if(bookingDB.get(id).getBookingAadharCard()==aadharCard)
            {
                ans+=bookingDB.get(id).getNoOfRooms();
            }
        }
        return ans;
    }


    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {

        List<Facility> currFacilitiesList=hotelDb.get(hotelName).getFacilities();

        for (Facility f : newFacilities)
        {
            if(!currFacilitiesList.contains(f))
            {
                currFacilitiesList.add(f);
            }
        }

        hotelDb.get(hotelName).setFacilities(currFacilitiesList);

        return hotelDb.get(hotelName);
    }
}
