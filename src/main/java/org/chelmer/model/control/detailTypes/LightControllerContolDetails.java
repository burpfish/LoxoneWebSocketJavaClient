package org.chelmer.model.control.detailTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.chelmer.model.control.ControlDetails;

/**
 * Created by burfo on 15/02/2017.
 */
public class LightControllerContolDetails extends ControlDetails {
    private int movementScene;

    @JsonCreator
    public LightControllerContolDetails(@JsonProperty("movementScene") int movementScene) {
        this.movementScene = movementScene;
    }

    public int getMovementScene() {
        return movementScene;
    }
}
