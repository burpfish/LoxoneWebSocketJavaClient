package org.chelmer.model.control.controlTypes;

import org.chelmer.model.control.Control;
import org.chelmer.model.control.ControlType;
import org.chelmer.model.entity.LoxUuid;

/**
 * Created by burfo on 14/02/2017.
 */
public class LightingControl extends Control {
    private boolean presence;

    public LightingControl(String name, ControlType type, LoxUuid uuidAction, int defaultRating, boolean isSecured) {
        super(name, type, uuidAction, defaultRating, isSecured);

        this.presence = presence;
    }

    public boolean getPresence() {
        return presence;
    }

    public void setPresence(boolean presence) {
        this.presence = presence;
    }

//    public LoxUuid getActiveSceneState() {
//        return getStates().getStateAsUuid("activeScene");
//    }
//
//    public LoxUuid getXceneListState() {
//        return getStates().getStateAsUuid("sceneList");
//    }
//
//    public LoxUuid getPresenceFromState() {
//        return getStates().getStateAsUuid("presenceFrom");
//    }
//
//    public LoxUuid getPresenceToState() {
//        return getStates().getStateAsUuid("presenceTo");
//    }


//    "detailTypes":{
//
//    },
//
//        "subControls":{
//        "0e4fa2f3-00fc-f9a4-ffff504f94000000/AI1":{
//            "name":"Dannys bedroom light (central)",
//                    "type":"Switch",
//                    "uuidAction":"0e4fa2f3-00fc-f9a4-ffff504f94000000/AI1",
//                    "defaultRating":0,
//                    "isFavorite":false,
//                    "isSecured":false,
//                    "presence":true,
//                    "states":{
//                "active":"0e4fa2f3-00fc-f995-fffff4f3322cc74f"
//            }
//        }
//    }
//    }
}
