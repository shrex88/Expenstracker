package com.expensetracker.util;

import com.expensetracker.model.Expense;
import com.expensetracker.model.User;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PDFGenerator {

    public static void generateReport(User user, List<Expense> expenses, OutputStream out) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            
            // Header Title
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
            contentStream.newLineAtOffset(50, 750);
            contentStream.showText("Expense Tracker Report");
            contentStream.endText();

            // Meta Info
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.newLineAtOffset(50, 725);
            contentStream.showText("User: " + user.getName() + " (" + user.getEmail() + ")");
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Generated On: " + java.time.LocalDate.now().toString());
            contentStream.endText();

            // Summary stats
            double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
            double highest = expenses.stream().mapToDouble(Expense::getAmount).max().orElse(0.0);
            
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(50, 680);
            contentStream.showText("Summary Stats:");
            contentStream.setFont(PDType1Font.HELVETICA, 11);
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Total Expenses: INR " + String.format("%.2f", total));
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Highest Expense: INR " + String.format("%.2f", highest));
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Total Transactions: " + expenses.size());
            contentStream.endText();

            // Draw Table Headers
            float y = 600;
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
            contentStream.newLineAtOffset(50, y);
            contentStream.showText("Title");
            contentStream.newLineAtOffset(180, 0);
            contentStream.showText("Category");
            contentStream.newLineAtOffset(110, 0);
            contentStream.showText("Date");
            contentStream.newLineAtOffset(110, 0);
            contentStream.showText("Amount");
            contentStream.endText();

            // Draw header line
            contentStream.setLineWidth(1f);
            contentStream.moveTo(50, y - 5);
            contentStream.lineTo(550, y - 5);
            contentStream.stroke();

            y -= 25;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (Expense e : expenses) {
                // If page overflows, create a new page
                if (y < 50) {
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    
                    // headers on new page
                    y = 730;
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                    contentStream.newLineAtOffset(50, y);
                    contentStream.showText("Title");
                    contentStream.newLineAtOffset(180, 0);
                    contentStream.showText("Category");
                    contentStream.newLineAtOffset(110, 0);
                    contentStream.showText("Date");
                    contentStream.newLineAtOffset(110, 0);
                    contentStream.showText("Amount");
                    contentStream.endText();

                    contentStream.setLineWidth(1f);
                    contentStream.moveTo(50, y - 5);
                    contentStream.lineTo(550, y - 5);
                    contentStream.stroke();
                    
                    y -= 25;
                }

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 9);
                contentStream.newLineAtOffset(50, y);
                
                // Crop title if too long to prevent wrapping issues
                String title = e.getTitle();
                if (title.length() > 30) title = title.substring(0, 27) + "...";
                contentStream.showText(title);
                
                contentStream.newLineAtOffset(180, 0);
                contentStream.showText(e.getCategory());
                
                contentStream.newLineAtOffset(110, 0);
                contentStream.showText(e.getExpenseDate().format(formatter));
                
                contentStream.newLineAtOffset(110, 0);
                contentStream.showText("INR " + String.format("%.2f", e.getAmount()));
                
                contentStream.endText();
                
                y -= 20;
            }

            contentStream.close();
            document.save(out);
        }
    }
}
