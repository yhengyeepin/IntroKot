package ttl.larku.dao.jpa;

import java.util.concurrent.ConcurrentHashMap;
import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JPATrackDAO implements BaseDAO<Track> {

	private Map<Integer, Track> tracks = new ConcurrentHashMap<Integer, Track>();
	private static int nextId = 0;
	
	private String from;
	public JPATrackDAO(String from) {
		this.from = from + ": ";
	}
	
	public JPATrackDAO() {
		this("JPA");
	}

	@Override
	public void update(Track updateObject) {
		if(tracks.containsKey(updateObject.getId())) {
			tracks.put(updateObject.getId(), updateObject);
		}
	}

	@Override
	public void delete(Track track) {
		tracks.remove(track.getId());
	}

	@Override
	public Track create(Track newObject) {
		//Create a new Id
		int newId = ++nextId;
		newObject.setId(newId);
		tracks.put(newId, newObject);
		
		//Put our Mark
		newObject.setTitle(from + newObject.getTitle());
		return newObject;
	}

	@Override
	public Track get(int id) {
		return tracks.get(id);
	}

	@Override
	public List<Track> getAll() {
		return new ArrayList<Track>(tracks.values());
	}
	

	public Map<Integer, Track> getTracks() {
		nextId = 0;
		return tracks;
	}

	public void setTracks(Map<Integer, Track> tracks) {
		this.tracks = tracks;
	}
}
