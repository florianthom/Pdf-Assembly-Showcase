package com.florianthom.pdfassemblyshowcase.controller;

import com.florianthom.pdfassemblyshowcase.domain.Pokemon;
import com.florianthom.pdfassemblyshowcase.domain.PokemonTrainer;
import com.florianthom.pdfassemblyshowcase.services.PdfAssemblyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController("/")
public class PdfController {

    private final TemplateEngine templateEngine;
    private final PdfAssemblyService pdfAssemblyService;
    private final PokemonTrainer trainer = new PokemonTrainer(
            "1",
            LocalDate.now(),
            List.of(
                    new Pokemon("Widget", 3),
                    new Pokemon("Gadget", 2)
            )
    );

    @Autowired
    public PdfController(TemplateEngine templateEngine, PdfAssemblyService pdfAssemblyService, WebClient gotenbergWebClient) {
        this.templateEngine = templateEngine;
        this.pdfAssemblyService = pdfAssemblyService;
    }

    @GetMapping("")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("<h1>ping</h1>");
    }

    @GetMapping("preview-htmlpdf")
    @ResponseBody
    public String previewInvoice() {
        return  templateEngine.process(
                "pokemontrainer",
                new Context(Locale.getDefault(), Map.of("pokemontrainer", trainer))
        );
    }

    @GetMapping("/create-pdf")
    public ResponseEntity<byte[]> createPdfWithPlaywrightChromium() throws IOException {
        var pdf = pdfAssemblyService.assemblePdf(trainer);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                // "inline" to show in browser if supported
                // "attachment" to force download
                ContentDisposition.builder("attachment")
                        .filename("pokemontrainer-" + trainer.trainerId + ".pdf")
                        .build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }

    @GetMapping("/create-pdf-with-gotenberg")
    public ResponseEntity<byte[]> createPdfWithGotenberg() {
        var pdf = pdfAssemblyService.renderPdfByGotenbergChromium(trainer);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.builder("inline")
                        .filename("pokemontrainer-" + trainer.trainerId + ".pdf")
                        .build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }

    @GetMapping("/create-pdf-with-openhtmltopdf")
    public ResponseEntity<byte[]> createPdfWithOpenHtmlToPdf() {
        var pdf = pdfAssemblyService.renderPdfByOpenHtmlToPdf(trainer);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.builder("inline")
                        .filename("pokemontrainer-" + trainer.trainerId + ".pdf")
                        .build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }


}
