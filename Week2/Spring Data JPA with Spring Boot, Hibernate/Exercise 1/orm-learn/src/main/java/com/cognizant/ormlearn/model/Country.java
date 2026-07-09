package com.cognizant.ormlearn.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Persistence Entity Class representing the database table 'country'.
 * 
 * Notes on JPA Annotations used:
 * - @Entity: Indicates to Spring Data JPA / Hibernate that this class is a JPA entity.
 * - @Table: Specifies the primary database table name ("country") mapped to this entity.
 * - @Id: Marks the primary key field of the entity ("code" mapping to "co_code" column).
 * - @Column: Explicitly maps the Java field to the exact database column name ("co_code" / "co_name").
 */
@Entity
@Table(name = "country")
public class Country {

    @Id
    @Column(name = "co_code")
    private String code;

    @Column(name = "co_name")
    private String name;

    public Country() {
    }

    public Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Country [code=" + code + ", name=" + name + "]";
    }
}
