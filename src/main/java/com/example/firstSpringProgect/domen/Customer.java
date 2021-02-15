package com.example.firstSpringProgect.domen;

import javax.persistence.* ;
import java.util.Date;

@Entity
public class Customer {

    //"customerseq" is Oracle sequence name.
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTSEQ")
    @SequenceGenerator(sequenceName = "customerseq", allocationSize = 1, name = "CUSTSEQ")
    Long id;

    String name;

    String email;

    @Column(name = "CREATED__DATE")
    Date date;

    //getters and setters, contructors
}