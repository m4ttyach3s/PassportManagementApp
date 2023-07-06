package com.progetto;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class PDFGenerator {
    private String fileName;
    private String downloadFolder;

    public PDFGenerator(String fileName) {
        this.fileName = fileName;
        this.downloadFolder = getDownloadFolder();
    }

    public void makePDF(List<String> textList, String servizio, String causaR) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(new File(downloadFolder, fileName)));
            document.open();
            document.setPageSize(PageSize.A4);

            Image header = Image.getInstance("src/main/java/com/progetto/header.png");
            Image footer = Image.getInstance("src/main/java/com/progetto/footer.png");

            float pageWidth = PageSize.A4.getWidth();
            float headerScaleFactor = pageWidth / header.getWidth();
            float footerScaleFactor = pageWidth / footer.getWidth();

            header.scalePercent(headerScaleFactor * 100);
            footer.scalePercent(footerScaleFactor * 50);

            float adjustedHeaderHeight = header.getHeight() * headerScaleFactor;
            float adjustedFooterHeight = footer.getHeight() * footerScaleFactor;
            header.setAbsolutePosition(0, document.top() - adjustedHeaderHeight);
            footer.setAbsolutePosition(147, adjustedFooterHeight-200);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            float paragraphPosition = adjustedHeaderHeight + 30f;
            float paragraphSpacing = 20f;

            if(servizio.equals("Ritiro")){
                Paragraph paragraph = new Paragraph();
                String SCRIPT = getScriptRitiro(textList);
                paragraph.setSpacingAfter(paragraphSpacing);
                paragraph.setSpacingBefore(paragraphPosition);
                paragraph.add(SCRIPT);
                document.add(paragraph);
            } else if(servizio.equals("Rilascio") && (causaR.equals("scadenza") || causaR.equals("deterioramento"))){
                Paragraph paragraph = new Paragraph();
                String SCRIPT = getScriptRilascioSD(textList, causaR);
                paragraph.setSpacingAfter(paragraphSpacing);
                paragraph.setSpacingBefore(paragraphPosition);
                paragraph.add(SCRIPT);
                document.add(paragraph);
            } else if(servizio.equals("Rilascio") && (causaR.equals("furto") || causaR.equals("smarrimento"))){
                Paragraph paragraph = new Paragraph();
                String SCRIPT = getScriptRilascioFS(textList, causaR);
                paragraph.setSpacingAfter(paragraphSpacing);
                paragraph.setSpacingBefore(paragraphPosition);
                paragraph.add(SCRIPT);
                document.add(paragraph);
            } else {
                Paragraph paragraph = new Paragraph();
                String SCRIPT = getScriptRilascioPS(textList);
                paragraph.setSpacingAfter(paragraphSpacing);
                paragraph.setSpacingBefore(paragraphPosition);
                paragraph.add(SCRIPT);
                document.add(paragraph);
            }
            document.add(footer);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getScriptRilascioPS(List<String> textList) {
        String output = "Prenotazione numero #"+textList.get(0) +" effettuata per: "+textList.get(8) +" \nNome: "+textList.get(6) +" \nCognome: "+textList.get(7)+
                "\nLa prenotazione è stata effettuata per la sede di: "+textList.get(1)+" il giorno "+textList.get(3) +" alle ore "+textList.get(2)
                + "\nOperazione richiesta: Rilascio primo passaporto.\n\n"+
                            """
                
                            DOCUMENTI DA PRESENTARE:
                               
                Al fine di poter ottenere delle prestazioni ottimali, Le ricordiamo che deve portare con sé:
                1. Una marca da bollo da 73.50€
                2. Una copia della sua carta di identità
                3. Una copia del suo codice fiscale
                4. Il passaporto in scadenza
                5. 2 foto formato tessera con il volto ben visibile
                6. Ricevuta del pagamento da 42.50€ presso il conto corrente della questura:
                                                       IBAN: IT07 Z 03069 02126 100000046021
                               
                Si ricordi di presentarsi puntuale e di presentare allo sportello il suo numero di prenotazione
                e/o questo PDF in formato cartaceo oppure elettronico.
                SI RICORDA INOLTRE: in caso di cittadino minorenne l'utente deve essere accompagnato allo sportello da un genitore,
                tutore o di chi ne fa le veci.
                """;
        return output;
    }

    private String getScriptRilascioFS(List<String> textList, String causaR) {
        String output = "Prenotazione numero #"+textList.get(0) +" effettuata per: "+textList.get(8) +" \nNome: "+textList.get(6) +" \nCognome: "+textList.get(7)+
                "\nLa prenotazione è stata effettuata per la sede di: "+textList.get(1)+" il giorno "+textList.get(3) +" alle ore "+textList.get(2)
                + "\nOperazione richiesta: Rilascio per "+causaR+ """
                \n\n
                            DOCUMENTI DA PRESENTARE:
                               
                Al fine di poter ottenere delle prestazioni ottimali, Le ricordiamo che deve portare con sé:
                1. Una marca da bollo da 73.50€
                2. Una copia della sua carta di identità
                3. Una copia del suo codice fiscale
                4. Due copie della denuncia di smarrimento
                5. 2 foto formato tessera con il volto ben visibile
                6. Ricevuta del pagamento da 42.50€ presso il conto corrente della questura:
                                                       IBAN: IT07 Z 03069 02126 100000046021
                               
                Si ricordi di presentarsi puntuale e di presentare allo sportello il suo numero di prenotazione
                e/o questo PDF in formato cartaceo oppure elettronico.
                SI RICORDA INOLTRE: in caso di cittadino minorenne l'utente deve essere accompagnato allo sportello da un genitore,
                tutore o di chi ne fa le veci.
                """;
        return output;
    }

    private String getScriptRilascioSD(List<String> textList, String causaR) {
        String output = "Prenotazione numero #"+textList.get(0) +" effettuata per: "+textList.get(8) +" \nNome: "+textList.get(6) +" \nCognome: "+textList.get(7)+
                "\nLa prenotazione è stata effettuata per la sede di: "+textList.get(1)+" il giorno "+textList.get(3) +" alle ore "+textList.get(2)
                + "\nOperazione richiesta: Rilascio per "+causaR+ """
                \n\n
                            DOCUMENTI DA PRESENTARE:
                               
                Al fine di poter ottenere delle prestazioni ottimali, Le ricordiamo che deve portare con sé:
                1. Una marca da bollo da 73.50€
                2. Una copia della sua carta di identità
                3. Una copia del suo codice fiscale
                4. Il passaporto in scadenza
                5. 2 foto formato tessera con il volto ben visibile
                6. Ricevuta del pagamento da 42.50€ presso il conto corrente della questura:
                                                       IBAN: IT07 Z 03069 02126 100000046021
                               
                Si ricordi di presentarsi puntuale e di presentare allo sportello il suo numero di prenotazione
                e/o questo PDF in formato cartaceo oppure elettronico.
                SI RICORDA INOLTRE: in caso di cittadino minorenne l'utente deve essere accompagnato allo sportello da un genitore,
                tutore o di chi ne fa le veci.
                """;
        return output;
    }

    private String getScriptRitiro(List<String> textList) {
        String output = "Prenotazione numero #"+textList.get(0) +" effettuata per: "+textList.get(8) +" \nNome: "+textList.get(6) +" \nCognome: "+textList.get(7)+
                "\nLa prenotazione è stata effettuata per la sede di: "+textList.get(1)+" il giorno "+textList.get(3) +" alle ore "+textList.get(2)
                + """
                \nOperazione richiesta: Ritiro passaporto

                Al fine di poter ottenere delle prestazioni ottimali, Le ricordiamo che deve portare con sé:
                1. Il documento di conferma rilasciato dalla sede il giorno della richiesta di rilascio
                2. Un documento in corso di validità che attesti la sua identità (carta di identità, patente, passaporto estero)
                3. Una delega con allegato la copia della carta di identità del delegato e del delegante

                Si ricordi di presentarsi puntuale e di presentare allo sportello il suo numero di prenotazione
                e/o questo PDF in formato cartaceo oppure elettronico.
                SI RICORDA INOLTRE: in caso di cittadino minorenne l'utente deve essere accompagnato allo sportello da un genitore,
                tutore o di chi ne fa le veci.
                """;
        return output;
    }


    private String getDownloadFolder() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            // sono su Windows
            return System.getProperty("user.home") + "\\Downloads";
        } else if (os.contains("mac")) {
            // sono su macOS
            return System.getProperty("user.home") + "/Downloads";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            // sono su Linux o Unix-based
            return System.getProperty("user.home") + "/Downloads";
        } else {
            // altro OS che non conosco
            System.err.println("Unsupported operating system: " + os);
            return null;
        }
    }
}
