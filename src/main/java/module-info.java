module com.codechum.csit228go1fx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.codechum.csit228go1fx to javafx.fxml;
    exports com.codechum.csit228go1fx;
}