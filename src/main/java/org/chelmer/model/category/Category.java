package org.chelmer.model.category;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.model.ComponentBase;
import org.chelmer.model.entity.LoxUuid;

public class Category extends ComponentBase {
    private final LoxUuid uuid;
    private final String image;
    private final int defaultRating;
    private final boolean isFavorite;
    private final CategoryType type;
    private final String color;

    @JsonCreator
    public Category(@JsonProperty("uuid") LoxUuid uuid, @JsonProperty("name") String name, @JsonProperty("image") String image, @JsonProperty("defaultRating") int defaultRating, @JsonProperty("isFavorite") boolean isFavorite, @JsonProperty("type") CategoryType type, @JsonProperty("color") String color) {
        super(name);
        this.uuid = uuid;
        this.image = image;
        this.defaultRating = defaultRating;
        this.isFavorite = isFavorite;
        this.type = type;
        this.color = color;
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

    public CategoryType getType() {
        return type;
    }

    public String getColor() {
        return color;
    }
}
