package com.information.rxjavaapplication.square;

/**
 * JSON응답에 필요한 정보데이터를 추출
 * 모든 필드를 정의할 필요 없이 원하는 정보만 JSON(이름, 데이터타입, 구조0에 맞게 정의하면 GSON에서 디코딩하여 원하는 값을
 * Contributor 클래스 필드에 설정함
 */
public class Contributor {
    String login;
    String url;
    int id;

    @Override
    public String toString() {
        return "login : " + login + " id : " + id + " url : " + url;
    }
}
