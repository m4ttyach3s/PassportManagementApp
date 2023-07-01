package com.progetto.packController;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

public class bozza {
    public static void main(String[] args) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE", new Locale("it", "IT"));
        String dayOfWeek = date.format(formatter);

        System.out.println(dayOfWeek.toUpperCase());
    }
}
