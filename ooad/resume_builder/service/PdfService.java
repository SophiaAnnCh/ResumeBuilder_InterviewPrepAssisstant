package com.project.ooad.resume_builder.service;


import com.project.ooad.resume_builder.model.Resume;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.ByteArrayOutputStream;
// import java.io.StringWriter;

@Service
public class PdfService {

    public byte[] generatePdf(Resume resume) throws Exception {
        String html = """
            <html>
            <head><title>Resume</title></head>
            <body>
                <h1>%s</h1>
                <p>Email: %s</p>
                <p>User ID: %s</p>
            </body>
            </html>
            """.formatted(resume.getName(), resume.getEmail(), resume.getUserId());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(baos);

        return baos.toByteArray();
    }
}
