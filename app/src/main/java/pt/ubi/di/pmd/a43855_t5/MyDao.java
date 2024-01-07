package pt.ubi.di.pmd.a43855_t5;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MyDao {
    @Insert
    public void addClient(Client c);

    @Query("SELECT * FROM Client")
    public List<Client> getClients();

    @Query("SELECT * FROM Client WHERE SNS = :nSNS")
    public Client getClientBySNS(int nSNS);

    @Query("SELECT * FROM Appointment WHERE SNS = :nSNS")
    public List<Appointment> getAppointmentsBySNS(int nSNS);

    @Query("SELECT * FROM Appointment WHERE IDappointment = :id")
    public Appointment getAppointmentByID(int id);

    @Query("SELECT MAX(IDappointment) FROM Appointment")
    public int getMaxAppointmentID();

    @Query("SELECT * FROM Appointment WHERE Year = :year AND Month = :month AND Day = :day AND MedicResponsable = :medic AND Hour = :hour")
    public List<Appointment> checkAppointment(int year, int month, int day, String medic, int hour);

    @Query("SELECT * FROM Appointment WHERE Year = :year AND Month = :month AND SNS = :nSns")
    public List<Appointment> getMonthAppointments(int year, int month, int nSns);

    @Insert
    public void addAppointment(Appointment a);
}
