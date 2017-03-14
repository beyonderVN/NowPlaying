package ngohoanglong.com.nowplaying.data.model;

import java.io.Serializable;

/**
 * Created by Long on 2/8/2017.
 */

public class User implements Serializable {

    private String id;
    private String name;
    private String photo_profile;

    public User() {
    }

    public User(String name, String photo_profile, String id) {
        this.name = name;
        this.photo_profile = photo_profile;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto_profile() {
        return photo_profile;
    }

    public void setPhoto_profile(String photo_profile) {
        this.photo_profile = photo_profile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

