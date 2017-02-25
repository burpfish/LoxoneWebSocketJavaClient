package org.chelmer.model.control.detailTypes;

import org.chelmer.model.control.ControlDetails;

/**
 * Created by burfo on 15/02/2017.
 */
public class LightControllerContolDetails extends ControlDetails {
    private int movementScene;

    public LightControllerContolDetails(int movementScene) {

        this.movementScene = movementScene;
    }

    public int getMovementScene() {
        return movementScene;
    }
}
