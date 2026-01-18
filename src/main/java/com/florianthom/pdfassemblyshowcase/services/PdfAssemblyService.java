package com.florianthom.pdfassemblyshowcase.services;

import com.florianthom.pdfassemblyshowcase.domain.PokemonTrainer;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Map;

@Service
public class PdfAssemblyService {

    private final BrowserContext playwrightBrowserBontext;
    private final TemplateEngine templateEngine;
    private final PdfSigningService signingService;
    private final WebClient gotenbergWebClient;

    public PdfAssemblyService(BrowserContext playwrightBrowserBontext, TemplateEngine templateEngine, PdfSigningService signingService, WebClient gotenbergWebClient) {
        this.playwrightBrowserBontext = playwrightBrowserBontext;
        this.templateEngine = templateEngine;
        this.signingService = signingService;
        this.gotenbergWebClient = gotenbergWebClient;
    }

    public String createHtmlDocumentLayout(PokemonTrainer trainer){
        return templateEngine.process(
                "pokemontrainer",
                new Context(Locale.getDefault(), Map.of("pokemontrainer", trainer))
        );
    }

    public byte[] renderPdfByPlaywrightChromium(PokemonTrainer trainer) {
        System.out.println("Assemble pdf by playwright");
        var htmlString = createHtmlDocumentLayout(trainer);

        Page page = playwrightBrowserBontext.newPage();
        page.setContent(htmlString);
        byte[] pdf = page.pdf(new Page.PdfOptions()
                .setFormat("A4")
                .setPrintBackground(true));
        page.close();
        return pdf;
    }

    public byte[] renderPdfByGotenbergChromium(PokemonTrainer trainer) {
        System.out.println("Assemble pdf by gotenberg");
        var htmlString = createHtmlDocumentLayout(trainer);

        Resource imageResource = new ClassPathResource("static/gotenberg/bird.jpg");

        byte[] imageBytes;

        try (InputStream is = imageResource.getInputStream()) {
            imageBytes = is.readAllBytes();
        } catch (IOException e) { throw new RuntimeException(e);}

        // https://gotenberg.dev/docs/routes#html-file-into-pdf-route

        var builder = new MultipartBodyBuilder();
        builder.part("files[]", htmlString.getBytes(StandardCharsets.UTF_8))
                .filename("index.html")
                .contentType(MediaType.TEXT_HTML);
        builder.part("files[]", imageBytes)
                .filename("bird.jpg")
                .contentType(MediaType.IMAGE_JPEG);

        var pdf = gotenbergWebClient.post()
                .uri("/forms/chromium/convert/html")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
        return pdf;
    }

    // integration guide: https://github.com/openhtmltopdf/openhtmltopdf/wiki/Integration-Guide
    public byte[] renderPdfByOpenHtmlToPdf(PokemonTrainer trainer) {
        System.out.println("Assemble pdf by openhtml to pdf");
        var htmlString = createHtmlDocumentLayout(trainer);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlString, null);
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("PDF generation failed", e);
        }
    }

    public byte[] assemblePdf(PokemonTrainer trainer) throws IOException {
        var pdf = renderPdfByPlaywrightChromium(trainer);
        var signedPdf = signingService.signWithPadesLt(pdf);
        return signedPdf;
    }
}