package org.chelmer.model.miniserver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.chelmer.exceptions.ParsingException;
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

    @JsonCreator
    public MsInfo(@JsonProperty("serialNr") String serialNr, @JsonProperty("msName") String msName, @JsonProperty("projectName") String projectName, @JsonProperty("localUrl") String localUrl, @JsonProperty("remoteUrl") String remoteUrl, @JsonProperty("tempUnit") TempUnit tempUnit, @JsonProperty("currency") String currency, @JsonProperty("location") String location, @JsonProperty("languageCode") String languageCode, @JsonProperty("heatPeriodStart") MonthDay heatPeriodStart, @JsonProperty("heatPeriodEnd") MonthDay heatPeriodEnd, @JsonProperty("coolPeriodStart") MonthDay coolPeriodStart, @JsonProperty("coolPeriodEnd") MonthDay coolPeriodEnd, @JsonProperty("catTitle") String catTitle, @JsonProperty("roomTitle") String roomTitle, @JsonProperty("miniserverType") int miniserverType, @JsonProperty("sortByRating") boolean sortByRating, @JsonProperty("currentUser") User currentUser) {
        this.serialNr = serialNr;
        this.msName = msName;
        this.projectName = projectName;

        try {
            this.localUrl = new URL("http://" + localUrl);
            this.remoteUrl = new URL("http://" + remoteUrl);
        } catch (MalformedURLException e) {
            throw new ParsingException("Cannot create URL for: " + localUrl, e);
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


