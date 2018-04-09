package com.example.dns.smartchair;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author dnsgbl
 */
public final class ProfileManager {
    private static UserProfile usedProfile;
    private static List<UserProfile> allProfiles;

    /*! \brief Loads profiles from PersistenceManager.
 *         Used prifle and all profiles get loaded from Persistence Manager.
 */
    public static void load(Context c){
        usedProfile = PersistenceManager.loadUsed(c);
        allProfiles = PersistenceManager.loadProfiles(c);
    }

    /*! \brief Gets the profile names from list.
 *         Get all created profiles' names from list.
 */
    public static List<String> getProfileNames(){
        List<String> names = new ArrayList<>();
        for(UserProfile pr: allProfiles){
            names.add(pr.getName());
        }
        return names;
    }

    /*! \brief Sets used profile.
 *         Searches and sets the used profile by it's position in list.
 */
    public static void setUsedAtPosition(int pos, Context c){
        usedProfile = allProfiles.get(pos);
        PersistenceManager.storeUsed(c, usedProfile);
    }

    /*! \brief Gets the used profile.
 *         Returns the profile which is currently in use.
 */
    public static UserProfile getUsed(){
        return usedProfile;
    }

    /*! \brief Adds a new profile to list.
 *         A new profile gets added to the profiles' list and gets persistend by Persistence Manager.
 */
    public static void addProfile(String name, int sst, int sbt, int h, Context c){
        UserProfile up = new UserProfile(name, sst, sbt, h);
        allProfiles.add(up);
        PersistenceManager.saveAll(c, allProfiles);
        /*Toast.makeText(c, name+", "+Integer.toString(sst)+", "+Integer.toString(sbt)+", "+
        Integer.toString(h), Toast.LENGTH_LONG);*/
    }

    /*! \brief Gets the profile on a specific position.
 *        Returns a profile on a specific position in the list.
 */
    public static UserProfile getProfileAtPostion(int pos){
        return allProfiles.get(pos);
    }

    /*! \brief Updates the profile's settings.
 *         Sets the changes of a profile and makes it persistend.
 */
    public static void updateProfile(String name, int seatSquab, int seatBack, int height, Context c){
        String uName = usedProfile.getName();
        for(UserProfile p: allProfiles){       //let usedProfile reference point to item in list
            if(p.getName().equals(uName))
                usedProfile = p;
        }
        usedProfile.setName(name);
        usedProfile.setSeatSquabTilt(seatSquab);
        usedProfile.setSeatBackTilt(seatBack);
        usedProfile.setHeight(height);
        PersistenceManager.storeUsed(c, usedProfile);
        PersistenceManager.saveAll(c, allProfiles);
    }

    /*! \brief Removes profile from list.
 *         Gets the profile which is to be removed from list by its position in the list. Whether profile is currently in use it cannot be removed.
 */
    public static boolean deleteProfile(int pos, Context c){
        UserProfile p = allProfiles.get(pos);
        if(p.getName().equals(usedProfile.getName())){
            Toast.makeText(c, "Profil kann nicht gelöscht werden!", Toast.LENGTH_SHORT).show();
            return false;
        }
        allProfiles.remove(pos);
        PersistenceManager.saveAll(c, allProfiles);
        return true;
    }
}
