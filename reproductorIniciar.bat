@echo off
set FXPATH=%~dp0javafx-sdk-21.0.7\lib
java --module-path "%FXPATH%" --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics,javafx.media -jar version_uno-main.jar
