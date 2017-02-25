package org.chelmer.model.control.controlTypes;

import org.chelmer.model.control.Control;
import org.chelmer.model.control.ControlType;
import org.chelmer.model.entity.LoxUuid;

/**
 * Created by burfo on 14/02/2017.
 */
public class RemoteControl extends Control {

    public RemoteControl(String name, ControlType type, LoxUuid uuidAction, int defaultRating, boolean isSecured) {
        super(name, type, uuidAction, defaultRating, isSecured);
    }

//    public LoxUuid getModeState() {
//        return getStates().getStateAsUuid("mode");
//    }
//    public LoxUuid getTimeoutState() {
//        return getStates().getStateAsUuid("timeout");
//    }
//    public LoxUuid getActiveState() {
//        return getStates().getStateAsUuid("active");
//    }


//    {
//
//            "detailTypes":{
//        "favoritePad":0,
//                "modeList":{
//            "1":{
//                "name":"Music",
//                        "favoritePad":0,
//                        "usedButtons":[
//                "power",
//                        "power",
//                        "mute",
//                        "volplus",
//                        "volminus",
//                        "play",
//                        "pause",
//                        "next",
//                        "previous"
//                  ]
//            }
//        }
//    },
//
//    }
//}
}
