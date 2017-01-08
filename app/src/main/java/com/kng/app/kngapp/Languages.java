package com.kng.app.kngapp;

/**
 * Created by GS-0913 on 16-12-2016.
 */

public class Languages {
    private int languageId;
    private String languageCode;
    private String languageName;

    public Languages(int languageId, String languageCode, String languageName) {
        this.languageId = languageId;
        this.languageCode = languageCode;
        this.languageName = languageName;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    @Override
    public String toString() {
        return "languageCode"+this.getLanguageCode();


    }
}
