package ttl.larku.dao.inmemory;

import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryStudentDAO implements BaseDAO<Student>{

	private Map<Integer, Student> students = new HashMap<Integer, Student>();
	private static AtomicInteger nextId = new AtomicInteger(0);
	
	public void update(Student updateObject) {
		if(students.containsKey(updateObject.getId())) {
			students.put(updateObject.getId(), updateObject);
		}
	}

	public void delete(Student student) {
		students.remove(student.getId());
	}

	public Student create(Student newObject) {
		//Create a new Id
		int newId = nextId.incrementAndGet();
		newObject.setId(newId);
		students.put(newId, newObject);
		
		return newObject;
	}

	public Student get(int id) {
		return students.get(id);
	}

	public List<Student> getAll() {
		return new ArrayList<Student>(students.values());
	}

	public void deleteStore() {
		students = null;
		nextId.set(0);
	}

	public void createStore() {
		students = new HashMap<Integer, Student>();
		nextId.set(0);
	}

	public Map<Integer, Student> getStudents() {
		return students;
	}
}
