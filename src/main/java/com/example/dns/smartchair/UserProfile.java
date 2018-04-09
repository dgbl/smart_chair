package com.example.dns.smartchair;

/**
 *
 * @author dnsgbl
 */
public class UserProfile {
    private String name;
    private int seatSquabTilt;      //Neigung der Sitzfläche
    private int seatBackTilt;       //Neigung der Rückenlehne
    private int height;             //Höhe des Stuhls

	
    /*! \brief Constructor of user prfile.
 *         User profile gets constructed.
 */
    public UserProfile(String s, int sst, int sbt, int h){
        name = s;
        seatSquabTilt = sst;
        seatBackTilt = sbt;
        height = h;
    }

    /*! \brief Get name.
 *         Returns user's name.
 */
    public final String getName(){
        return name;
    }

    /*! \brief Set name.
 *         Sets user's name to delivered parameter.
 */
    public final void setName(String name){
        this.name = name;
    }

    /*! \brief Gets leg rest tilt.
 *         Returns the tilt of leg rest position.
 */
    public final int getSeatSquabTilt(){
        return seatSquabTilt;
    }

	    /*! \brief Sets leg rest tilt.
 *         Sets the tilt of leg rest position to delivered value.
 */
    public final void setSeatSquabTilt(int s){
        seatSquabTilt = s;
    }
	
    /*! \brief Gets back rest tilt.
 *         Returns the tilt of back rest position.
 */
    public final int getSeatBackTilt(){
        return seatBackTilt;
    }

	    /*! \brief Sets back rest tilt.
 *         Sets the tilt of back rest position to delivered value.
 */
    public final void setSeatBackTilt(int s){
        seatBackTilt = s;
    }

	    /*! \brief Gets height.
 *         Returns the height of the chair.
 */
    public final int getHeight(){
        return height;
    }
	
	    /*! \brief Sets height.
 *         Sets the height of the chair to delivered value.
 */
    public final void setHeight(int h){
        height = h;
    }
}
