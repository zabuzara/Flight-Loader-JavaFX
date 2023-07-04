package com.ome.flight.model;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import javax.sound.sampled.LineUnavailableException;
import com.ome.flight.contoller.RootController;
import com.ome.flight.tools.Copyright;
import com.ome.flight.tools.JSON;
import com.ome.flight.tools.RelationalDatabases;
import com.ome.flight.tools.SignalMaker;

/**
 * FlightDataLoaderApp starts a request loop of the FlightData-API by https://api.lufthansa.com
 * @author Omid Malekzadeh Eshtajarani
 * @version 1.0.0
 */
@Copyright(year = 2022, holders = "Omid Malekzadeh Eshtajarani")
public class FlightLoader implements Runnable {
	static public enum SearchBy { ARRIVAL, DEPARTURE, BOTH }
	private final String CLIENT_ID = "rqesbmjgxkvnt2j2asyef6kf";
	private final String CLIENT_SECRET = "bwrrDcsHw3Gd424yHtZk";
	private final String API_URL = "https://api.lufthansa.com/v1/operations/";
	private final Set<Consumer<String>> eventListeners;
	private int intervalTime;
	private boolean debug;
	private boolean headless;
	private SearchBy searchingType;
	private String token = "";
	private String message = "";
	private LocalDateTime startLoadingTime;
	
	
	/**
	 * Initializes a new instance 
	 * @param headless
	 * @param debug
	 * @param interval
	 */
	public FlightLoader (final boolean headless, final boolean debug, final int interval, final SearchBy searchBy) {
		this.headless = headless;
		this.debug = debug;
		this.intervalTime = interval * 1000;
		this.searchingType = searchBy;
		this.eventListeners = new CopyOnWriteArraySet<>();
		final Thread loaderThread = new Thread(this, "flight-loader");
		loaderThread.setDaemon(true);
		loaderThread.start();
	}

	
	/**
	 * Runs the loader thread
	 */
	@Override
	public void run () {	
		this.startLoadingTime = LocalDateTime.now();
		while (true) {
			try {
				while (!RootController.isAuthenticated()) {
					try {
						Thread.sleep(100);
					} catch (final InterruptedException e) {
						// do nothing 
					}
				}
				try {
					Thread.sleep(1000);
				} catch (final InterruptedException e) {
					// do nothing 
				}
				while (!this.isConnectionAlive()) {
					if(!this.waitLoading()) break;
					try {
						Thread.sleep(2000);
					} catch (final InterruptedException e) {
						// do nothing 
					}
				}
				if (this.token.length() == 0) {	
					this.message = "connected, try to take a token";
					if (this.debug) System.out.println(this.message);
					if (!this.headless)
						for (final Consumer<String> eventListener : this.eventListeners())
							eventListener.accept("");
					if (this.newToken().length() != 0) {
						this.token = this.newToken();
						continue;
					}
				} else {				
					if (this.token.length() != 0) {
						if (this.debug) System.out.println(this.message);
						this.message = "connected, token received";
						if (!this.headless)
							for (final Consumer<String> eventListener : this.eventListeners())
								eventListener.accept("");
						try {
							Thread.sleep(1000);
						} catch (final InterruptedException e) {
							// do nothing 
						}
						if (this.debug) System.out.println(this.message);
						this.message = "stable internet connection";
						if (!this.headless)
							for (final Consumer<String> eventListener : this.eventListeners())
								eventListener.accept("");
					}
					for (final Airports civilAirport : Airports.getAll()){
						if (this.searchingType == SearchBy.BOTH)
							for (int typeIndex = 0; typeIndex < 2; typeIndex++)
								this.load(civilAirport, (typeIndex == 0 ? SearchBy.DEPARTURE : SearchBy.ARRIVAL));
						else
							this.load(civilAirport, this.searchingType);
						try {
							Thread.sleep(this.intervalTime);
						} catch (final InterruptedException e) {
							// do nothing 
						}
					}
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * Waits for internet connection
	 * @return true if program have connection, otherwise false
	 */
	private boolean waitLoading () {
		this.message = "no internet connection";
		if (this.debug) System.out.println(this.message);
		if (this.token.length() == 0) {
			this.message = "no internet connection, token failed";
			if (this.debug) System.out.println(this.message);
		}
		if (this.isConnectionAlive() && this.token.length() != 0) {
			this.message = "stable internet connection";
			if (this.debug) System.out.println(this.message);
		}
		if (!this.headless)
			for (final Consumer<String> eventListener : this.eventListeners())
				eventListener.accept("");
		if (!this.isConnectionAlive()) return true;
		else return false;
	}
	
	
	/**
	 * Checks if connection is alive
	 * @return connection is alive
	 */
	private boolean isConnectionAlive () {		
		final HttpClient client = HttpClient.newHttpClient();
		final HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("https://dns.google/"))
			.header("cache-control", "no-cache")
			.header("Accept", "application/json").build();
		HttpResponse<String> response = null;
		try {
			response = client.send(request, BodyHandlers.ofString());
			return response.statusCode() == 200;
		} catch (final IOException | InterruptedException e) {
//			e.printStackTrace();
		}
		return false;
	}


	/**
	 * Returns the new token from API
	 * @return the new token from API
	 */
	@SuppressWarnings("unchecked")
	private String newToken () {	
		final Map<Object,Object> postData = new HashMap<>();
		postData.put("client_id", CLIENT_ID);
		postData.put("client_secret", CLIENT_SECRET);
		postData.put("grant_type", "client_credentials");
		final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
		final HttpRequest request = HttpRequest.newBuilder().POST(bodyPublisher(postData)).uri(URI.create("https://api.lufthansa.com/v1/oauth/token"))
			.setHeader("User-Agent", "Automatic Flight-Data Loader (Version: 1.0.0)")
			.header("Content-Type", "application/x-www-form-urlencoded").build();
		HttpResponse<String> response = null;
		try {
			response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (final IOException |InterruptedException e) {
			// do nothing 
		}
		if (response != null && response.statusCode() >= 300) throw new IllegalStateException(Objects.toString(response));
		String tokenString = "";
		if (response != null && ((Map<String,Object>) JSON.parse(response.body())).containsKey("access_token")) tokenString = Objects.toString(((Map<String,Object>) JSON.parse(response.body())).get("access_token"), "unknown_token");
		return tokenString;
	}


	/**
	 * Loads flight data from Flight-API
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String load (final Airports airport, final SearchBy searchBy) throws IOException {		
		final Airports civilAirport = airport;
		String flightData = "";
		switch (searchBy) {
			case ARRIVAL: flightData = this.getFlightStatusAtArrivalAirport(civilAirport.iata()); break;
		 	case DEPARTURE: flightData = this.getFlightStatusAtDepartureAirport(civilAirport.iata()); break;				
			default: break;
		}
		String output = "";
		if (flightData != null) {
			final Map<String,Object> flightMap = JSON.parse(flightData);			
			final Map<String,Object> flightStatusResource = (Map) flightMap.get("FlightStatusResource");
			final Map<String,Object> metaData = (Map) flightStatusResource.get("Meta");
			output = flightData == null ? "{\""+searchBy+"\":\""+airport.iata()+"\",\"City\":\""+airport.city()+"\", \"Result\":\"without flight\"}" : "{\""+searchBy+"\":\""+airport.iata()+"\",\"City\":\""+airport.city()+"\",\"ResultCount(found flights)\":"+(Objects.toString(metaData.get("TotalCount")))+"}";
			if (!this.headless)
				for (final Consumer<String> eventListener : this.eventListeners())
					eventListener.accept(Objects.toString(output));	
		}
		if (flightData != null && !flightData.isEmpty()) {
			this.initializeFlight(civilAirport, flightData);
		}
		return Objects.toString(output);
	}
	

	/**
	 * Returns the airportMap (airportCity, airportName)
	 * @param iataCode
	 * @return the airportMap (airportCity, airportName)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Map<String,String> getAirportMap (final String iataCode) {
		final String url = "https://api.lufthansa.com/v1/mds-references/airports/"+iataCode+"?limit=1&offset=0&LHoperated=0";
		final Map<String,String> output = new LinkedHashMap<>();
		final Map<String,Object> responseMap = this.getResponse(url);
		if (responseMap.containsKey("response") && responseMap.containsKey("error") && !Boolean.parseBoolean(Objects.toString(responseMap.get("error")))) {
			final HttpResponse<String> response = (HttpResponse<String>)responseMap.get("response");
			final Map<String,Object> airportInformation = JSON.parse(response.body());
			final Map<String,Object> airportRecourses = (Map) airportInformation.get("AirportResource");
			final Map<String,Object> airportMaps = (Map) airportRecourses.get("Airports");
			final Map<String,Object> airportMap = (Map) airportMaps.get("Airport");
			final Map<String,Object>  airportNames = (Map) airportMap.get("Names");
			final Object[]  airportNameArray = (Object[]) airportNames.get("Name");
			String cityName = "";
			for (final Object obj: airportNameArray) {
				Map<String,Object> objMap = (Map) obj;
				if (objMap.get("@LanguageCode").equals("DE"))
					cityName = Objects.toString(objMap.get("$"));
			}
			final String  airportName = cityName + " Airport";
			final String  countryCode = Objects.toString(airportMap.get("CountryCode"));
			output.put("cityName", cityName);
			output.put("airportName", airportName);
			output.put("countryCode", countryCode);
		} else {
			output.put("cityName", "unknown");
			output.put("airportName", "unknown");
			output.put("countryCode", "unknown");
		}
		if (this.debug) System.out.println("\n" + output);
		try {
			Thread.sleep(1000);
		} catch (final InterruptedException e) {
			// do nothing 
		}
		return output;
	}



	/**
	 * Returns the flight information at arrival airport as a JSON string
	 * @param destination
	 * @return the flight information at arrival airport as a JSON string
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private String getFlightStatusAtArrivalAirport (final String destination) throws IOException {
		final String dateTimeFormatedString = Objects.toString(this.startLoadingTime)
			.substring(0, Objects.toString(this.startLoadingTime).lastIndexOf(":"));
		final String url = API_URL + "flightstatus/arrivals/" + destination + "/" + dateTimeFormatedString + "?serviceType=all&limit=100";
		final Map<String,Object> responseMap = this.getResponse(url);
		return ((boolean) responseMap.get("error")) ? null : ((HttpResponse<String>) responseMap.get("response")).body();
	}


	/**
	 * Returns the flight information at departure airport as a JSON string
	 * @param origin
	 * @return the flight information at departure airport as a JSON string
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private String getFlightStatusAtDepartureAirport (final String origin) throws IOException {
		final String dateTimeFormatedString = Objects.toString(this.startLoadingTime)
			.substring(0, Objects.toString(this.startLoadingTime).lastIndexOf(":"));
		final String url = API_URL + "flightstatus/departures/" + origin + "/" + dateTimeFormatedString + "?serviceType=all&limit=100";
		final Map<String,Object> responseMap = this.getResponse(url);
		return ((boolean) responseMap.get("error")) ? null : ((HttpResponse<String>) responseMap.get("response")).body();
	}


	/**
	 * Returns the response text from API
	 * @param url
	 * @return the response text from API
	 */
	private Map<String,Object> getResponse (final String url) {
		while (!this.isConnectionAlive()) {
			if(!this.waitLoading()) break;
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				// do nothing 
			}
		}
		final Map<String,Object> responseMap = new HashMap<>();
		final HttpClient client = HttpClient.newHttpClient();
		final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
			.header("cache-control", "no-cache")
			.header("Accept", "application/json")
			.header("Authorization", "Bearer " + this.token).build();
		boolean responseFailed = false;
		HttpResponse<String> response = null;
		try {
			response = client.send(request, BodyHandlers.ofString());
			if (response.statusCode() >= 300)
				responseFailed = true;
		} catch (final IOException | InterruptedException e) {
			// do nothing 
			responseFailed = true;
		}
		responseMap.put("error", responseFailed);
		responseMap.put("response", response);
		return responseMap;
	}


	/**
	 * Returns the post body parameters
	 * @param postData
	 * @return the post body parameters
	 */
	private HttpRequest.BodyPublisher bodyPublisher (final Map<Object,Object> postData) {
		final StringBuilder queryBuilder = new StringBuilder();
		for (Map.Entry<Object,Object> postEntry : postData.entrySet()) {
			if (queryBuilder.length() > 0) queryBuilder.append('&');
			final String paramKey = URLEncoder.encode(Objects.toString(postEntry.getKey()), StandardCharsets.UTF_8);
			final String paramValue = URLEncoder.encode(Objects.toString(postEntry.getValue()), StandardCharsets.UTF_8);
			queryBuilder.append(paramKey).append('=').append(paramValue);
		}
		return HttpRequest.BodyPublishers.ofString(queryBuilder.toString());
	}


	/**
	 * Initialize the new Flight instances
	 * @param flightsJsonString
	 * @throw IllegalStateException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initializeFlight (final Airports airport, final String flightsJsonString) throws IllegalStateException {	
		final Map<String,Object> flightMap = JSON.parse(flightsJsonString);
		if (!(flightMap.containsKey("FlightStatusResource") | flightMap.containsKey("Flights") | flightMap.containsKey("Flight")))
			throw new IllegalStateException("invalid flight information");
		final Map<String,Object> flightStatusResource = (Map) flightMap.get("FlightStatusResource");
		final Map<String,Object> flights = (Map) flightStatusResource.get("Flights");
		final Object[] flightArray = (Object[]) flights.get("Flight");
		int rowCount = 0;
		if (flightArray.length > 0) {
			for (final Object flightObject : flightArray) {	
				if (this.debug) System.out.println("Flights count: " + flightArray.length);
				if (this.debug) System.out.println(flightObject);
				final Map<String,Object> flightObjectMap = (Map) flightObject;
				final Flight flight = new Flight(flightObjectMap);
				if (flight.getDeparture().getAirport().getName().equals("unknown")) {
					final Map<String,String> answerMap = this.getAirportMap(flight.getDeparture().getAirport().getIata());
					flight.getDeparture().getAirport().setCity(answerMap.get("cityName"));
					flight.getDeparture().getAirport().setName(answerMap.get("airportName"));
				}
				if (flight.getArrival().getAirport().getName().equals("unknown")) {
					final Map<String,String> answerMap = this.getAirportMap(flight.getArrival().getAirport().getIata());
					flight.getArrival().getAirport().setCity(answerMap.get("cityName"));
					flight.getArrival().getAirport().setName(answerMap.get("airportName"));
				}		
				try {
					while (RootController.getConnection() == null) {
						try {
							Thread.sleep(1000);
						} catch (final InterruptedException e) {
							// do nothing 
						}
					}
					if (RootController.getConnection() != null) {
						final Object result = RelationalDatabases.executeProcedure(
							RootController.getConnection(), 
							"flight",
							"PROCESS_FLIGHT",
							flight.getEquipment().getAircraftCode(), 
							flight.getEquipment().getAircraftRegistration(),
							flight.getAirlineId(),
							flight.getFlightNumber(), 
							flight.getServiceType(),
							flight.getFlightStatus().getStatusCode(), 
							flight.getFlightStatus().getStatusDefinition(),
							flight.getDeparture().getAirport().getUsage().name(), 
							flight.getDeparture().getAirport().getIata(),
							flight.getDeparture().getAirport().getIcao(), 
							flight.getDeparture().getAirport().getCity(), 
							flight.getDeparture().getAirport().getState(), 
							flight.getDeparture().getAirport().getName(),
							flight.getArrival().getAirport().getUsage().name(), 
							flight.getArrival().getAirport().getIata(),
							flight.getArrival().getAirport().getIcao(), 
							flight.getArrival().getAirport().getCity(), 
							flight.getArrival().getAirport().getState(), 
							flight.getArrival().getAirport().getName(),
							flight.getDeparture().getAirport().getTerminal().getGate().getName(), 
							flight.getDeparture().getAirport().getTerminal().getName(),
							flight.getDeparture().getScheduledTimeLocal(), 
							flight.getDeparture().getScheduledTimeUTC(), 
							flight.getDeparture().getEstimatedTimeLocal(), 
							flight.getDeparture().getEstimatedTimeUTC(), 
							flight.getDeparture().getActualTimeLocal(), 
							flight.getDeparture().getActualTimeUTC(),
							flight.getArrival().getAirport().getTerminal().getGate().getName(), 
							flight.getArrival().getAirport().getTerminal().getName(),
							flight.getArrival().getScheduledTimeLocal(), 
							flight.getArrival().getScheduledTimeUTC(), 
							flight.getArrival().getEstimatedTimeLocal(),
							flight.getArrival().getEstimatedTimeUTC(), 
							flight.getArrival().getActualTimeLocal(), 
							flight.getArrival().getActualTimeUTC(),
							flight.getDeparture().getTimeStatus().getStatusCode(), 
							flight.getDeparture().getTimeStatus().getStatusDefinition(),
							flight.getArrival().getTimeStatus().getStatusCode(), 
							flight.getArrival().getTimeStatus().getStatusDefinition()
						);
						if (this.debug) System.out.println("\n" + result + " ----> " + (++rowCount) + " / " + flightArray.length + "\n");
					}
				} catch (final SQLException e) {
					for(int freq = 400; freq < 20000; freq *= 9) {
				        try {
							SignalMaker.tone(freq,500);
						} catch (LineUnavailableException e1) {
							e1.printStackTrace();
						}
					}
					e.printStackTrace();
				}
				if (!(this.isConnectionAlive() || this.token.length() == 0)) {
					if (!this.headless)
						for (final Consumer<String> eventListener : this.eventListeners())
							eventListener.accept("");
				} else {
					this.message = "stable internet connection";
					if (!this.headless)
						for (final Consumer<String> eventListener : this.eventListeners())
							eventListener.accept("");
				}
				try {
					Thread.sleep(500);
				} catch (final InterruptedException e) {
					// do nothing 
				}
			}
		}
	}
	
	
	/**
	 * Returns the api url
	 * @return the api url
	 */
	public String getAPI_URL () {
		return this.API_URL;
	}
	
	
	/**
	 * Returns the interval time
	 * @return the interval time
	 */
	public int getIntervalTime () {
		return this.intervalTime;
	}


	/**
	 * Sets the interval time
	 * @param intervalTime
	 */
	public void setIntervalTime (final int intervalTime) {
		this.intervalTime = intervalTime * 1000;
	}


	/**
	 * Returns the searching type
	 * @return the searching type
	 */
	public SearchBy getSearchingType () {
		return this.searchingType;
	}


	/**
	 * Sets the searching type
	 * @param searchBy
	 */
	public void setSearchingType (final SearchBy searchBy) {
		this.searchingType = searchBy;
	}
	

	/**
	 * Returns the token string
	 * @return the token string
	 */
	public String getToken () {
		return this.token;
	}
	

	/**
	 * Returns the event listeners.
	 * @return the event listeners as a modifiable collection
	 */
	public Set<Consumer<String>> eventListeners () {
		return this.eventListeners;
	}
	
	
	/**
	 * Returns the message
	 * @return the message
	 */
	public String getMessage () {
		return this.message;
	}
	
	
	/**
	 * Returns the startLoadingTime
	 * @return the startLoadingTime
	 */
	public LocalDateTime getStartLoadingTime () {
		return this.startLoadingTime;
	}
		
	
	/**
	 * Sets the debug
	 * @param debug
	 */
	public void setDebug (final boolean debug) {
		this.debug = true;
	}
	
	
	/**
	 * Sets the headless-mode
	 * @param headless
	 */
	public void setHeadless (final boolean value) {
		this.headless = true;
	}
}
