package com.guitar.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.guitar.db.model.Location;
import com.guitar.db.repository.LocationJpaRepository;

@ContextConfiguration(locations={"classpath:com/guitar/db/applicationTests-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class LocationPersistenceTests {
	@Autowired
	private LocationJpaRepository locationJpaRepository;
	
	@Test
	public void testJpaFindByStateAndCountry() {
		//Save Location
		Location location = new Location();
		location.setState("British Columbia");
		location.setCountry("Canada");
		
		// Test findBy
		location = locationJpaRepository.saveAndFlush(location);
		List<Location> locationList = locationJpaRepository.findByStateNot("British Columbia");
		assertNotNull(locationList);
		System.out.println("locationList size " + locationList.size());
		
	}
	
	@Test
	public void testJpaFindByStateOrCountry() {
		//Save Location
		Location location = new Location();
		location.setState("British Columbia");
		location.setCountry("Canada");
		
		// Test findBy
		location = locationJpaRepository.saveAndFlush(location);
		List<Location> locationList = locationJpaRepository.findByStateOrCountry("Columbia", "Canada");
		assertNotNull(locationList);
		assertEquals("Canada", locationList.get(0).getCountry());
		System.out.println("locationList size " + locationList.size());
		
	}

	@Test
	public void testJpaFind() {
		List<Location> locationList = locationJpaRepository.findAll();
		assertNotNull(locationList);
		
	}
	
	@Test
	@Transactional
	public void testSaveAndGetAndDelete() throws Exception {
		Location location = new Location();
		location.setCountry("Canada");
		location.setState("British Columbia");
		location = locationJpaRepository.saveAndFlush(location);

		Location otherLocation = locationJpaRepository.findOne(location.getId());
		assertEquals("Canada", otherLocation.getCountry());
		assertEquals("British Columbia", otherLocation.getState());
		
		//delete BC location now
		locationJpaRepository.delete(otherLocation);
	}

	@Test
	public void testFindWithLike() throws Exception {
		List<Location> locs = locationJpaRepository.findByStateLike("New%");
		assertEquals(4, locs.size());
	}

	@Test
	public void testFindWithNotLike() throws Exception {
		List<Location> locs = locationJpaRepository.findByStateNotLike("New%");
		System.out.println("locs.size() "+locs.size());
		assertEquals(46, locs.size());
	}
	
	@Test
	public void testFindWithStartingWith() throws Exception {
		List<Location> locs = locationJpaRepository.findByStateIgnoreCaseStartingWith("new");
		assertEquals(4, locs.size());
	}
	
	
	@Test
	@Transactional  //note this is needed because we will get a lazy load exception unless we are in a tx
	public void testFindWithChildren() throws Exception {
 		Location arizona = locationJpaRepository.findOne(3L);
		assertEquals("United States", arizona.getCountry());
		assertEquals("Arizona", arizona.getState());
		
		assertEquals(1, arizona.getManufacturers().size());
		
		assertEquals("Fender Musical Instruments Corporation", arizona.getManufacturers().get(0).getName());
	}
}
