package org.chelmer.model.control.detailTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.chelmer.model.control.ControlDetails;

/**
 * Created by burfo on 15/02/2017.
 */
public class ApplicationContolDetails extends ControlDetails {
    private String url;
    private String image;

    @JsonCreator
    public ApplicationContolDetails(@JsonProperty("url") String url, @JsonProperty("image") String image) {
        this.url = url;
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }
}
