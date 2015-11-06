package ua.com.yakovchuk.reminder.app.entity;

import android.location.Address;
import android.location.Location;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

@Table(name = "Mind")
public class Mind extends Model {
    @Column(name = "title")
    private String title;

    @Column(name = "body")
    private String body;

    @Column(name = "createdDate")
    private Date createdDate;

    @Column(name = "longtitude")
    private double longtitude;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "locationName")
    private String locationName;

    @Column(name = "lastModifedTime")
    private Date lastModifedTime;

    public Mind (String title, String body){
        this.title = title;
        this.body = body;
    }
    public Mind () {

    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Date getLastModifedTime() {
        return lastModifedTime;
    }

    public void setLastModifedTime(Date lastModifedTime) {
        this.lastModifedTime = lastModifedTime;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
