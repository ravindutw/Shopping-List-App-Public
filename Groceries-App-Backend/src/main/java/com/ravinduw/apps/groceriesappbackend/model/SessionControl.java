package com.ravinduw.apps.groceriesappbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "session_control")
public class SessionControl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String session_id;
    private String user_name;
    private String status;
    private String browser_fingerprint;

    public SessionControl() {}

    public SessionControl(String sessionId, String userName, String status, String browserFingerprint) {
        this.session_id = sessionId;
        this.user_name = userName;
        this.status = status;
        this.browser_fingerprint = browserFingerprint;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getBrowser_fingerprint() {
        return browser_fingerprint;
    }

    public void setBrowser_fingerprint(String browser_fingerprint) {
        this.browser_fingerprint = browser_fingerprint;
    }

}
