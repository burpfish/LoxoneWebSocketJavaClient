package org.chelmer.response;

/**
 * Created by burfo on 21/02/2017.
 */
public class MessageNotificationItem {
    private final byte marker;
    private final MessageType messageType;
    private final byte infoFlags;
    private final byte unused;
    private final long length;

    public MessageNotificationItem(byte marker, byte messageType, byte infoFlags, byte unused, long length) {
        this.marker = marker;
        this.messageType = MessageType.fromValue(messageType);
        this.infoFlags = infoFlags;
        this.unused = unused;
        this.length = length;
    }

    public static MessageNotificationItem nullValue() {
        return new MessageNotificationItem((byte) 0, (byte) MessageType.UNDEFINED.getTypeId(), (byte) 0, (byte) 0, (long) 0);
    }

    @Override
    public String toString() {
        return "MessageNotificationItem{" +
                "marker=" + marker +
                ", messageType=" + messageType +
                ", infoFlags=" + infoFlags +
                ", unused=" + unused +
                ", length=" + length +
                '}';
    }

    public byte getMarker() {
        return marker;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public byte getInfoFlags() {
        return infoFlags;
    }

    public byte getUnused() {
        return unused;
    }

    public long getLength() {
        return length;
    }
}
