package pt.ubi.di.pmd.a43855_t5;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;


public class AddAppointment extends Activity {

    String id;

    Button confirm,cancel;
    ImageButton add_settings_button;
    ImageButton add_list_button;
    ImageButton add_home_button;
    Button hourButton;
    Spinner apType;
    Spinner doctorName;
    EditText email;
    EditText apNote;

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private int year,month,day, hour;
    private String strDoctor, strType,strNote,strEmail;
    private int nSNS,apID;

    DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Initialize the te view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_appointment);
        Intent iCameToAdd = getIntent();
        id = iCameToAdd.getStringExtra("SNS");

        //Initialize database
        db = DataBase.getInstance(getApplicationContext());

        //Initialize the components
        confirm = (Button) findViewById(R.id.confirm);
        cancel = (Button) findViewById(R.id.cancel);
        apType = (Spinner) findViewById(R.id.add_typeSpinner);
        doctorName = (Spinner) findViewById(R.id.add_doctorSpinner);
        email = (EditText) findViewById(R.id.contactEmail);
        apNote = (EditText) findViewById(R.id.apNotes);
        hourButton = (Button) findViewById(R.id.hourButton);
        dateButton = (Button) findViewById(R.id.dateButton);
        add_settings_button = (ImageButton) findViewById(R.id.add_settingsButton);
        add_home_button = (ImageButton) findViewById(R.id.add_homeButton);
        add_list_button = (ImageButton) findViewById(R.id.add_appointmentsButton);




        //==================================Setup the doctor name spinner + type spinner=======================================================================
        //Create the types
        String[] typesList={"General consultation","COVID-19 test","Blood exam","Eye exam","Ear exam (Otoscopy)","Cardiac exam","Prostate Exam","Endoscopy"};
        String[] generalDoctors = {"Dr. Ângelo Morgado","Dr. Rita Quelhas"};
        String[] covidDoctors = {"Dr. Rosa Duarte","Dr. Joana Gonçalves"};
        String[] bloodDoctors = {"Dr. Rita Quelhas","Dr. Artur Canário","Dr. André Garcia"};
        String[] eyeDoctors = {"Dr. Diogo Correia","Dr. Manuel Morais"};
        String[] earDoctors = {"Dr. Gonçalo Simões","Dr. Beatriz Nave"};
        String[] cardiacDoctors = {"Dr. Luís Sena","Dr. Inês Carrilho"};
        String[] prostateDoctors = {"Dr. João Pina","Dr. Francisco Sirenes"};
        String[] endoscopyDoctors = {"Dr. Joana Morais","Dr. Ângelo Morgado"};

        strType = typesList[0];
        strDoctor = generalDoctors[0];

        //check the type spinner
        ArrayAdapter<String> doctorAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_layout, generalDoctors);
        doctorName.setAdapter(doctorAdapter);
        doctorAdapter.notifyDataSetChanged();

        //First spinner listener that will change the values of the second
        apType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] selectedList;
                switch (position)
                {
                    case 0:
                        selectedList = generalDoctors;
                        strDoctor = generalDoctors[0];
                        break;
                    case 1:
                        selectedList = covidDoctors;
                        strDoctor = covidDoctors[0];
                        break;
                    case 2:
                        selectedList = bloodDoctors;
                        strDoctor = bloodDoctors[0];
                        break;
                    case 3:
                        selectedList = eyeDoctors;
                        strDoctor = eyeDoctors[0];
                        break;
                    case 4:
                        selectedList = earDoctors;
                        strDoctor = earDoctors[0];
                        break;
                    case 5:
                        selectedList = cardiacDoctors;
                        strDoctor = cardiacDoctors[0];
                        break;
                    case 6:
                        selectedList = prostateDoctors;
                        strDoctor = prostateDoctors[0];
                        break;
                    case 7:
                        selectedList = endoscopyDoctors;
                        strDoctor = endoscopyDoctors[0];
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + position);
                }

                ArrayAdapter<String> doctorAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_layout, selectedList);

                strType = parent.getItemAtPosition(position).toString();
                doctorName.setAdapter(doctorAdapter);
                doctorAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        //Listener of the second spinner
        doctorName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strDoctor = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });





        //============================================================Setup the hour==========================================================
        hourButton.setOnClickListener(
                oView -> showHour()
        );

        //Setup the date
        initDatePicker();
        dateButton.setText(getTodaysDate());

        dateButton.setOnClickListener(
                oView -> datePickerDialog.show()
        );


        //======================================================Confirmation Buttons======================================================================================
        confirm.setOnClickListener(
                oView -> {

                    //Get the values of the appointment
                    year = datePickerDialog.getDatePicker().getYear();
                    month = datePickerDialog.getDatePicker().getMonth() + 1;
                    day = datePickerDialog.getDatePicker().getDayOfMonth();
                    hour = Integer.parseInt((String) hourButton.getText());
                    strEmail = email.getText().toString();
                    strNote = apNote.getText().toString();
                    apID = db.myDao().getMaxAppointmentID() + 1;
                    nSNS = Integer.parseInt(id);


                    //Checks if this appointment with this doctor already exists
                    List<Appointment> checkList = db.myDao().checkAppointment(year,month,day,strDoctor, hour);
                    Appointment a = new Appointment();
                    Client c = db.myDao().getClientBySNS(nSNS);

                    //Checks if it is a correct hour
                    Calendar cal = Calendar.getInstance();
                    boolean validHour = true;
                    if((cal.get(Calendar.MONTH) + 1 == month) && (cal.get(Calendar.DAY_OF_MONTH) == day) && (hour <= cal.get(Calendar.HOUR_OF_DAY)))
                    {
                        validHour = false;
                        Toast.makeText(AddAppointment.this,
                                "Invalid hour", Toast.LENGTH_SHORT).show();
                    }

                    //Checks if the email is appropriate
                    boolean validEmail = true;
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if(strEmail.matches(emailPattern) && !strEmail.isEmpty())
                    {
                        Toast.makeText(AddAppointment.this,
                                "Invalid email", Toast.LENGTH_SHORT).show();
                        validEmail = false;
                    }

                    //Only adds the appointment if all the conditions are met
                    if(checkList.isEmpty() && validEmail && validHour)
                    {
                        a.setDay(day);
                        a.setMonth(month);
                        a.setYear(year);
                        a.setIDappointment(apID);
                        a.setSNS(nSNS);
                        if(!strEmail.isEmpty())
                            a.setEmail(strEmail);
                        else
                            a.setEmail(c.getEmail());
                        a.setType(strType);
                        a.setMedicResponsable(strDoctor);
                        if(!strNote.isEmpty())
                            a.setNotes(strNote);
                        else
                            a.setNotes(" ");
                        a.setHour(hour);

                        db.myDao().addAppointment(a);

                        Toast.makeText(AddAppointment.this,
                                "Success!", Toast.LENGTH_LONG).show();

                        Intent AddToDetails = new Intent(this, AppointmentDetails.class);
                        AddToDetails.putExtra("SNS", String.valueOf(apID));
                        finish();
                        this.overridePendingTransition(0, 0);
                        startActivity(AddToDetails);

                    }
                    else{
                        Toast.makeText(AddAppointment.this,
                                "Appointment already exists", Toast.LENGTH_SHORT).show();
                    }

                }
        );

        //Goes back to home
        cancel.setOnClickListener(
                oView -> {
                    Intent AddToHome = new Intent(this, InitialPage.class);
                    AddToHome.putExtra("SNS", id);
                    finish();
                    this.overridePendingTransition(0, android.R.anim.fade_out);
                    startActivity(AddToHome);
                }
        );


        //Setup the menu buttons
        add_list_button.setOnClickListener(
                oView -> {
                    Intent AddToList = new Intent(this, AppointmentList.class);
                    AddToList.putExtra("SNS", id);
                    finish();
                    this.overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_left);
                    startActivity(AddToList);
                }
        );
        add_settings_button.setOnClickListener(
                oView -> {
                    Intent AddToSettings = new Intent(this, SettingsPage.class);
                    AddToSettings.putExtra("SNS", id);
                    finish();
                    this.overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_left);
                    startActivity(AddToSettings);
                }
        );
        add_home_button.setOnClickListener(
                oView -> {
                    Intent AddToHome = new Intent(this, InitialPage.class);
                    AddToHome.putExtra("SNS", id);
                    finish();
                    this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
                    startActivity(AddToHome);
                }
        );

    }

    //Gets today's date
    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month++;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month, year);
    }

    @Override
    public void onBackPressed() {
        Intent AddToHome = new Intent(this, InitialPage.class);
        AddToHome.putExtra("SNS", id);
        finish();
        this.overridePendingTransition(0, android.R.anim.fade_out);
        startActivity(AddToHome);
    }

    //===================Date Methods==================
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            dateButton.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this,dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    //============================== Hour Methods ===========================================
    public void showHour()
    {
        //Creates the dialog and shows it
        final Dialog d = new Dialog(AddAppointment.this);
        d.setTitle("Pick an hour:");
        d.setContentView(R.layout.hour_dialog);

        Button setHour = (Button) d.findViewById(R.id.setHour);
        Button cancelHour = (Button) d.findViewById(R.id.cancelHour);

        //gets the number picker and changes it
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(19);
        np.setMinValue(8);
        np.setWrapSelectorWheel(false);

        //Changes the value of the button on confirm
        setHour.setOnClickListener(v -> {
            hourButton.setText(String.valueOf(np.getValue()));
            d.dismiss();
        });

        //Simply closes the app
        cancelHour.setOnClickListener(v -> d.dismiss());

        //Shows the dialog
        d.show();
    }
}