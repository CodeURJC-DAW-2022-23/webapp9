package com.tripscanner.TripScanner.utils;

import java.awt.*;
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

        // Title
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(12);
        Paragraph paragraph = new Paragraph("Intinerary generated with", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(20);
        paragraph = new Paragraph("TripScanner", font);
        paragraph.setSpacingAfter(10f);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        // Itinerary deatils
        font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        paragraph = new Paragraph("Name: " + itinerary.getName(), font);
        paragraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(paragraph);

        font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        paragraph = new Paragraph("Description: " + itinerary.getDescription(), font);
        paragraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(paragraph);

        font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        paragraph = new Paragraph("Created by: @" + itinerary.getUser().getUsername() + " (" + itinerary.getUser().getFirstName() + " " + itinerary.getUser().getLastName() + ")", font);
        paragraph.setSpacingAfter(10f);
        paragraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(paragraph);

        PdfPTable table = new PdfPTable(itinerary.getPlaces().size());
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{3, 3, 3});
        table.setSpacingBefore(5);

        // Itinerary places
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new Color(0xA1C6EA));
        cell.setPadding(5);
        font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(CMYKColor.WHITE);
        cell.setPhrase(new Phrase("Place name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Place description", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Destination", font));
        table.addCell(cell);

        for (Place place : itinerary.getPlaces()) {
            table.addCell(place.getName());
            table.addCell(place.getDescription());
            table.addCell(place.getDestination().getName());
        }

        document.add(table);
        document.close();
    }
}