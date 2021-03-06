package city;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import engine.TimeSimulator;
import used.Point;

/**
 * Implements a design pattern "Singleton" to call only one instance of the
 * object.
 * 
 * @author MOEs
 *
 */
public class City {
	private static City instance = new City();
	private static City instanceTest = new City();

	private TimeSimulator timeSim;

	private HashMap<Point, District> districts;
	private ArrayList<SubwayLine> subwayLines;
	private ArrayList<Citizen> citizens;

	private int nbStation;
	private int servicing = 500;
	private float prosperity;
	private float unemployement;

	public City() {
		timeSim = new TimeSimulator();
		districts = new HashMap<Point, District>();
		subwayLines = new ArrayList<SubwayLine>();
		citizens = new ArrayList<Citizen>();
		nbStation = 0;
		prosperity = 50;
	}

	public static City getInstance() {
		return instance;
	}

	public static City getInstanceTest() {
		return instanceTest;
	}

	public int getServicing() {
		return servicing;
	}

	public void setServicing(int servicing) {
		this.servicing = servicing;
	}

	public float getProsperity() {
		return prosperity;
	}

	public void setProsperity(float prosperity) {
		this.prosperity = prosperity;
	}

	public void addDistrict(Point position, District district) {
		districts.put(position, district);
	}

	public void addSubwayLine(SubwayLine line) {
		subwayLines.add(line);
	}

	// Contains all district build on the map
	public HashMap<Point, District> getDistricts() {
		return districts;
	}

	public ArrayList<SubwayLine> getSubwayLines() {
		return subwayLines;
	}

	public TimeSimulator getTimeSimulator() {
		return timeSim;
	}

	public int getNbDistricts() {
		return districts.size();
	}

	public District getDistrictByPosition(Point pos) {
		District district = null;

		for (District dist : districts.values()) {
			if (dist.getPosition().equals(pos)) {
				district = dist;
				break;
			}
		}
		return district;
	}

	public boolean isDistrictPosition(Point position) {
		boolean contains = false;
		Set<Point> pos = districts.keySet();

		for (Point p : pos) {
			if (p.equals(position)) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	public Point getPositionById(int id) {
		Point pos = null;
		for (District district : districts.values()) {
			if (district.hasStation()) {
				if (district.getStation().getId() == id)
					pos = district.getPosition();
			}
		}
		return pos;
	}

	public int getIdByPosition(Point position) {
		int id = 0;
		for (District district : districts.values()) {
			if (district.hasStation()) {
				if (district.getPosition().equals(position))
					id = district.getStation().getId();
			}
		}
		return id;
	}

	public ArrayList<Citizen> getCitizensByDistrict(District dist) {
		ArrayList<Citizen> result = new ArrayList<Citizen>();
		for (Citizen c : citizens) {
			if (c.getOriginDistrict().getPosition().equals(dist.getPosition())) {
				result.add(c);
			}
		}
		return result;
	}

	public int getNbCitizensOfDistrict(District dist) {
		int count = 0;
		for (Citizen c : citizens) {
			if (c.getOriginDistrict().equals(dist)) {
				count++;
			}
		}
		return count;
	}

	public ArrayList<District> getDistrictByType(String type) {
		ArrayList<District> result = new ArrayList<District>();
		for (District dis : districts.values()) {
			switch (type) {
			case "pri":
				if (dis.getType().isPrivate())
					result.add(dis);
				break;
			case "pub":
				if (dis.getType().isPublic())
					result.add(dis);
				break;
			case "res":
				if (dis.getType().isResidential())
					result.add(dis);
				break;
			}
		}
		return result;
	}

	public Station getClosestStation(Point position) {
		Station station = null;
		double min = Double.MAX_VALUE;
		for (District dist : districts.values()) {
			double tmp = position.distance(dist.getPosition());
			if (tmp < min && dist.hasStation()) {
				min = tmp;
				station = dist.getStation();
			}
		}
		return station;
	}

	public int nbStations() {
		return nbStation;
	}

	public void addStation() {
		this.nbStation++;
	}

	public int getNbSubwayLines() {
		return subwayLines.size();
	}

	public void addCitizen(Citizen citizen) {
		citizens.add(citizen);
	}

	public ArrayList<Citizen> getCitizens() {
		return citizens;
	}

	public boolean isEmpty() {
		return districts.isEmpty() || citizens.isEmpty();
	}

	public void setCitizens(ArrayList<Citizen> citizens) {
		this.citizens = citizens;
	}

	public int getNbStation() {
		return nbStation;
	}

	public void setNbStation(int nbStation) {
		this.nbStation = nbStation;
	}

	public int getNbCitizens() {
		return citizens.size();
	}

	public float getUnemployement() {
		return unemployement;
	}

	public void setUnemployement(float unemployement) {
		this.unemployement = unemployement;
	}

	@Override
	public String toString() {
		return "Date=" + timeSim.getTime() + "\n" + "nbDistricts=" + getNbDistricts() + "\n" + "SubwayLines="
				+ subwayLines + "\n" + "nbStation=" + nbStation + "\n" + "Citizens=" + getNbCitizens() + "\n"
				+ "Prosperity=" + prosperity;
	}
}