module com.playlistversionuno {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;
    requires javafx.media;
    
    opens com.playlistversionuno to javafx.fxml;
    exports com.playlistversionuno;
}
