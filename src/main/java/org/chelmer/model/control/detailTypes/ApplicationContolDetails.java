package org.chelmer.model.control.detailTypes;

import org.chelmer.model.control.ControlDetails;

/**
 * Created by burfo on 15/02/2017.
 */
public class ApplicationContolDetails extends ControlDetails {
    private String url;
    private String image;

    public ApplicationContolDetails(String url, String image) {

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
