package com.example.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 3926463443005150707L;

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Column(unique=true)
    private String username;

    private String password;
    public Employee(){

    }
    public Employee(String name,String username,String password){
        this.name= name;
        this.username = username;
        this.password= password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //@JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
}
