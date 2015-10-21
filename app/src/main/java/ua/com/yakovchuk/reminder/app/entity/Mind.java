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

    public Mind (String title, String body){
        this.title = title;
        this.body = body;
    }
    public Mind () {

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
