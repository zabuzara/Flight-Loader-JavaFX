package com.ome.flight.model;

import java.util.ArrayList;
import java.util.List;
import com.ome.flight.tools.Copyright;

/**
 * This enum includes all clivil airports,
 * that exists in Germany
 */
@Copyright(year = 2022, holders = "Omid Malekzadeh Eshtajarani")
public enum Airports {
	/**
	 * Reference
	 * {@link https://en.wikipedia.org/wiki/List_of_airports_in_Germany}
	 * 
	 * List of civil airports
	 */
	EDDB("CIVIL", "EDDB", "BER", "Berlin", "Brandenburg", "Berlin Brandenburg Airport"),
	EDDF("CIVIL", "EDDF", "FRA", "Frankfurt am Main", "Hesse", "Frankfurt Airport (Frankfurt am Main Airport, also: Rhein-Main Airport)"),
	EDDH("CIVIL", "EDDH", "HAM", "Hamburg / Fuhlsbüttel", "Hamburg", "Hamburg Airport (Hamburg-Fuhlsbüttel Airport)"),
	EDDM("CIVIL", "EDDM", "MUC", "Munich", "Bavaria", "Munich Airport"),
	EDKA("CIVIL", "EDKA", "AAH", "Aachen", "North Rhine-Westphalia", "Aachen-Merzbrück Airport"),
	EDFQ("CIVIL", "EDFQ",    "", "Allendorf", "Hesse", "Allendorf Airport"),
	EDAC("CIVIL", "EDAC", "AOC", "Altenburg", "Thuringia", "Altenburg-Nobitz Airport (formerly known as Leipzig-Altenburg Airport)"),
	EDMA("CIVIL", "EDMA", "AGB", "Augsburg", "Bavaria", "Augsburg Airport"),
	EDVA("CIVIL", "EDVA",    "", "Bad Gandersheim", "Lower Saxony", "Bad Gandersheim Aerodrome"),
	EDFK("CIVIL", "EDFK",    "", "Bad Kissingen", "Bavaria", "Bad Kissingen Airfield"),
	EDQA("CIVIL", "EDQA", "ZCD", "Bamberg", "Bavaria", "Bamberg-Breitenau Airfield (former Bamberg Army Airfield (US Army))"),
	EDBH("CIVIL", "EDBH", "BBH", "Barth / Stralsund", "Mecklenburg-Vorpommern", "Stralsund–Barth Airport"),
	EDQD("CIVIL", "EDQD", "BYU", "Bindlach / Bayreuth", "Bavaria", "Bayreuth Airport"),
	EDRB("CIVIL", "EDRB", "BBJ", "Bitburg", "Rhineland-Palatinate", "Bitburg Airport"),
	EDWR("CIVIL", "EDWR", "BMK", "Borkum", "Lower Saxony", "Borkum Airfield"),
	EDXA("CIVIL", "EDXA",    "", "Bramsche", "Lower Saxony", "Achmer Aerodrome"),
	EDVE("CIVIL", "EDVE", "BWE", "Braunschweig", "Lower Saxony", "Braunschweig Wolfsburg Airport"),
	EDDW("CIVIL", "EDDW", "BRE", "Bremen", "Free Hanseatic City of Bremen", "Bremen Airport"),
	EDDK("CIVIL", "EDDK", "CGN", "Cologne / Bonn", "North Rhine-Westphalia", "Cologne Bonn Airport"),
	EDLW("CIVIL", "EDLW", "DTM", "Dortmund", "North Rhine-Westphalia", "Dortmund Airport"),
	EDDC("CIVIL", "EDDC", "DRS", "Dresden / Klotzsche", "Saxony", "Dresden Airport (Dresden-Klotzsche Airport)"),
	EDDL("CIVIL", "EDDL", "DUS", "Düsseldorf", "North Rhine-Westphalia", "Düsseldorf Airport"),
	EDLN("CIVIL", "EDLN", "MGL", "Düsseldorf / Mönchengladbach", "North Rhine-Westphalia", "Mönchengladbach Airport"),
	EDFE("CIVIL", "EDFE",    "", "Egelsbach", "Hesse", "Frankfurt Egelsbach Airport"),
	EDWE("CIVIL", "EDWE", "EME", "Emdan", "Lower Saxony", "Emden Airport"),
	EDDE("CIVIL", "EDDE", "ERF", "Erfurt", "Thuringia", "Erfurt-Weimar Airport"),
	EDTF("CIVIL", "EDTF", "QFB", "Freiburg im Breisgau", "Baden-Württemberg", "Freiburg Airport"),
	EDNY("CIVIL", "EDNY", "FDH", "Friedrichshafen", "Baden-Württemberg", "Friedrichshafen Airport (Bodensee Airport, Friedrichshafen)"),
	EDQG("CIVIL", "EDQG",    "", "Giebelstadt", "Bavaria", "Giebelstadt Airport / Giebelstadt Army Airfield"),
	EDFH("CIVIL", "EDFH", "HHN", "Hahn", "Rhineland-Palatinate", "Frankfurt-Hahn Airport"),
	EDHI("CIVIL", "EDHI", "XFW", "Hamburg / Finkenwerder", "Hamburg", "Airbus Hamburg-Finkenwerder"),
	EDDV("CIVIL", "EDDV", "HAJ", "Hannover", "Lower Saxony", "Hannover Airport"),
	EDXB("CIVIL", "EDXB", "HEI", "Heide", "Schleswig-Holstein", "Heide-Büsum Airport"),
	EDHE("CIVIL", "EDHE",    "", "Heist", "Schleswig-Holstein", "Uetersen Airfield"),
	EDXH("CIVIL", "EDXH", "HGL", "Heligoland / Düne", "Schleswig-Holstein", "Heligoland Airport (Helgoland-Düne Airport)"),
	EDAH("CIVIL", "EDAH", "HDF", "Heringsdorf", "Mecklenburg-Vorpommern", "Heringsdorf Airport"),
	EDQM("CIVIL", "EDQM", "HOQ", "Hof", "Bavaria", "Hof-Plauen Airport"),
	EDRH("CIVIL", "EDRH",    "", "Hoppstädten", "Rhineland-Palatinate", "Hoppstädten-Weiersbach Airfield"),
	ETSI("CIVIL", "ETSI", "IGS", "Ingolstadt", "Bavaria", "Ingolstadt Manching Airport"),
	EDSB("CIVIL", "EDSB", "FKB", "Karlsruhe / Baden-Baden", "Baden-Württemberg", "Karlsruhe/Baden-Baden Airport"),
	EDVK("CIVIL", "EDVK", "KSF", "Kassel", "Hesse", "Kassel Airport"),
	EDHK("CIVIL", "EDHK", "KEL", "Kiel", "Schleswig-Holstein", "Kiel Airport (Kiel Holtenau Airport)"),
	EDTL("CIVIL", "EDTL", "LHA", "Lahr", "Baden-Württemberg", "Flughafen Lahr"),
	EDEL("CIVIL", "EDEL",    "", "Langenlonsheim", "Rhineland-Palatinate", "Langenlonsheim Airfield"),
	EDDP("CIVIL", "EDDP", "LEJ", "Leipzig", "Saxony", "Leipzig/Halle Airport"),
	EDHL("CIVIL", "EDHL", "LBC", "Lübeck", "Schleswig-Holstein", "Lübeck Airport (Blankensee Airport)"),
	EDBC("CIVIL", "EDBC", "CSO", "Magdeburg", "Saxony-Anhalt", "Magdeburg-Cochstedt Airport"),
	EDFZ("CIVIL", "EDFZ",    "", "Mainz", "Rhineland-Palatinate", "Mainz-Finthen Airport"),
	EDFM("CIVIL", "EDFM", "MHG", "Mannheim", "Baden-Württemberg", "Mannheim City Airport"),
	EDJA("CIVIL", "EDJA", "FMM", "Memmingen / Allgäu", "Bavaria", "Memmingen Airport (former Allgäu Airport/Memmingen)"),
	EDDG("CIVIL", "EDDG", "FMO", "Münster / Osnabrück", "North Rhine-Westphalia", "Münster Osnabrück International Airport"),
	EDRN("CIVIL", "EDRN",    "", "Nannhausen / Simmern", "Rhineland-Palatinate", "Nannhausen Airfield"),
	EDBN("CIVIL", "EDBN", "FNB", "Neubrandenburg", "Mecklenburg-Vorpommern", "Neubrandenburg Airport"),
	EDON("CIVIL", "EDON",    "", "Neuhardenberg", "Brandenburg", "Neuhardenberg Airfield (former Marxwalde Air Base)"),
	EDDN("CIVIL", "EDDN", "NUE", "Nuremberg", "Bavaria", "Nuremberg Airport"),
	EDWO("CIVIL", "EDWO",    "", "Osnabrück", "Lower Saxony", "Atterheide Airfield"),
	EDLP("CIVIL", "EDLP", "PAD", "Paderborn / Lippstadt", "North Rhine-Westphalia", "Paderborn Lippstadt Airport"),
	EDAX("CIVIL", "EDAX", "REB", "Rechlin", "Mecklenburg-Vorpommern", "Rechlin-Lärz Airfield"),
	ETNL("CIVIL", "ETNL", "RLG", "Rostock", "Mecklenburg-Vorpommern", "Rostock-Laage Airport"),
	EDDR("CIVIL", "EDDR", "SCN", "Saarbrücken", "Saarland", "Saarbrücken Airport (Ensheim Airport)"),
	EDGS("CIVIL", "EDGS", "SGE", "Siegen", "North Rhine-Westphalia", "Siegerland Airport"),
	EDLS("CIVIL", "EDLS",    "", "Stadtlohn", "North Rhine-Westphalia", "Stadtlohn-Vreden Airport"),
	EDDS("CIVIL", "EDDS", "STR", "Stuttgart", "Baden-Württemberg", "Stuttgart Airport (was Stuttgart Army Airfield, Stuttgart Echterdingen Airport)"),
	EDRT("CIVIL", "EDRT",    "", "Trier / Föhren", "Rhineland-Palatinate", "Trier-Föhren Airfield"),
	EDWG("CIVIL", "EDWG", "AGE", "Wangerooge", "Lower Saxony", "Wangerooge Airfield"),
	EDLV("CIVIL", "EDLV", "NRN", "Weeze", "North Rhine-Westphalia", "Weeze Airport (Niederrhein Airport)"),
	EDXW("CIVIL", "EDXW", "GWT", "Westerland, Sylt", "Schleswig-Holstein", "Sylt Airport"),
	EDRZ("CIVIL", "EDRZ", "ZQW", "Zweibrücken", "Rhineland-Palatinate", "Zweibrücken Airport"),
	/**
	 * List of military airports
	 */
	ETHA("MILITARY", "ETHA",    "", "Altenstadt", "Bavaria", "Altenstadt Air Base (Army)"),
	ETEK("MILITARY", "ETEK",    "", "Baumholder", "Rhineland-Palatinate", "Baumholder Army Airfield (U.S. Army)"),
	ETSB("MILITARY", "ETSB",    "", "Büchel", "Rhineland-Palatinate", "Büchel Air Base (Air Force)"),
	ETHB("MILITARY", "ETHB",    "", "Bückeburg", "Lower Saxony", "Bückeburg Air Base (Army)"),
	ETHC("MILITARY", "ETHC", "ZCN", "Celle", "Lower Saxony", "Celle Air Base (Army)"),
	ETND("MILITARY", "ETND",    "", "Diepholz", "Lower Saxony", "Diepholz Air Base (Air Force)"),
	ETSE("MILITARY", "ETSE",   "", "Erding", "Bavaria", "Erding Air Base (Air Force)"),
	ETHS("MILITARY", "ETHS",   "", "Faßberg (Fassberg)", "Lower Saxony", "Faßberg Air Base (Army)"),
	ETHF("MILITARY", "ETHF", "FRZ", "Fritzlar", "Hesse", "Fritzlar Air Base (Army)"),
	ETSF("MILITARY", "ETSF", "FEL", "Fürstenfeldbruck", "Bavaria", "Fürstenfeldbruck Air Base (Air Force)"),
	ETNG("MILITARY", "ETNG", "GKE", "Geilenkirchen / Teveren", "North Rhine-Westphalia", "NATO Air Base Geilenkirchen (NATO)"),
	ETIC("MILITARY", "ETIC",    "", "Grafenwöhr", "Bavaria", "Grafenwöhr Army Airfield (U.S. Army)"),
	ETIH("MILITARY", "ETIH",    "", "Hohenfels", "Bavaria", "Hohenfels Army Airfield (U.S. Army)"),
	ETNH("MILITARY", "ETNH",    "", "Hohn", "Schleswig-Holstein", "Hohn Air Base (Air Force)"),
	ETSH("MILITARY", "ETSH",    "", "Holzdorf / Schönewalde", "Saxony-Anhalt / Brandenburg", "Holzdorf Air Base (Air Force)"),
	ETNJ("MILITARY", "ETNJ",    "", "Jever", "Lower Saxony", "Jever Air Base (Air Force)"),
	ETSA("MILITARY", "ETSA",    "", "Landsberg am Lech", "Bavaria", "Landsberg-Lech Air Base (Air Force)"),
	ETHL("MILITARY", "ETHL",    "", "Laupheim", "Baden-Württemberg", "Laupheim Air Base (Air Force)"),
	ETSL("MILITARY", "ETSL",    "", "Klosterlechfeld", "Bavaria", "Lechfeld Air Base (Air Force)"),
	ETOR("MILITARY", "ETOR",    "", "Mannheim", "Baden-Württemberg", "Coleman Army Airfield"),
	ETWM("MILITARY", "ETWM",    "", "Meppen", "Lower Saxony", "Meppen Air Base (Air Force)"),
	ETSN("MILITARY", "ETSN",    "", "Neuburg an der Donau", "Bavaria", "Neuburg Air Base (Air Force)"),
	ETHN("MILITARY", "ETHN",    "", "Niederstetten", "Baden-Württemberg", "Niederstetten Air Base (Army)"),
	ETMN("MILITARY", "ETMN", "NDZ", "Nordholz", "Lower Saxony", "Nordholz Air Base (Navy)"),
	ETNN("MILITARY", "ETNN", "QOE", "Nörvenich", "North Rhine-Westphalia", "Nörvenich Air Base (Air Force)"),
	ETAR("MILITARY", "ETAR", "RMS", "Ramstein-Miesenbach", "Rhineland-Palatinate", "Ramstein Air Base (U.S. Air Force)"),
	ETHE("MILITARY", "ETHE", "ZPQ", "Rheine", "North Rhine-Westphalia", "Rheine-Bentlage Air Base (Army)"),
	ETHR("MILITARY", "ETHR",    "", "Roth", "Bavaria", "Roth Air Base (Army)"),
	ETNS("MILITARY", "ETNS", "WBG", "Schleswig / Jagel", "Schleswig-Holstein", "Schleswig Air Base (Air Force)"),
	ETAD("MILITARY", "ETAD", "SPM", "Spangdahlem", "Rhineland-Palatinate", "Spangdahlem Air Base (U.S. Air Force)"),
	ETOU("MILITARY", "ETOU", "WIE", "Wiesbaden", "Hesse", "Wiesbaden Army Airfield (U.S. Army)"),
	ETNT("MILITARY", "ETNT",    "", "Wittmund", "Lower Saxony", "Wittmundhafen Air Base (Air Force)"),
	ETNW("MILITARY", "ETNW", "", "Wunstorf", "Lower Saxony", "Wunstorf Air Base (Air Force)");
	
	
	/**
	 * Usage : Military or Civil
	 */
	private final String usage;
	
	
	/**
	 * 4 letter ICAO code.
	 */
	private final String icao;
	
	
	/**
	 * 3 letter IATA airport code of the place of destination.
	 */
	private final String iata;
	
	
	/**
	 * The city of airport
	 */
	private final String city;
	
	
	/**
	 * The state of airport
	 */
	private final String state;
	
	
	/**
	 * The airport name
	 */
	private final String airportName;
	

	/**
	 * Initializes a new instance.
	 * @param usage
	 * @param icao
	 * @param iata
	 * @param city
	 * @param state
	 * @param airportName
	 */
	private Airports (final String usage, final String icao, final String iata, final String city, final String state, final String airportName) {
		this.usage = usage;
		this.icao = icao;
		this.iata = iata;
		this.city = city;
		this.state = state;
		this.airportName = airportName;
	}
	
	
	/**
	 * Returns the usage
	 * @return the usage
	 */
	public String usage () { 
		return this.usage;
	} 
	
	
	/**
	 * Returns the icao code
	 * @return the icao code
	 */
	public String icao () { 
		return this.icao;
	} 
	
	
	/**
	 * Returns the iata code
	 * @return the iata code
	 */
	public String iata () { 
		return this.iata;
	} 
	
	/**
	 * Returns the city
	 * @return the city
	 */
	public String city () { 
		return this.city;
	} 
	
	/**
	 * Returns the state
	 * @return the state
	 */
	public String state () { 
		return this.state;
	} 
	
	
	/**
	 * Returns the airport name
	 * @return the airport name
	 */
	public String airportName () {
		return this.airportName;  
	}
	
	
	/**
	 * Returns city name IATA-Code of given IATA-Code
	 * @param airportName
	 * @return city name IATA-Code of given IATA-Code
	 */
	static public String getCity (final String airportCode){
		for (final Airports instance : Airports.values())
			if (instance.iata().equals(airportCode)) 
				return instance.airportName();
		return null;
	}
	
	
	/**
	 * Returns airport IATA-Code of given city name
	 * @param airportName
	 * @return airport IATA-Code of given city name
	 */
	static public String getIATA (final String airportName){
		for (final Airports instance : Airports.values())
			if (instance.airportName().equals(airportName)) 
				return instance.iata();
		return null;
	}
	
	/**
	 * Returns the instance of Airports of given IATA
	 * @param iata
	 * @return the instance of Airports of given IATA
	 */
	static public Airports getAirport (final String iata){
		for (final Airports instance : Airports.values())
			if (instance.iata().equals(iata))
				return instance;
		return null;
	}
	
	/**
	 * Returns the all CivilAirports instnaces, that have a IATA-Code
	 * @return the all CivilAirports instnaces, that have a IATA-Code
	 */
	static public List<Airports> getAll () {
		final List<Airports> allCivilAirports = new ArrayList<>();
		for (final Airports instance : Airports.values())
			if (!instance.iata().isEmpty()) 
				allCivilAirports.add(instance);
		return allCivilAirports;
	}
}
