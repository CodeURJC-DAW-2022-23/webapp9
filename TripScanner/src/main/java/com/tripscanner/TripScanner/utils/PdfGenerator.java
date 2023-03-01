package com.tripscanner.TripScanner.utils;

import java.io.IOException;

import com.tripscanner.TripScanner.model.Itinerary;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tripscanner.TripScanner.model.Place;

public class PdfGenerator {
    public void generate(Itinerary itinerary, HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font fontTiltle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTiltle.setSize(20);
        Paragraph paragraph1 = new Paragraph(itinerary.getName() + " [Itinerary]", fontTiltle);
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph1);

        PdfPTable table = new PdfPTable(itinerary.getPlaces().size());
        table.setWidthPercentage(100f);
        table.setWidths(new int[] {3,3,3});
        table.setSpacingBefore(5);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(CMYKColor.BLUE);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(CMYKColor.WHITE);
        cell.setPhrase(new Phrase("Place name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Place description", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Destination", font));
        table.addCell(cell);

        for (Place place: itinerary.getPlaces()) {
            table.addCell(place.getName());
            table.addCell(place.getDescription());
            table.addCell(place.getDestination().getName());
        }

        document.add(table);
        document.close();
    }
}