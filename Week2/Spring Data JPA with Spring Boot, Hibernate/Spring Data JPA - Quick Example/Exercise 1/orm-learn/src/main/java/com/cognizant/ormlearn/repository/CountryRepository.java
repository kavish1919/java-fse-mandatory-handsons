package com.cognizant.ormlearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognizant.ormlearn.model.Country;

/**
 * Repository Interface for CRUD and custom query operations on Country entity.
 * 
 * Notes on Spring Data JPA Repository:
 * - Extends JpaRepository<Country, String>: Provides standard JPA methods like findAll(), findById(), save(), delete().
 *   The first type parameter is the Entity type (Country) and the second is the primary key type (String).
 * - @Repository: Stereotype annotation indicating that this interface is a Spring Data repository bean.
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, String> {

}
