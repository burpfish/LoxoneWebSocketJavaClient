package org.chelmer.response;

/**
 * Created by burfo on 21/02/2017.
 */
public class WeatherTimerItemEntry {
    private final long timestamp; // 32­Bit Integer (little endian)
    private final long weatherType; // 32­Bit Integer (little endian)
    private final long windDirection; // 32­Bit Integer (little endian)
    private final long solarRadiation; // 32­Bit Integer (little endian)
    private final long relativeHumidity; // 32­Bit Integer (little endian)
    private final double temperature; // 64­Bit Float (little endian)
    private final double perceivedTemperature; // 64­Bit Float (little endian)
    private final double dewPoint; // 64­Bit Float (little endian)
    private final double precipitation; // 64­Bit Float (little endian)
    private final double windSpeed; // 64­Bit Float (little endian)
    private final double barometicPressure; // 64­Bit Float (little endian)

    public WeatherTimerItemEntry(long timestamp, long weatherType, long windDirection, long solarRadiation, long relativeHumidity, double temperature, double perceivedTemperature, double dewPoint, double precipitation, double windSpeed, double barometicPressure) {
        this.timestamp = timestamp;
        this.weatherType = weatherType;
        this.windDirection = windDirection;
        this.solarRadiation = solarRadiation;
        this.relativeHumidity = relativeHumidity;
        this.temperature = temperature;
        this.perceivedTemperature = perceivedTemperature;
        this.dewPoint = dewPoint;
        this.precipitation = precipitation;
        this.windSpeed = windSpeed;
        this.barometicPressure = barometicPressure;
    }

    @Override
    public String toString() {
        return "WeatherTimerItemEntry{" +
                "timestamp=" + timestamp +
                ", weatherType=" + weatherType +
                ", windDirection=" + windDirection +
                ", solarRadiation=" + solarRadiation +
                ", relativeHumidity=" + relativeHumidity +
                ", temperature=" + temperature +
                ", perceivedTemperature=" + perceivedTemperature +
                ", dewPoint=" + dewPoint +
                ", precipitation=" + precipitation +
                ", windSpeed=" + windSpeed +
                ", barometicPressure=" + barometicPressure +
                '}';
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getWeatherType() {
        return weatherType;
    }

    public long getWindDirection() {
        return windDirection;
    }

    public long getSolarRadiation() {
        return solarRadiation;
    }

    public long getRelativeHumidity() {
        return relativeHumidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getPerceivedTemperature() {
        return perceivedTemperature;
    }

    public double getDewPoint() {
        return dewPoint;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getBarometicPressure() {
        return barometicPressure;
    }
}
