package by.jum.locationbyip.models;

import android.graphics.Bitmap;

public class ItemInformation {
    private String infForTextView;
    private Bitmap flag;

    public String getInfForTextView() {
        return infForTextView;
    }

    public void setInfForTextView(String infForTextView) {
        this.infForTextView = infForTextView;
    }

    public Bitmap getFlag() {
        return flag;
    }

    public void setFlag(Bitmap flag) {
        this.flag = flag;
    }
}
