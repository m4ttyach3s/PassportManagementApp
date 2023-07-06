module com.progetto.ver1_0 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    //requires org.controlsfx.controls;
    //requires com.dlsc.formsfx;
    //requires net.synedra.validatorfx;
    //requires org.kordamp.bootstrapfx.core;
    //requires eu.hansolo.tilesfx;
    requires java.sql;
    requires itextpdf;
    requires barcodes;
    //requires kernel;

    opens com.progetto.packController to javafx.fxml;
    exports com.progetto.packController;
}