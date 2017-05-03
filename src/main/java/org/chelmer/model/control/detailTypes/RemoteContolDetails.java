package org.chelmer.model.control.detailTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.chelmer.model.control.ControlDetails;

import java.util.Map;

/**
 * Created by burfo on 15/02/2017.
 */
public class RemoteContolDetails extends ControlDetails {
    private Map<String, ModeList> modeList;
    private int favoritePad;

    @JsonCreator
    public RemoteContolDetails(@JsonProperty("modeList") Map<String, ModeList> modeList, @JsonProperty("favoritePad") int favoritePad) {

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
