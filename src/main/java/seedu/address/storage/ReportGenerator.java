package seedu.address.storage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import seedu.address.model.entity.body.Body;

//@@author bernicechio
/**
 * Represents a report generator and the ability to be generate aå report.
 */
public class ReportGenerator {

    /**
     * Generates a PDF report for the specific body.
     *
     * @param body which is used to generate the report.
     */
    public static void generate(Body body) {
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document,
                    new FileOutputStream(String.format("Report %s.pdf", body.getIdNum())));
            document.open();
            Font bold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            document.add(new Paragraph(String.format("Mortago Report for %s", body.getIdNum()), bold));
            document.add(new Paragraph("\n"));

            Image logo = Image.getInstance("docs/images/Logo.png");
            logo.setAbsolutePosition(450, 715);
            logo.scaleAbsolute(100, 100);
            document.add(logo);

            addPersonalDetails(document, body);
            addNokDetails(document, body);
            addOtherDetails(document, body);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("© This report is generated by Mortago."));
            document.close();
            writer.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addOtherDetails(Document document, Body body) throws DocumentException {
        document.add(new Paragraph("Other Details:"));
        PdfPTable otherDetails = createTable();

        otherDetails.addCell(new Paragraph("Cause of Death:"));
        if (!body.getCauseOfDeath().isPresent()) {
            otherDetails.addCell(
                    new PdfPCell(new Paragraph("N.A.")));
        } else {
            otherDetails.addCell(
                    new PdfPCell(new Paragraph(String.format("%s", body.getCauseOfDeath()))));
        }
        List organList = new List();
        if (body.getOrgansForDonation().isPresent()) {
            for (String organ : body.getOrgansForDonation().get()) {
                organList.add(new ListItem(organ));
            }
            if (organList.isEmpty()) {
                organList.add("No organs for donation.");
            }
        }
        PdfPCell cell = new PdfPCell();
        cell.addElement(organList);
        otherDetails.addCell(new Paragraph("Organs for Donation:"));
        otherDetails.addCell(cell);
        otherDetails.addCell(new Paragraph("Body Status:"));
        if (!body.getNextOfKin().isPresent()) {
            otherDetails.addCell(new PdfPCell(new Paragraph("N.A.")));
        } else {
            otherDetails.addCell(
                    new PdfPCell(new Paragraph(String.format("%s", body.getBodyStatus()))));
        }
        otherDetails.addCell(new Paragraph("Fridge ID:"));
        if (!body.getFridgeId().isPresent()) {
            otherDetails.addCell(new PdfPCell(new Paragraph("N.A.")));
        } else {
            otherDetails.addCell(
                    new PdfPCell(new Paragraph(String.format("%s", body.getFridgeId()))));
        }

        document.add(otherDetails);
    }

    private static PdfPTable createTable() throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(70);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        float[] columnWidths = {0.5f, 0.5f};
        table.setWidths(columnWidths);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        return table;
    }

    private static void addNokDetails(Document document, Body body) throws DocumentException {
        document.add(new Paragraph("Next of Kin:"));
        PdfPTable nokDetails = createTable();

        nokDetails.addCell(new PdfPCell(new Paragraph("Next of Kin:")));
        if (!body.getNextOfKin().isPresent()) {
            nokDetails.addCell(new PdfPCell(new Paragraph("N.A.")));
        } else {
            nokDetails.addCell(
                    new PdfPCell(new Paragraph(String.format("%s", body.getNextOfKin()))));
        }
        nokDetails.addCell(new PdfPCell(new Paragraph("Relationship:")));
        if (!body.getRelationship().isPresent()) {
            nokDetails.addCell(new PdfPCell(new Paragraph("N.A.")));
        } else {
            nokDetails.addCell(
                    new PdfPCell(new Paragraph(String.format("%s", body.getRelationship()))));
        }
        nokDetails.addCell(new PdfPCell(new Paragraph("Contact Number:")));
        if (!body.getKinPhoneNumber().isPresent()) {
            nokDetails.addCell(new PdfPCell(new Paragraph("N.A.")));
        } else {
            nokDetails.addCell(
                    new PdfPCell(new Paragraph(String.format("%s", body.getKinPhoneNumber()))));
        }

        document.add(nokDetails);
    }

    private static void addPersonalDetails(Document document, Body body) throws DocumentException {
        document.add(new Paragraph("Personal Details:"));
        PdfPTable personalDetails = createTable();

        personalDetails.addCell(new PdfPCell(new Paragraph("ID Number:")));
        personalDetails.addCell(new PdfPCell(new Paragraph(body.getIdNum().toString())));
        personalDetails.addCell(new PdfPCell(new Paragraph("Name:")));
        personalDetails.addCell(new PdfPCell(new Paragraph(body.getName().toString())));
        personalDetails.addCell(new PdfPCell(new Paragraph("Sex:")));
        personalDetails.addCell(new PdfPCell(new Paragraph(body.getSex().toString())));
        personalDetails.addCell(new PdfPCell(new Paragraph("Date of Admission:")));
        personalDetails.addCell(new PdfPCell(new Paragraph(body.getDateOfAdmission().toString())));
        personalDetails.addCell(new PdfPCell(new Paragraph("Date of Birth:")));
        if (!body.getDateOfBirth().isPresent()) {
            personalDetails.addCell(
                    new PdfPCell(new Paragraph("N.A.")));
        } else {
            personalDetails.addCell(
                    new PdfPCell(new Paragraph(String.format("%s", body.getDateOfBirth()))));
        }
        personalDetails.addCell(new PdfPCell(new Paragraph("Date of Death:")));
        personalDetails.addCell(new PdfPCell(new Paragraph(body.getDateOfDeath().toString())));
        personalDetails.addCell(new PdfPCell(new Paragraph("NRIC/FIN Number:")));
        if (!body.getNric().isPresent()) {
            personalDetails.addCell(
                    new PdfPCell(new Paragraph("N.A.")));
        } else {
            personalDetails.addCell(
                    new PdfPCell(new Paragraph(String.format("%s", body.getNric()))));
        }
        personalDetails.addCell(new PdfPCell(new Paragraph("Religion:")));
        if (!body.getReligion().isPresent()) {
            personalDetails.addCell(
                    new PdfPCell(new Paragraph("N.A.")));
        } else {
            personalDetails.addCell(
                    new PdfPCell(new Paragraph(String.format("%s", body.getReligion()))));
        }

        document.add(personalDetails);

    }
}
