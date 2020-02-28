package com.guitar.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guitar.db.model.Location;

@Repository
public interface LocationJpaRepository extends JpaRepository<Location, Long> {
	List<Location> findByStateLike(String stateName);
	
	List<Location> findByStateNotLike(String stateName);
	
	List<Location> findByStateIgnoreCaseStartingWith(String stateName);
	
	List<Location> findByStateOrCountry(String state, String country);

	List<Location> findByStateAndCountry(String state, String country);
	
	List<Location> findByStateNot(String state);
	
}
