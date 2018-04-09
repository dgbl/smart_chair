package com.example.dns.smartchair;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public final class PersistenceManager {


    /*! \brief Loads profiles from file.
 *         Reads profiles from file and adds them to profile list.
 */
    public static final List<UserProfile> loadProfiles(Context c){
        List<UserProfile> profiles = new ArrayList<>();
        File myFile = new File(c.getFilesDir()+"/"+R.string.file_profiles);
        try{
            FileReader fr = new FileReader(myFile);
            BufferedReader bur = new BufferedReader(fr);
            String line = "";
            String all = "";
            while((line = bur.readLine()) != null){
                all += line;


            }
            JSONArray jAr = new JSONArray(all);
            //Toast.makeText(c, jAr.toString(), Toast.LENGTH_LONG).show();
            for(int i =  0; i < jAr.length(); ++i){
                JSONObject obj = jAr.getJSONObject(i);
                String name = obj.getString("name");
                int height = obj.getInt("height");
                int seatsquab = obj.getInt("seatsquab");
                int seatback = obj.getInt("seatback");
                UserProfile pr = new UserProfile(name, seatsquab, seatback, height);
                profiles.add(pr);
            }
        }
        catch(java.lang.Exception e){
            Toast.makeText(c, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return profiles;
    }

    /*! \brief Saves all profiles from list.
 *         Saves all profiles to list and creates a JSON file.
 */
    public static final void saveAll(Context c, List<UserProfile> pr){
        File myFile = new File(c.getFilesDir()+"/"+R.string.file_profiles);
        try{
            FileWriter fw = new FileWriter(myFile);
            JSONArray jAr = new JSONArray();
            for(int i = 0; i < pr.size(); ++i){
                JSONObject obj = new JSONObject();
                obj.put("name", pr.get(i).getName());
                obj.put("height", pr.get(i).getHeight());
                obj.put("seatsquab", pr.get(i).getSeatSquabTilt());
                obj.put("seatback", pr.get(i).getSeatBackTilt());
                jAr.put(obj);
            }
            fw.write(jAr.toString());
            fw.close();
        }
        catch(java.lang.Exception e){
            Toast.makeText(c, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /*! \brief Stores the used profile.
 *         JSON object gets information of currently used profil and creates file.
 */
    public static final void storeUsed(Context c, UserProfile p){
        JSONObject obj = new JSONObject();
        File myFile = new File(c.getFilesDir()+"/"+R.string.file_used);
        try {
            obj.put("name", p.getName());
            obj.put("height", p.getHeight());
            obj.put("seatsquab", p.getSeatSquabTilt());
            obj.put("seatback", p.getSeatBackTilt());
            String saveObj = obj.toString();
            FileWriter fw = new FileWriter(myFile);
            fw.write(saveObj);
            fw.close();
            //Toast.makeText(c, saveObj, Toast.LENGTH_LONG).show();
        }
        catch(java.lang.Exception e){
            Toast.makeText(c, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /*! \brief Loads currently used profil.
 *         Reads currently usee profile's JSON file and returns it.
 */
    public static final UserProfile loadUsed(Context c){

        File myFile = new File(c.getFilesDir()+"/"+R.string.file_used);
        UserProfile pr = null;
        try{
            FileReader fr = new FileReader(myFile);
            BufferedReader bur = new BufferedReader(fr);
            String line = "";
            String all = "";
            while((line = bur.readLine()) != null){
                all += line;

            }
            JSONObject obj = new JSONObject(all);
            //Toast.makeText(c, obj.toString(), Toast.LENGTH_LONG).show();
            String name = obj.getString("name");
            int height = obj.getInt("height");
            int seatsquab = obj.getInt("seatsquab");
            int seatback = obj.getInt("seatback");
            pr = new UserProfile(name, height, seatsquab, seatback);
        }
        catch(java.lang.Exception e){
            Toast.makeText(c, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return pr;
    }
}
