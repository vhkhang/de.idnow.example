package de.idnow.example.core.entity;

import java.io.Serializable;
import java.util.Date;

public class Identification implements Serializable, Entity {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private Date time;
    private int waiting_time;
    private int companyid;
    private Company company;

    public Identification() {
    }

    public Identification(int id, String name, Date time, int waiting_time, int companyid) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.waiting_time = waiting_time;
        this.companyid = companyid;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getWaiting_time() {
        return waiting_time;
    }

    public void setWaiting_time(int waiting_time) {
        this.waiting_time = waiting_time;
    }

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }
}
