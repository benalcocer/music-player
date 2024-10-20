module com.example.mediaplayer {
    requires javafx.controls;
    requires reactfx;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires unirest.java;
    requires javafx.media;
    requires java.dotenv;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires java.logging;
    requires org.apache.xmlgraphics.batik.transcoder;
    requires java.desktop;
    requires javafx.swing;

    exports com.example.mediaplayer;
    exports com.example.views;
    exports com.example.models;
    exports com.example.events;
    exports com.example.fxutil;
    opens com.example.models to com.fasterxml.jackson.databind;
    exports com.example.library;
    exports com.example.playlist;
    exports com.example;
    opens com.example to com.fasterxml.jackson.databind;
    exports com.example.util;
    opens com.example.util to com.fasterxml.jackson.databind;
    exports com.example.login;
    exports com.example.windows;
    opens com.example.windows to com.fasterxml.jackson.databind;
}