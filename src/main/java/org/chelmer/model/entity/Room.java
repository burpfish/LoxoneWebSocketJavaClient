package org.chelmer.model.entity;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.model.ComponentBase;

public class Room extends ComponentBase {
    private final LoxUuid uuid;
    private final String image;
    private final int defaultRating;
    private final boolean isFavorite;

    @JsonCreator
    public Room(@JsonProperty("uuid") LoxUuid uuid, @JsonProperty("name") String name, @JsonProperty("image") String image, @JsonProperty("defaultRating") int defaultRating, @JsonProperty("isFavorite") boolean isFavorite) {
        super(name);

        this.uuid = uuid;
        this.image = image;
        this.defaultRating = defaultRating;
        this.isFavorite = isFavorite;
    }

    @JacksonInject
    public void setRegistry(UuidComponentRegistry registry) {
        registry.register(uuid, this);
    }

    public LoxUuid getUuid() {
        return uuid;
    }

    public String getImage() {
        return image;
    }

    public int getDefaultRating() {
        return defaultRating;
    }

    public boolean isFavorite() {
        return isFavorite;
    }
}
