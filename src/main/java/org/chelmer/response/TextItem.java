package org.chelmer.response;

import org.chelmer.model.entity.LoxUuid;

/**
 * Created by burfo on 21/02/2017.
 */

public class TextItem {
    private final LoxUuid uuid;
    private final LoxUuid iconUuid;
    private final long textLen;
    private final String text;

    public TextItem(LoxUuid uuid, LoxUuid iconUuid, long textLen, String text) {
        this.uuid = uuid;
        this.iconUuid = iconUuid;
        this.textLen = textLen;
        this.text = text;
    }

    public LoxUuid getUuid() {
        return uuid;
    }

    public LoxUuid getIconUuid() {
        return iconUuid;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "TextItem{" +
                "uuid=" + uuid +
                ", iconUuid=" + iconUuid +
                ", text='" + text + '\'' +
                '}';
    }
}
