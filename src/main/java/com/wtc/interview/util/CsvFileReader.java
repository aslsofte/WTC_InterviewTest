package com.wtc.interview.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.wtc.interview.entity.Person;
import com.wtc.interview.graph.People;

public class CsvFileReader {
	
	private static CSVReader reader;
	public final static String FAMILY = "FAMILY"; 
	public static final String FRIENDS = "FRIENDS";
	public final static String PATH_TO_CSV_PEOPLE = "src/test/resources/people.csv"; 
	public static final String PATH_TO_CSV_RELATIONSHIPS = "src/test/resources/relationships.csv";

	
	// This Application makes use of OpenCSV library to Read Csv files
	public static void readFromCsv() {
		try {
			readPeople();
			readRelationships();
		} catch (IOException | CsvException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Read from people.csv and initialise People Map
	 * @throws IOException
	 * @throws CsvException
	 */
	private static void readPeople() throws IOException, CsvException {
		reader = new CSVReader(new FileReader(PATH_TO_CSV_PEOPLE));
		Map<String, Person> people = reader.readAll()
						   .stream()
						   .map(Person::new)
					           .collect(Collectors.toMap(Person::getEmail, Function.identity()));
		People.setMap(people);
	}
	
	/**
	 * Read from relationships.csv and add connections between people
	 * @throws IOException
	 * @throws CsvException
	 */
	private static void readRelationships() throws IOException, CsvException {
		reader = new CSVReader(new FileReader(PATH_TO_CSV_RELATIONSHIPS));
		reader.readAll().forEach(data -> {
			if (data.length == 3) {
				String email1 = data[0], relation = data[1], email2 = data[2];
				Person p1 = People.getPersonByEmail(email1), p2 = People.getPersonByEmail(email2);
				if (relation.equals(FAMILY))
					People.addToFamily(p1, p2);
				else
					People.addToFriends(p1, p2);
			}
		});
	}
}
