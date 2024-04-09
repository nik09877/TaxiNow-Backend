package com.taxinow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "make")
    private String make;

    @Column(name = "model")
    private String model;

    @Column(name = "year")
    private int year;

    @Column(name = "color")
    private String color;

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "capacity")
    private int capacity;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private Driver driver;



}
