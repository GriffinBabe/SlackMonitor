package eu.bestbrusselsulb.model.html;

import java.io.Serializable;

/**
 * Package private class containing
 * all the emoji information.
 */
class EmojiData implements Serializable {

    private String name;
    private String unified;
    private String nonQualified;
    private String docomo;
    private String au;
    private String softbank;
    private String google;
    private String image;
    private int sheet_x;
    private int sheet_y;
    private String short_name;
    private String[] short_names;
    private String text;
    private String[] texts;
    private String category;
    private int sort_order;
    private String added_in;
    private boolean has_img_apple;
    private boolean has_img_google;
    private boolean has_img_twitter;
    private boolean has_img_facebook;

    String getUnified() {
        return unified;
    }

    String[] getShortNames() {
        return short_names;
    }


}