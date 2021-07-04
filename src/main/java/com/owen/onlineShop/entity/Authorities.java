package com.owen.onlineShop.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

// Authorities表有2列: emailId and authorities
@Entity // Used with model class to specify that it is an entity and mapped to a table in the DB
@Table(name = "authorities")
public class Authorities implements Serializable {

    private static final long serialVersionUID = 8734140534986494039L;
    // SerialVersionUID is used to ensure that during deserialization
    // the same class (that was used during serialize process) is loaded.

    @Id
    private String emailId; // Primary Key

    private String authorities; // 分为ROLE_USER, ROLE_ADMIN

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }
}
