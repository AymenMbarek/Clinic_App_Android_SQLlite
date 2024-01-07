package pt.ubi.di.pmd.a43855_t5;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Appointment")
public class Appointment {
    @PrimaryKey
    private int IDappointment;

    @ColumnInfo
    private int SNS;

    @ColumnInfo
    private int Day;

    @ColumnInfo
    private int Month;

    @ColumnInfo
    private int Year;

    @ColumnInfo
    private int Hour;

    @ColumnInfo
    private String Type;

    @ColumnInfo
    private String MedicResponsable;

    @ColumnInfo
    private String Email;

    @ColumnInfo
    private String Notes;

    public int getIDappointment() {
        return IDappointment;
    }

    public void setIDappointment(int IDappointment) {
        this.IDappointment = IDappointment;
    }

    public int getSNS() {
        return SNS;
    }

    public void setSNS(int SNS) {
        this.SNS = SNS;
    }

    public int getDay() {
        return Day;
    }

    public void setDay(int day) {
        Day = day;
    }

    public int getMonth() {
        return Month;
    }

    public void setMonth(int month) {
        Month = month;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public int getHour() {
        return Hour;
    }

    public void setHour(int hour) {
        Hour = hour;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMedicResponsable() {
        return MedicResponsable;
    }

    public void setMedicResponsable(String medicResponsable) {
        MedicResponsable = medicResponsable;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "IDappointment=" + IDappointment +
                ", SNS=" + SNS +
                ", Day=" + Day +
                ", Month=" + Month +
                ", Year=" + Year +
                ", Hour=" + Hour +
                ", Type='" + Type + '\'' +
                ", MedicResponsable='" + MedicResponsable + '\'' +
                ", Email='" + Email + '\'' +
                ", Notes='" + Notes + '\'' +
                '}';
    }
}
