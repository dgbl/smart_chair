package com.example.dns.smartchair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfile extends AppCompatActivity {
    Button btnSave;
    boolean newItem;
    SeekBar sst;
    SeekBar sbt;
    SeekBar h;
    EditText n;
    TextView sh;
    TextView ss;
    TextView ssb;

    /*! \brief Activity is started.
*         Called when the activity is starting.
*         @savedInstanceState 	Bundle: If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        newItem = true;
        Intent in = getIntent();
        Bundle b = in.getExtras();
        int ne = b.getInt("new");
        //setViewValues();
        if(ne == 0) {
            newItem = false;
            setViewValues();
        }

        setButtons();

    }

    /*! \brief Button is pressed.
 *         Set a message to be sent when a button is pressed.
 */
    protected void setButtons(){
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newItem == false){
                    updateProfile();
                    Intent in = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(in);
                }
                else{
                    addProfile();
                    Intent in = new Intent(getApplicationContext(), ProfileList.class);
                    startActivity(in);
                }

            }
        });
    }

    /*! \brief Updates the used profile.
 *         Seat position and height can be edited.
 *         Name change is only permitted whether the name is not already given to an other profile.
 */
    protected void updateProfile(){
        sst = (SeekBar) findViewById(R.id.seekBarSeatsquab);
        sbt = (SeekBar) findViewById(R.id.seekBarSeatback);
        h = (SeekBar) findViewById(R.id.seekBarHeight);
        n = (EditText) findViewById(R.id.editTextName);
        int seatSquab = sst.getProgress();
        int seatBack = sbt.getProgress();
        int height = h.getProgress();
        String name = n.getText().toString();
        boolean dup = checkDuplicates(name, true);
        if(dup) {
            Toast.makeText(getApplicationContext(), "Name bereits vergeben!", Toast.LENGTH_SHORT).show();
            return;
        }
        ProfileManager.updateProfile(name, seatSquab, seatBack, height, getApplicationContext());
    }

    /*! \brief Adds the new created profile.
 *         Setting of seat position and height.
 *         Setting the name is only permitted whether the name is not already given to an other profile.
 */
    protected void addProfile(){
        sst = (SeekBar) findViewById(R.id.seekBarSeatsquab);
        sbt = (SeekBar) findViewById(R.id.seekBarSeatback);
        h = (SeekBar) findViewById(R.id.seekBarHeight);
        n = (EditText) findViewById(R.id.editTextName);
        int seatSquab = sst.getProgress();
        int seatBack = sbt.getProgress();
        int height = h.getProgress();
        String name = n.getText().toString();
        boolean dup = checkDuplicates(name, false);
        if(dup) {
            Toast.makeText(getApplicationContext(), "Name bereits vergeben!", Toast.LENGTH_SHORT).show();
            return;
        }
        ProfileManager.addProfile(name, seatSquab, seatBack, height, getApplicationContext());
    }

    /*! \brief Sets the selected values.
 *         Seat position and height are to be selected by seek bar.
 */
    protected void setViewValues(){
        UserProfile up = ProfileManager.getUsed();
        sst = (SeekBar) findViewById(R.id.seekBarSeatsquab);
        sbt = (SeekBar) findViewById(R.id.seekBarSeatback);
        h = (SeekBar) findViewById(R.id.seekBarHeight);
        n = (EditText) findViewById(R.id.editTextName);
        sh = (TextView) findViewById(R.id.selectedHeight);
        ss = (TextView) findViewById(R.id.selectedSeat);
        ssb = (TextView) findViewById(R.id.selectedSeatBack);

        int seatSquab = up.getSeatSquabTilt();
        int seatBack = up.getSeatBackTilt();
        int height = up.getHeight();
        String name = up.getName();

        sh.setText(String.valueOf(height));
        ss.setText(String.valueOf(seatSquab));
        ssb.setText(String.valueOf(seatBack));



        h.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                sh.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                sh.setVisibility(View.VISIBLE);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sst.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                ss.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                ss.setVisibility(View.VISIBLE);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                ssb.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                ssb.setVisibility(View.VISIBLE);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sst.setProgress(seatSquab);
        sbt.setProgress(seatBack);
        h.setProgress(height);
        n.setText(name);
    }

    /*! \brief Checks for duplicates
*         Compareing the profile's name to already given profiles' names.
*/
    private boolean checkDuplicates(String name, boolean edit){
        if(!edit) {
            for (String p : ProfileManager.getProfileNames()) {
                if (p.equals(name))
                    return true;
            }
            return false;
        }else{
            if(ProfileManager.getUsed().getName().equals(name))
                return false;
            for(String p: ProfileManager.getProfileNames()){
                if(p.equals(name))
                    return true;
            }
            return false;
        }
    }

}

