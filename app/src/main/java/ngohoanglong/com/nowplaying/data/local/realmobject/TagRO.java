package ngohoanglong.com.nowplaying.data.local.realmobject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Long on 4/10/2017.
 */

public class TagRO extends RealmObject {
    @PrimaryKey
    String tagName;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TagRO{");
        sb.append("tagName='").append(tagName).append('\'').append('\n');
        sb.append('}');
        return sb.toString();
    }
}
