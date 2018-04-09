package com.example.dns.smartchair;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dnsgbl
 */
public class MainActivity extends AppCompatActivity {
    //Layout
    Button btnChange;
    Button btnEdit;
    Button btnNFC;
    TextView profileText;
    //Profil
    UserProfile used;
    String seatSquab;
    String seatBack;
    String height;
    //NFC
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context;

    /*! \brief Activity is started.
*         Called when the activity is starting.
*         @savedInstanceState 	Bundle: If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProfileManager.load(getApplicationContext());

        List<UserProfile> profiles = new ArrayList<>();

        if(profiles.equals(null)) {
            //Only for the first time
            UserProfile p1 = new UserProfile("Default", 1, 2, 1);

            profiles.add(p1);
            PersistenceManager.saveAll(getApplicationContext(), profiles);
            PersistenceManager.storeUsed(getApplicationContext(), p1);
        }
        setButtons();
        setTexts();

        //NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {

            Toast.makeText(this, "Dieses Gerät unterstützt kein NFC!", Toast.LENGTH_LONG).show();
            //finish();
        }

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[] { tagDetected };
    }

    /*! \brief Button is pressed.
 *         Set a message to be sent when a button is pressed.
 */
    protected void setButtons(){
        btnChange = (Button) findViewById(R.id.btnChange);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),ProfileList.class);
                startActivity(in);
            }
        });
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), EditProfile.class);
                Bundle b = new Bundle();
                b.putInt("new",0);
                in.putExtras(b);
                startActivity(in);
            }
        });

        used = ProfileManager.getUsed();
        seatSquab = Integer.toString(used.getSeatSquabTilt());
        seatBack = Integer.toString(used.getSeatBackTilt());
        height = Integer.toString(used.getHeight());

        btnNFC = (Button) findViewById(R.id.btnNFC);
        btnNFC.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {
                    if(myTag == null) {
                        Toast.makeText(getApplicationContext(), "Kein NFC-Tag gefunden!", Toast.LENGTH_LONG).show();

                    } else {
                        write(seatSquab+ " "+ seatBack + " " + height, myTag);
                        Toast.makeText(getApplicationContext(), "NFC-Tag erfolgreich beschrieben!", Toast.LENGTH_LONG ).show();
                    }

                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Fehler beim Beschreiben des NFC-Tags!", Toast.LENGTH_LONG ).show();
                    e.printStackTrace();
                } catch (FormatException e) {
                    Toast.makeText(getApplicationContext(), "Fehler beim Beschreiben des NFC-Tags!", Toast.LENGTH_LONG ).show();
                    e.printStackTrace();
                }
            }
        });
    }

    /*! \brief Button is pressed.
  *         Sets the text of this TextComponent to the specified text.
  */
    protected void setTexts(){
        profileText = (TextView) findViewById(R.id.profileText);
        String profileName = ProfileManager.getUsed().getName();
        profileText.setText(profileName);
        //Toast.makeText(getApplicationContext(), getApplicationContext().getFilesDir().toString(), Toast.LENGTH_LONG).show();
    }

    /*! \brief Recovery of tag object
*         Tests whether intent filter matches the discovery of and NFC-enabled device and processes the intent.
*         Calls buildTagViews(msgs).
*/
    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }

    /*! \brief Builds the tag view.
*         Getting the information an and the NDEF content from the NFC activity.
*/
    private void buildTagViews(NdefMessage[] msgs) {

        if (msgs == null || msgs.length == 0){

            return;
        }

        String text = "";
        String tagId = new String(msgs[0].getRecords()[0].getType());

        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            // Get the Text
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }


        //Toast.makeText(context, "NFC Content: " + text, Toast.LENGTH_LONG);
    }

    /*! \brief Writes the message to a deteced tag.
*         Gets the instance of NDEF for the tag and enables I/O.
*         Wrties the message to the tag and closes the connection.
*/
    private void write(String text, Tag tag) throws IOException, FormatException {
        NdefRecord[] records = { createRecord(text) };
        NdefMessage message = new NdefMessage(records);
        // Get an instance of Ndef for the tag.
        Ndef ndef = Ndef.get(tag);
        // Enable I/O
        ndef.connect();
        // Write the message
        ndef.writeNdefMessage(message);
        // Close the connection
        ndef.close();
    }

    /*! \brief Creating an NDEF record
*         Creates an NDEF message with a single text record, with language
*         code "en" and the given text, encoded using US-ASCII.
*/
    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang       = "en";
        byte[] textBytes  = text.getBytes();
        byte[] langBytes  = lang.getBytes("US-ASCII");
        int    langLength = langBytes.length;
        int    textLength = textBytes.length;
        byte[] payload    = new byte[1 + langLength + textLength];

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1,              langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,  NdefRecord.RTD_TEXT,  new byte[0], payload);

        return recordNFC;
    }
    /*! \brief Set launchMode to "singleTop" in activity's package
  *          the activity will not be launched if it is already running at the top of the history stack.
  *          It will not relaunch just show from stack.
  */
    @Override
    protected void onNewIntent(Intent intent) {
         setIntent(intent);
         readFromIntent(intent);
        Toast.makeText(this, "NFC-Tag erkannt!", Toast.LENGTH_LONG).show();

         if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
             myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
         }
     }

    /*! \brief NFC discovery is paused.
  *         The NFC discovery in the foreground gets deactivated when the activity is paused (no longer in the foreground).
  */
    @Override
    public void onPause(){
        super.onPause();
        WriteModeOff();
    }
    /*! \brief NFC discovery is activated.
  *         The activation of the NFC discovery in the foreground will be initialized at the activity wake up.
  */
    @Override
    public void onResume(){
        super.onResume();
        WriteModeOn();
    }
    /*! \brief Enable foreground dispatch to the given Activity.
  *         This will give priority to the foreground activity when dispatching a discovered Tag to the application.
  */
    private void WriteModeOn(){
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }
    /*! \brief Disable foreground dispatch
  *         Disable foreground dispatch to the given activity.
  */
    private void WriteModeOff(){
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }

}

