package de.idnow.example.core.entity;

import java.io.Serializable;
import java.util.Date;

public class Identification implements Serializable, Entity {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private Date time;
    private int waitingTime;
    private int companyId;
    private Company company;

    public Identification() {
    }

    public Identification(int id, String name, Date time, int waitingTime, int companyId) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.waitingTime = waitingTime;
        this.companyId = companyId;
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

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
