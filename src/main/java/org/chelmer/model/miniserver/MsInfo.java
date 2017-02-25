package org.chelmer.model.miniserver;

import org.chelmer.exceptions.CouldNotDeserializeException;
import org.chelmer.model.entity.User;
import org.chelmer.model.weatherServer.TempUnit;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.MonthDay;

public class MsInfo {
    private final String msName;
    private final String projectName;
    private final URL localUrl;
    private final URL remoteUrl;
    private final TempUnit tempUnit;
    private final String currency;
    private final String location;
    private final String languageCode;
    private final MonthDay heatPeriodStart;
    private final MonthDay heatPeriodEnd;
    private final MonthDay coolPeriodStart;
    private final MonthDay coolPeriodEnd;
    private final String catTitle;
    private final String roomTitle;
    private final int miniserverType;
    private final boolean sortByRating;
    private final User currentUser;
    private final String serialNr;

    public MsInfo(String serialNr, String msName, String projectName, String localUrl, String remoteUrl, TempUnit tempUnit, String currency, String location, String languageCode, MonthDay heatPeriodStart, MonthDay heatPeriodEnd, MonthDay coolPeriodStart, MonthDay coolPeriodEnd, String catTitle, String roomTitle, int miniserverType, boolean sortByRating, User currentUser) {
        this.serialNr = serialNr;
        this.msName = msName;
        this.projectName = projectName;

        try {
            this.localUrl = new URL("http://" + localUrl);
            this.remoteUrl = new URL("http://" + remoteUrl);
        } catch (MalformedURLException e) {
            throw new CouldNotDeserializeException("Cannot create URL for: " + localUrl, e);
        }
        this.tempUnit = tempUnit;
        this.currency = currency;
        this.location = location;
        this.languageCode = languageCode;
        this.heatPeriodStart = heatPeriodStart;
        this.heatPeriodEnd = heatPeriodEnd;
        this.coolPeriodStart = coolPeriodStart;
        this.coolPeriodEnd = coolPeriodEnd;
        this.catTitle = catTitle;
        this.roomTitle = roomTitle;
        this.miniserverType = miniserverType;
        this.sortByRating = sortByRating;
        this.currentUser = currentUser;
    }

    public String getMsName() {
        return msName;
    }

    public String getProjectName() {
        return projectName;
    }

    public URL getLocalUrl() {
        return localUrl;
    }

    public URL getRemoteUrl() {
        return remoteUrl;
    }

    public TempUnit getTempUnit() {
        return tempUnit;
    }

    public String getCurrency() {
        return currency;
    }

    public String getLocation() {
        return location;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public MonthDay getHeatPeriodStart() {
        return heatPeriodStart;
    }

    public MonthDay getHeatPeriodEnd() {
        return heatPeriodEnd;
    }

    public MonthDay getCoolPeriodStart() {
        return coolPeriodStart;
    }

    public MonthDay getCoolPeriodEnd() {
        return coolPeriodEnd;
    }

    public String getCatTitle() {
        return catTitle;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public int getMiniserverType() {
        return miniserverType;
    }

    public boolean isSortByRating() {
        return sortByRating;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}


