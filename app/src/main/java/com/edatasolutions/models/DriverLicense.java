package com.edatasolutions.models;

import android.os.Parcel;
import android.os.Parcelable;

public class DriverLicense implements Parcelable {
    public String documentType;
    public String firstName;
    public String middleName;
    public String lastName;
    public String gender;
    public String addressStreet;
    public String addressCity;
    public String addressState;
    public String addressZip;
    public String licenseNumber;
    public String issueDate;
    public String expiryDate;
    public String birthDate;
    public String issuingCountry;
    public String weight;
    public String suffix;
    public String address1;
    public String address2;
    public String driverState;
    public String hairColor;
    public String eyeColor;
    public String height;
    public String driverlicenseType;

    public DriverLicense() {

    }

    protected DriverLicense(Parcel in) {
        documentType = in.readString();
        firstName = in.readString();
        middleName = in.readString();
        lastName = in.readString();
        gender = in.readString();
        addressStreet = in.readString();
        addressCity = in.readString();
        addressState = in.readString();
        addressZip = in.readString();
        licenseNumber = in.readString();
        issueDate = in.readString();
        expiryDate = in.readString();
        birthDate = in.readString();
        issuingCountry = in.readString();
        weight = in.readString();
        suffix = in.readString();
        address1 = in.readString();
        address2 = in.readString();
        driverState = in.readString();
        hairColor = in.readString();
        eyeColor = in.readString();
        height = in.readString();
        driverlicenseType=  in.readString();
    }

    public static final Creator<DriverLicense> CREATOR = new Creator<DriverLicense>() {
        @Override
        public DriverLicense createFromParcel(Parcel in) {
            return new DriverLicense(in);
        }

        @Override
        public DriverLicense[] newArray(int size) {
            return new DriverLicense[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(documentType);
        parcel.writeString(firstName);
        parcel.writeString(middleName);
        parcel.writeString(lastName);
        parcel.writeString(gender);
        parcel.writeString(addressStreet);
        parcel.writeString(addressCity);
        parcel.writeString(addressState);
        parcel.writeString(addressZip);
        parcel.writeString(licenseNumber);
        parcel.writeString(issueDate);
        parcel.writeString(expiryDate);
        parcel.writeString(birthDate);
        parcel.writeString(issuingCountry);
        parcel.writeString(weight);
        parcel.writeString(suffix);
        parcel.writeString(address1);
        parcel.writeString(address2);
        parcel.writeString(driverState);
        parcel.writeString(hairColor);
        parcel.writeString(eyeColor);
        parcel.writeString(height);
        parcel.writeString(driverlicenseType);
    }
}
