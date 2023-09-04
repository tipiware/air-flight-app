package com.ecommerce.server.dao;

import com.ecommerce.server.entity.Flights;
import com.ecommerce.server.entity.Tickets;

public interface FlightDaoInterface {

	boolean cancelFlight(Tickets bd);
	
	public Flights getFlightById(int flightId);
	
	public Tickets getBookingDetails(Integer bookindId);
	
	public void updateFlight(Flights fd);
	
}
