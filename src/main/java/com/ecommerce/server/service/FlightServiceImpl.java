package com.ecommerce.server.service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ecommerce.server.dao.FlightDaoImpl;
import com.ecommerce.server.entity.Flights;
import com.ecommerce.server.entity.Tickets;
import com.ecommerce.server.entity.Users;
import com.ecommerce.server.exception.EntityNotFound;
import com.ecommerce.server.utility.GlobalResources;


/**
 * @author Sumeet Patil
 * This is Service Layer for Flight operatons
 *
 */
@Service
@Transactional
public class FlightServiceImpl implements FlightServiceInterface {

	@Autowired
	private FlightDaoImpl daoObject;
	@Autowired
	private EntityManager entityManager;

	private Logger logger = GlobalResources.getLogger(FlightServiceImpl.class);

	private static int bookingId;

	public FlightServiceImpl() {
	}

	/**
	 * This method searches flights for particular source and destination
	 * @param source : String
	 * @param destination : String
	 * @return  Flights
	 * 
	 */
	
	@Override
	public List<Flights> searchFlight(String source, String dest) throws EntityNotFound
	{

		String methodName = "searchFlight()";
		logger.info(methodName + " called");

		try {
			String qStr = "SELECT f FROM Flights f WHERE f.source=:psource and f.destination=:pdest";
			TypedQuery<Flights> query = entityManager.createQuery(qStr, Flights.class);
			query.setParameter("psource", source);
			query.setParameter("pdest", dest);
			List<Flights> flights = query.getResultList();

			return flights;
		} catch (Exception e) {
			 
			throw new EntityNotFound("No Flight Found from "+source+" to "+dest);
		}
	}

	/**
	 * This method books flights for given userid and flightid
	 * @param flightid : Integer
	 * @param userId : Integer
	 * @return status : Integer 
	 * 
	 */
	
	@Transactional
	@Override
	public Integer bookFlight(int flightid, Integer userid)throws EntityNotFound
	{

		String methodName = "bookFlight()";
		logger.info(methodName + " called");

		try {

			Integer temp = bookingId;
			Tickets bd = new Tickets();
			Flights fd = daoObject.getFlightById(flightid);

			if (fd.getAvailableSeats() > 0) {
				fd.setAvailableSeats(fd.getAvailableSeats() - 1);
				daoObject.updateFlight(fd);

				String command = "select count(bd.bookingId) from Tickets bd";
				TypedQuery<Long> query1 = entityManager.createQuery(command, Long.class);
				long count = query1.getSingleResult();
				if (count > 0) {
					command = "select max(bd.bookingId) from Tickets bd";
					TypedQuery<Integer> query2 = entityManager.createQuery(command, Integer.class);
					Integer bid = query2.getSingleResult();
					temp = bid + 1;
				} else {
					temp = 1000;
				}

				command = "select user from Users user where user.userId =: puserid";
				TypedQuery<Users> query2 = entityManager.createQuery(command, Users.class);
				query2.setParameter("puserid", userid);

				Users user = query2.getSingleResult();
				bd.setBookingId(temp);
				bd.setuser(user);
				bd.setFlightData(fd);

				if (daoObject.addBooking(bd)) {
					return temp;
				}

			} else {
				System.out.println("No booking available");
			}
			return 0;
		} catch (Exception e) {
			 
			return 0;

		}
	}

	/**
	 * This method cancels boooked flight for given booking Id
	 * @param flightid : Integer
	 * @param userId : Integer
	 * @return Response Entity : Integer 
	 * 
	 */
	@Override
	public boolean cancelFlight(Integer bookid)throws EntityNotFound
	{

		String methodName = "cancelFlight()";
		logger.info(methodName + " called");

		try {
			Tickets bd = daoObject.getBookingDetails(bookid);

			int availableSeats = bd.getFlightData().getAvailableSeats() + 1;
			bd.getFlightData().setAvailableSeats(availableSeats);

			return daoObject.cancelFlight(bd);
		} catch (Exception e) {
			throw new EntityNotFound("Cannot Canel Ticket ! No Tickect Found");
		}

	}

	/**
	 * This method returns all the booking for particular user
	 * @param userId : Integer
	 * @return Tickets : List<Tickets>
	 * 
	 */
	
	
	@Override
	public List<Tickets> viewTickets(int userid)throws EntityNotFound
	{

		String methodName = "viewTickets()";
		logger.info(methodName + " called");

		try {
			String qStr = "SELECT t FROM Tickets t WHERE userid=:uId";
			TypedQuery<Tickets> query = entityManager.createQuery(qStr, Tickets.class);
			query.setParameter("uId", userid);
			List<Tickets> tickets = query.getResultList();
			return tickets;

		} catch (Exception e) {
			throw new EntityNotFound("No Ticket Available");

		}

	}

	/**
	 * This method returns the single Booking given bookingId
	 * @param bookId : boolean 
	 * @return Tickets
	 * 
	 */
	
	@Override
	public Tickets getBooking(Integer bookId)throws EntityNotFound
	{

		String methodName = "getBooking()";
		logger.info(methodName + " called");

		try {

			return daoObject.getBookingDetails(bookId);

		} catch (Exception e) {
			throw new EntityNotFound("No booking found ..!");
		}

	}
}
