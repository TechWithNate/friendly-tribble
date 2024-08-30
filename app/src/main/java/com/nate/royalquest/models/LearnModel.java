package com.nate.royalquest.models;

public class LearnModel {

        private String title;
        private String date;
        private String content;

    public LearnModel() {
    }

    public LearnModel(String title, String date, String content) {
            this.title = title;
            this.date = date;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public String getDate() {
            return date;
        }

        public String getContent() {
            return content;
        }

}
