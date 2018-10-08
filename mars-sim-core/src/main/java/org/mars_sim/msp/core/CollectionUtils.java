/**
 * Mars Simulation Project
 * CollectionUtils.java
 * @version 3.1.0 2017-08-30
 * @author Sebastien Venot
 */
package org.mars_sim.msp.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import org.mars_sim.msp.core.equipment.Equipment;
import org.mars_sim.msp.core.person.Person;
import org.mars_sim.msp.core.robot.Robot;
import org.mars_sim.msp.core.structure.Settlement;
import org.mars_sim.msp.core.tool.RandomUtil;
import org.mars_sim.msp.core.vehicle.Vehicle;

/**
 * This class provides general collection manipulation convenience methods.
 */
public class CollectionUtils {

	public static Collection<Equipment> getEquipment(
		Collection<Unit> units
	) {
		return units
				.stream()
				.filter(u-> u instanceof Equipment)
				.map(u -> (Equipment) u)
				.filter(u-> !u.isSalvaged())
				.collect(Collectors.toList());

//		ConcurrentLinkedQueue<Equipment> equipment = new ConcurrentLinkedQueue<Equipment>();
//		for (Unit unit : units) {
//			if (unit instanceof Equipment) {
//				Equipment equipmentUnit = (Equipment) unit;
//				if (!equipmentUnit.isSalvaged())
//					equipment.add(equipmentUnit);
//			}
//		}
//		return equipment;

	}

	public synchronized static void mergeEquipments(Collection<Unit> units,
		Collection<Equipment> equipments
	) {
		for (Equipment equipmentUnit : equipments) {
			if (!units.contains(equipmentUnit))
				units.add(equipmentUnit);
		}
	}

	public static Collection<Vehicle> getVehicle(
		Collection<Unit> units
	) {
		return units
				.stream()
				.filter(u-> u instanceof Vehicle)
				.map(u -> (Vehicle) u)
				.filter(u-> !u.isSalvaged())
				.collect(Collectors.toList());

//		ConcurrentLinkedQueue<Vehicle> vehicles = new ConcurrentLinkedQueue<Vehicle>();
//		for (Unit unit : units) {
//			if (unit instanceof Vehicle) {
//				Vehicle vehicleUnit = (Vehicle) unit;
//				if (!vehicleUnit.isSalvaged()) vehicles.add(vehicleUnit);
//			}
//		}
//		return vehicles;

	}

	public synchronized static void mergeVehicles(Collection<Unit> units,
		Collection<Vehicle> vehicles
	) {
		for (Vehicle vehicleUnit : vehicles) {
			if (!units.contains(vehicleUnit))
				units.add(vehicleUnit);
		}
	}

	public static Collection<Robot> getRobot(
		Collection<Unit> units
	) {
		
//		return units
//				.stream()
//				.filter(u-> u instanceof Robot)
//				.map(u -> (Robot) u)
//				.collect(Collectors.toList());

		ConcurrentLinkedQueue<Robot> robots = new ConcurrentLinkedQueue<Robot>();
		for (Unit unit : units) {
			if (unit instanceof Robot)
				robots.add((Robot) unit);
		}
		return robots;

	}

	public static void mergeRobots(Collection<Unit> units,
		Collection<Robot> robots
	) {
		for (Robot robotUnit : robots) {
			if (!units.contains(robotUnit))
				units.add(robotUnit);
		}
	}


	public static Collection<Person> getPerson(
		Collection<Unit> units
	) {

		// StackOverflowError sometimes when using stream below
//		return units
//				.stream()
//				.filter(u-> u instanceof Person)
//				.map(u -> (Person) u)
//				.collect(Collectors.toList());

		ConcurrentLinkedQueue<Person> persons = new ConcurrentLinkedQueue<Person>();
		Iterator<Unit> i = units.iterator(); // switch to iterator to avoid concurrent modification exception
		while (i.hasNext()) {
			Unit unit = i.next();
			if (unit instanceof Person)
				persons.add((Person) unit);
		}
		return persons;

	}

	public synchronized static void mergePersons(Collection<Unit> units,
		Collection<Person> persons
	) {
		for (Person personUnit : persons) {
			if (!units.contains(personUnit))
				units.add(personUnit);
		}
	}

	public static Collection<Settlement> getSettlement(
		Collection<Unit> units
	) {

//		return units
//				.stream()
//				.filter(u-> u instanceof Settlement)
//				.map(u -> (Settlement) u)
//				.collect(Collectors.toList());
			

		ConcurrentLinkedQueue<Settlement> settlements = new ConcurrentLinkedQueue<Settlement>();
		for (Unit unit : units) {
			if (unit instanceof Settlement)
				settlements.add((Settlement) unit);
		}
		return settlements;

	}

	public synchronized static void mergeSettlements(Collection<Unit> units,
		Collection<Settlement> settlements
	) {
		for (Settlement settlementUnit : settlements) {

			if (!units.contains(settlementUnit))
				units.add(settlementUnit);
		}
	}

	public static Settlement getRandomSettlement(
		Collection<Settlement> collection
	) {
	    Settlement result = null;
		Object[] array = collection.toArray();
		if (collection.size() > 0) {
		    int r = RandomUtil.getRandomInt(collection.size() - 1);
		    result = (Settlement) array[r];
		}

		return result;
	}

	public static Settlement getRandomRegressionSettlement(
		Collection<Settlement> collection
	) {
		Settlement result = null;
		int size = collection.size();
		if (size > 0) {
			Object[] array = collection.toArray();
			int chosenSettlementNum = RandomUtil.getRandomRegressionInteger(size);
			result = (Settlement) array[chosenSettlementNum - 1];
		}

		return result;
	}

	public static Settlement getSettlement(
		Collection<Settlement> collection,
		String name
	) {

		Iterator<Settlement> i = collection.iterator();
		Settlement result = null;
		while (result == null && i.hasNext()) {
			Settlement settlement = i.next();
			if (name.equals(settlement.getName()))
				result = settlement;
		}

		return result;

//		return collection
//				.stream()
//				.filter(u-> name.equals(u.getName()))
//				.map(u -> (Settlement) u)
//				.findFirst().orElse(null);//.get();

	}

	public static <T extends Unit> Collection<T> sortByName(
		Collection<T> collection
	) {
		ConcurrentSkipListSet<T> sorted = new ConcurrentSkipListSet<T>(
			new Comparator<T>() {
				@Override
				public int compare(T o1, T o2) {
					return o1.getName().compareToIgnoreCase(o2.getName());
				}
			}
		);
		sorted.addAll(collection);
		return sorted;
	}

	public static <T extends Unit> Collection<T> sortByProximity(
		Collection<T> collection,
		final Coordinates location
	) {
		ConcurrentSkipListSet<T> sorted = new ConcurrentSkipListSet<T>(
			new Comparator<T>() {
				@Override
				public int compare(T o1, T o2) {
					return Double.compare(
							location.getDistance(o1.getCoordinates()),
							location.getDistance(o2.getCoordinates())
							);
				}
			}
		);
		sorted.addAll(collection);
		return sorted;
	}
	
	
	
	/**
	 * Compiles a list of shortcuts
	 * 
	 * @return a list of string
	 */
	public static List<String> createShortcutHelp() {
		List<String> list = new ArrayList<>();
		list.add("key");
		list.add("keys");
		list.add("keyword");
		list.add("keywords");
		list.add("/k");
		
		list.add("help");
		list.add("/h");
		list.add("/?");
		list.add("?");
		
		list.add("/p");
		
		list.add("/y1");
		list.add("/y2");
		list.add("/y3");
		list.add("/y4");

		list.add("hello");
		list.add("hi");
		list.add("hey");
		
		list.add("quit");
		list.add("bye");
		list.add("exit");
		
		list.add("/quit");
		list.add("/bye");
		list.add("/exit");

		list.add("/b");
		list.add("/q");
		list.add("/x");
		
		return list;
	}
	
	
	/**
	 * Compiles the names of settlements, people, robots and vehicles into one single list
	 * 
	 * @return a list of string
	 */
	public static List<String> createAutoCompleteData() {

		// Creates an array with the names of all of settlements
		Collection<Settlement> settlements = Simulation.instance().getUnitManager().getSettlements();
		List<Settlement> settlementList = new ArrayList<Settlement>(settlements);
//		System.out.println("# settlements : " + settlementList.size());
//		Collection<Settlement> settlements = getSettlement(Simulation.instance().getUnitManager().getUnits());

		// autoCompleteArray = settlementList.toArray(new
		// String[settlementList.size()]);
		// or with java 8 stream
		// autoCompleteArray = settlementList.stream().toArray(String[]::new);

		// Creates an array with the names of all of people and robots
		List<String> nameList = new ArrayList<>();

		nameList.add("settlement");
		nameList.add("settlements");
		
		// Questions regarding a settlement 
		nameList.add("people");
		nameList.add("settler");
		nameList.add("persons");
		
		nameList.add("robot");
		nameList.add("bot");

		nameList.add("bed");
		nameList.add("sleep");
		nameList.add("lodging");
		nameList.add("quarters");

		nameList.add("vehicle");
		nameList.add("rover");
		
		// Questions regarding a person/robot
		nameList.add("feeling");
		nameList.add("status");

		nameList.add("birth");
		nameList.add("age");
		nameList.add("how old");
		nameList.add("born");

		nameList.add("job");
		nameList.add("nationality");
		nameList.add("specialty");
		nameList.add("career");

		nameList.add("country");
		nameList.add("nationality");
		
		nameList.add("outside");
		nameList.add("inside");
		nameList.add("container");

		nameList.add("where");
		nameList.add("location");
		nameList.add("located");

		nameList.add("task");
		nameList.add("activity");
		nameList.add("action");
		nameList.add("mission");
		nameList.add("trip");
		nameList.add("excursion");

		nameList.add("building");
		nameList.add("associated");
		nameList.add("association");
		nameList.add("home");
		nameList.add("home town");
		
		nameList.add("vehicle top container");		
		nameList.add("vehicle container");
		nameList.add("garage");
		nameList.add("vehicle park");
		nameList.add("vehicle settlement");
		nameList.add("vehicle outside");		
		nameList.add("vehicle inside");
		
		nameList.add("bed time");
		nameList.add("sleep hour");
				
				
		nameList.addAll(createShortcutHelp());
		
		Iterator<Settlement> i = settlementList.iterator();
		while (i.hasNext()) {
			Settlement s = i.next();
			nameList.add(s.getName());

			// Get two lists of settlers name  
			// One having the order of [first name] + [last name]
			// The other having the order of [last name] + "," + [first name]
			Iterator<Person> j = s.getAllAssociatedPeople().iterator();
			while (j.hasNext()) {
				Person p = j.next();

				String first = "";
				String last = "";
				// Added names in both orders, namely, "first last" or "last, first"
				String firstLast = p.getName();
				String lastFirst = "";
				int len1 = firstLast.length();
				// Used for loop to find the last is the best approach instead of int index =
				// firstLast.indexOf(" ");
				int index = 0;

				for (int k = len1 - 1; k > 0; k--) {
					// Note: finding the whitespace from the end to 0 (from right to left) works
					// better than from left to right
					// e.g. Mary L. Smith (last name should be "Smith", not "L. Smith"
					if (firstLast.charAt(k) == ' ') {
						index = k;
						first = firstLast.substring(0, k);
						last = firstLast.substring(k + 1, len1);
						break;
					} else
						first = firstLast;
				}

				if (index == -1) {
					// the person has no last name
					first = firstLast;
					nameList.add(first);
				} else {
					first = firstLast.substring(0, index);
					last = firstLast.substring(index + 1, firstLast.length());
					lastFirst = last + ", " + first;
					nameList.add(firstLast);
					nameList.add(lastFirst);
				}

			}

			// get all robot names
			Iterator<Robot> k = s.getAllAssociatedRobots().iterator();
			while (k.hasNext()) {
				Robot r = k.next();
				nameList.add(r.getName());
			}
		}

		// Get all vehicles name
		Iterator<Vehicle> k = getVehicle(Simulation.instance().getUnitManager().getUnits()).iterator();
		while (k.hasNext()) {
			Vehicle v = k.next();
			nameList.add(v.getName());
		}
		
		return nameList;
	}

	/**
	 * Compiles the names of all people into one single list
	 * 
	 * @return a list of names
	 */
	public static List<String> createSettlerNames() {

		// Creates an array with the names of all of settlements
		Collection<Settlement> settlements = Simulation.instance().getUnitManager().getSettlements();
		List<Settlement> settlementList = new ArrayList<Settlement>(settlements);

		// autoCompleteArray = settlementList.toArray(new
		// String[settlementList.size()]);
		// or with java 8 stream
		// autoCompleteArray = settlementList.stream().toArray(String[]::new);

		// Creates an array with the names of all of people and robots
		List<String> nameList = new ArrayList<>();

		nameList.addAll(createShortcutHelp());
		
		Iterator<Settlement> i = settlementList.iterator();
		while (i.hasNext()) {
			Settlement s = i.next();
			nameList.add(s.getName());

			// Get two lists of settlers name  
			// One having the order of [first name] + [last name]
			// The other having the order of [last name] + "," + [first name]
			Iterator<Person> j = s.getAllAssociatedPeople().iterator();
			while (j.hasNext()) {
				Person p = j.next();

				String first = "";
				String last = "";
				// Added names in both orders, namely, "first last" or "last, first"
				String firstLast = p.getName();
				String lastFirst = "";
				int len1 = firstLast.length();
				// Used for loop to find the last is the best approach instead of int index =
				// firstLast.indexOf(" ");
				int index = 0;

				for (int k = len1 - 1; k > 0; k--) {
					// Note: finding the whitespace from the end to 0 (from right to left) works
					// better than from left to right
					// e.g. Mary L. Smith (last name should be "Smith", not "L. Smith"
					if (firstLast.charAt(k) == ' ') {
						index = k;
						first = firstLast.substring(0, k);
						last = firstLast.substring(k + 1, len1);
						break;
					} else
						first = firstLast;
				}

				if (index == -1) {
					// the person has no last name
					first = firstLast;
					nameList.add(first);
				} else {
					first = firstLast.substring(0, index);
					last = firstLast.substring(index + 1, firstLast.length());
					lastFirst = last + ", " + first;
					nameList.add(firstLast);
					nameList.add(lastFirst);
				}

			}

			// get all robot names
//			Iterator<Robot> k = s.getAllAssociatedRobots().iterator();
//			while (k.hasNext()) {
//				Robot r = k.next();
//				nameList.add(r.getName());
//			}
		}

		// Get all vehicles name
//		Iterator<Vehicle> k = getVehicle(Simulation.instance().getUnitManager().getUnits()).iterator();
//		while (k.hasNext()) {
//			Vehicle v = k.next();
//			nameList.add(v.getName());
//		}
		
		return nameList;
	}
	
}
