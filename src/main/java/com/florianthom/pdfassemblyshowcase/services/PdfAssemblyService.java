package com.florianthom.pdfassemblyshowcase.services;

import com.florianthom.pdfassemblyshowcase.domain.PokemonTrainer;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

@Service
public class PdfAssemblyService {

    private final BrowserContext playwrightBrowserBontext;
    private final TemplateEngine templateEngine;
    private final PdfSigningService signingService;

    public PdfAssemblyService(BrowserContext playwrightBrowserBontext, TemplateEngine templateEngine, PdfSigningService signingService) {
        this.playwrightBrowserBontext = playwrightBrowserBontext;
        this.templateEngine = templateEngine;
        this.signingService = signingService;
    }

    public byte[] renderPdf(PokemonTrainer trainer) {
        System.out.println("Assemble pdf");
        var htmlString = templateEngine.process(
                "pokemontrainer",
                new Context(Locale.getDefault(), Map.of("pokemontrainer", trainer))
        );

        Page page = playwrightBrowserBontext.newPage();
        page.setContent(htmlString);
        byte[] pdf = page.pdf(new Page.PdfOptions()
                .setFormat("A4")
                .setPrintBackground(true));
        page.close();
        return pdf;
    }

    public byte[] assemblePdf(PokemonTrainer trainer) throws IOException {
        var pdf = renderPdf(trainer);
        var signedPdf = signingService.signWithPadesLt(pdf);
        return signedPdf;
    }
}