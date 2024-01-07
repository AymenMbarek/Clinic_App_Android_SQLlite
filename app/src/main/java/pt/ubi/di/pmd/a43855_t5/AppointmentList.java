package pt.ubi.di.pmd.a43855_t5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class AppointmentList extends Activity implements MyRecyclerViewAdapter.ItemClickListener{

    String id;
    DataBase db;
    List<Appointment> apList;

    ImageButton list_settings_button;
    ImageButton list_add_button;
    ImageButton list_home_button;

    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Irá inicializar a view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_list);
        Intent iCameToList = getIntent();
        id = iCameToList.getStringExtra("SNS");

        //Setup the database
        db = DataBase.getInstance(getApplicationContext());
        apList = db.myDao().getAppointmentsBySNS(Integer.parseInt(id));
        Collections.reverse(apList);

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.list_AppointmentsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, apList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);





        //=================================MENU OPTIONS===================================
        //Setup the menu buttons
        list_settings_button = (ImageButton) findViewById(R.id.list_settingsButton);
        list_home_button = (ImageButton) findViewById(R.id.list_homeButton);
        list_add_button = (ImageButton) findViewById(R.id.list_addButton);

        list_add_button.setOnClickListener(
                oView -> {
                    Intent ListToAdd = new Intent(this, AddAppointment.class);
                    ListToAdd.putExtra("SNS", id);
                    finish();
                    this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
                    startActivity(ListToAdd);
                }
        );
        list_settings_button.setOnClickListener(
                oView -> {
                    Intent ListToSettings = new Intent(this, SettingsPage.class);
                    ListToSettings.putExtra("SNS", id);
                    finish();
                    this.overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_left);
                    startActivity(ListToSettings);
                }
        );
        list_home_button.setOnClickListener(
                oView -> {
                    Intent ListToHome = new Intent(this, InitialPage.class);
                    ListToHome.putExtra("SNS", id);
                    finish();
                    this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
                    startActivity(ListToHome);
                }
        );
    }

    @Override
    public void onBackPressed() {
        Intent ListToHome = new Intent(this, InitialPage.class);
        ListToHome.putExtra("SNS", id);
        finish();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(ListToHome);
    }

    //If any Item of the list is clicked
    @Override
    public void onItemClick(View view, int position) {
        Appointment a = adapter.getItem(position);
        Intent ListToDetails = new Intent(this, AppointmentDetails.class);
        ListToDetails.putExtra("SNS", String.valueOf(a.getIDappointment()));
        finish();
        this.overridePendingTransition(0, android.R.anim.fade_out);
        startActivity(ListToDetails);
    }
}