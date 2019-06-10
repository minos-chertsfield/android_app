package com.minos.slaver;


/*
课程类
 */
public class Lesson {

    private String lessonName;
    private int lessonId;

    public Lesson(String lessonName, int lessonId) {
        this.lessonName = lessonName;
        this.lessonId = lessonId;
    }

    public String getLessonName() {
        return lessonName;
    }

    public int getLessonId() {
        return lessonId;
    }
}
