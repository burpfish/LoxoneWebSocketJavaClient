package org.chelmer.model.category;

import com.fasterxml.jackson.annotation.JacksonInject;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.model.UuidComponent;
import org.chelmer.model.entity.LoxUuid;

public class Category implements UuidComponent {
    private final LoxUuid uuid;
    private final String name;
    private final String image;
    private final int defaultRating;
    private final boolean isFavorite;
    private final CategoryType type;
    private final String color;
    private Double value = null;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Category(LoxUuid uuid, String name, String image, int defaultRating, boolean isFavorite, CategoryType type, String color) {
        this.uuid = uuid;
        this.name = name;
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

    public String getName() {
        return name;
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
