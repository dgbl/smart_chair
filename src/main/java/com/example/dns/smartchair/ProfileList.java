package com.example.dns.smartchair;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

/**
 *
 * @author dnsgbl
 */
public class ProfileList extends AppCompatActivity {
    Button btnCreate;
    ListView lv;
    List<String> profiles;
    ArrayAdapter adapter;

    /*! \brief Activity is started.
*         Called when the activity is starting.
*         @savedInstanceState 	Bundle: If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_list);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        setButtons();
        setListView();
    }

    /*! \brief Button is pressed.
 *         Set a message to be sent when a button is pressed.
 */
    protected void setButtons(){
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), EditProfile.class);
                Bundle b = new Bundle();
                b.putInt("new",1);
                in.putExtras(b);
                startActivity(in);
            }
        });
    }

    /*! \brief Sets list to listView.
 *         Profile will be set to listview in activity. In case of removing an object from list it calles the function to create a n alert dialog.
 */
    protected void setListView(){
        lv = (ListView) findViewById(R.id.profileList);
        profiles = ProfileManager.getProfileNames();
        adapter = new ArrayAdapter<String>(this, R.layout.listitem, profiles);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProfileManager.setUsedAtPosition(position, getApplicationContext());
                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(in);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //delete Profile
                createAlertDialog(position);
                return true;
            }
        });
    }

    /*! \brief Creates an alert dialog.
 *         Whenever a profile should get removed from list an alert dialog will appear.
 *         Making sure the selected profile is to be removed.
 */
    private void createAlertDialog(final int pos){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileList.this);
        dialog.setTitle("Achtung!");
        dialog.setMessage("Wollen Sie dieses Profil wirklich löschen?");
        dialog.setPositiveButton("Ja", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                boolean deleted = ProfileManager.deleteProfile(pos, getApplicationContext());
                if(deleted)
                    updateList(pos);
            }
        });
        dialog.setNegativeButton("Nein", null);
        dialog.show();
    }

    /*! \brief Updates the list of profiles.
 *         Removes profile from list on it's position and notifies.
 *         Is called whenever the observed object is changed.
 */
    private void updateList(int pos){
        profiles.remove(pos);
        adapter.notifyDataSetChanged();
    }
}

