package org.chelmer.model.control.detailTypes;

import org.chelmer.model.control.ControlDetails;

import java.util.Map;

/**
 * Created by burfo on 15/02/2017.
 */
public class RemoteContolDetails extends ControlDetails {
    private Map<String, ModeList> modeList;
    private int favoritePad;

    public RemoteContolDetails(Map<String, ModeList> modeList, int favoritePad) {

        this.modeList = modeList;
        this.favoritePad = favoritePad;
    }

    public Map<String, ModeList> getModeList() {
        return modeList;
    }

    public int getFavoritePad() {
        return favoritePad;
    }

}
