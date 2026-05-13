package br.com.challenge.application.service.parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import br.com.challenge.adapter.in.rest.response.UploadHeaderResponse;
import br.com.challenge.adapter.in.rest.response.UploadTrailerResponse;

@Component
public class UploadFileParser {

    public UploadHeaderResponse parseHeader(
            String headerLine
    ) {

        String fileName =
                headerLine.substring(0, 29).trim();

        String fileDate =
                headerLine.substring(29, 37).trim();

        String lot =
                headerLine.substring(37, 45).trim();

        Integer expectedRecords =
                Integer.parseInt(
                        headerLine.substring(45, 51).trim()
                );

        LocalDate formattedDate =
                LocalDate.parse(
                        fileDate,
                        DateTimeFormatter.BASIC_ISO_DATE
                );

        return new UploadHeaderResponse(
                fileName,
                formattedDate.toString(),
                lot,
                expectedRecords
        );
    }

    public UploadTrailerResponse parseTrailer(
            String trailerLine
    ) {

        String lot =
                trailerLine.substring(0, 8).trim();

        Integer expectedRecords =
                Integer.parseInt(
                        trailerLine.substring(8, 14).trim()
                );

        return new UploadTrailerResponse( lot, expectedRecords );
    }

    public String extractCardIdentifier( String line ) {

        return line.substring(0, 7).trim();
    }

    public String extractCardNumber( String line ) {

        return line.substring(7).trim();
    }
}