package com.example.moove.models;

import android.net.Uri;

import com.example.moove.utilities.JsonFormatter;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class User {
    public static User currentUser;

    private String id;
    private String email;
    private String username;
    private String phoneNumber;
    private String photoUrl;
    private long weight;
    private long height;
    private int lastHeartRate;
    private Timestamp birthDate;

    public User() { }

    public User(String id) { this.id = id; }

    public User(String id, String email, String username, String phoneNumber, String photoUrl,
            long weight, long height, Timestamp birthDate) {
        setId(id);
        setEmail(email);
        setUsername(username);
        setPhoneNumber(phoneNumber);
        setPhotoUrl(photoUrl);
        setWeight(weight);
        setHeight(height);
        setBirthDate(birthDate);
    }

    public User(String id, String email, String username, String phoneNumber, Uri photoUrl,
                long weight, long height, Timestamp birthDate) {
        setId(id);
        setEmail(email);
        setUsername(username);
        setPhoneNumber(phoneNumber);
        setPhotoUrl(photoUrl);
        setWeight(weight);
        setHeight(height);
        setBirthDate(birthDate);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhotoUrl() {
        if (photoUrl == null)
            return "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/1024px-Circle-icons-profile.svg.png";

        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        String photoStr = null;
        if (photoUrl != null)
            photoStr = photoUrl.toString();

        this.photoUrl = photoStr;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public int getLastHeartRate() { return lastHeartRate; }

    public void setLastHeartRate(int lastHeartRate) { this.lastHeartRate = lastHeartRate; }

    public Timestamp getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Timestamp birthDate) {
        this.birthDate = birthDate;
    }

    public int getAge() {
        if (getBirthDate() == null)
            return -1;

        Date birth = getBirthDate().toDate();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(birth);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month + 1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR))
            age--;

        return age;
    }

    public Map<String, Object> getHashedData() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", getId());
        userData.put("username", getUsername());
        userData.put("email", getEmail());
        userData.put("phoneNumber", getPhoneNumber());
        userData.put("photoUrl", getPhotoUrl());
        userData.put("weight", getWeight());
        userData.put("height", getHeight());
        userData.put("birthDate", getBirthDate());

        return userData;
    }

    @Override
    public String toString() {
        return JsonFormatter.jsonString(getHashedData());
    }
}
