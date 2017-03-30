package ngohoanglong.com.nowplaying;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ngohoanglong.com.nowplaying.display.v2.MainViewModel;

/**
 * Created by Long on 3/29/2017.
 */

public class AppState implements Serializable{
    List<MainViewModel.Section> sections = new ArrayList<>();

    public List<MainViewModel.Section> getSections() {
        return sections;
    }

    public void setSections(List<MainViewModel.Section> sections) {
        this.sections = sections;
    }
}
