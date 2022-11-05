module com.example.gymmembershipgui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.gymmembershipgui to javafx.fxml;
    exports com.example.gymmembershipgui;
}