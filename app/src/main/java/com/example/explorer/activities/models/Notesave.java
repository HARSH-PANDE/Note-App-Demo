package com.example.explorer.activities.models;

public class Notesave {
    private String CityNoteTitle;
    private String CityNoteSubtitle;
    private String CityNoteText;
    private String DateTime;

    public Notesave() {
    }

    public Notesave(String cityNoteTitle, String cityNoteSubtitle, String cityNoteText, String dateTime) {
        CityNoteTitle = cityNoteTitle;
        CityNoteSubtitle = cityNoteSubtitle;
        CityNoteText = cityNoteText;
        DateTime = dateTime;
    }

    public String getCityNoteTitle() {
        return CityNoteTitle;
    }

    public void setCityNoteTitle(String cityNoteTitle) {
        CityNoteTitle = cityNoteTitle;
    }

    public String getCityNoteSubtitle() {
        return CityNoteSubtitle;
    }

    public void setCityNoteSubtitle(String cityNoteSubtitle) {
        CityNoteSubtitle = cityNoteSubtitle;
    }

    public String getCityNoteText() {
        return CityNoteText;
    }

    public void setCityNoteText(String cityNoteText) {
        CityNoteText = cityNoteText;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }
}
