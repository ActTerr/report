package mac.yk.report.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mac-yk on 2017/1/29.
 */

public class Weather implements Parcelable {
    String location;
    int temperature;
    protected Weather() {
    }

    public Weather(String location, int temperature) {
        this.location = location;
        this.temperature = temperature;
    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {

            return new Weather(in.readString(),in.readInt());
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location);
        dest.writeInt(temperature);
    }

    @Override
    public String toString() {
        return "Weather{" +
                "location='" + location + '\'' +
                ", temperature=" + temperature +
                '}';
    }
}
