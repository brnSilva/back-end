package br.com.challenge.application.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.challenge.adapter.in.rest.logging.CardMaskUtil;
import br.com.challenge.adapter.in.rest.response.UploadCardsResponse;
import br.com.challenge.adapter.in.rest.response.UploadErrorResponse;
import br.com.challenge.adapter.in.rest.response.UploadHeaderResponse;
import br.com.challenge.adapter.in.rest.response.UploadTrailerResponse;
import br.com.challenge.application.dto.CreateCardCommand;
import br.com.challenge.application.port.in.CreateCardUseCase;
import br.com.challenge.application.port.in.UploadCardsUseCase;

@Service
public class UploadCardsService implements UploadCardsUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadCardsService.class);

    private final CreateCardUseCase createCardUseCase;

    public UploadCardsService(CreateCardUseCase createCardUseCase) {
        this.createCardUseCase = createCardUseCase;
    }

    @Override
    public UploadCardsResponse execute(MultipartFile file) {

        LOGGER.info("Upload processing started - filename={}", file.getOriginalFilename() );

        UploadHeaderResponse header = null;
        UploadTrailerResponse trailer = null;

        int processed = 0;
        int success = 0;

        List<UploadErrorResponse> errorsDetails = new ArrayList<>();

        try (
            BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(
                                            file.getInputStream()))
        ) {
            String headerLine = reader.readLine();

            if(headerLine == null)
                throw new IllegalStateException("File is empty");

            header = parseHeader(headerLine);

            String line;
            String cardIdentifier = null;
            String cardNumber = null;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                if(line.startsWith("LOTE")){
                    trailer = parseTrailer(line);
                    continue;
                }

                if(!line.startsWith("C")) {
                    continue; // Skip invalid lines
                }

                processed++;

                
                try {
                    cardIdentifier = line.substring(0, 7).trim();

                    cardNumber = line.substring(7).trim();
                    
                    CreateCardCommand command = new CreateCardCommand(cardNumber);

                    createCardUseCase.execute(command);

                    success++;
                } catch (Exception e) {
                    LOGGER.warn( "Upload failed - identifier={} cardNumber={} reason={}", cardIdentifier, CardMaskUtil.maskCardNumber(cardNumber), e.getMessage());
                    errorsDetails.add(
                        new UploadErrorResponse(
                                cardIdentifier,
                                e.getMessage()));
                }
            }

        } catch (Exception e) {
            throw new IllegalStateException(
                "Error processing file upload", e );
        }

        LOGGER.info("Upload processed= {}, success= {}, failed= {}",
                    processed,
                    success,
                    errorsDetails.size());

        boolean lotConsistent = header != null 
                                && trailer != null 
                                && header.lot().equals(
                                                    trailer.lot()
                                                );

        boolean quantityConsistent = header != null
                                        && trailer != null
                                        && header.expectedRecords()
                                                    .equals(
                                                        trailer.expectedRecords()
                                                    )
                                        && header.expectedRecords()
                                                    .equals(processed);
        
        return new UploadCardsResponse(
            header,
            trailer,
            lotConsistent,
            quantityConsistent,
            processed,
            success,
            errorsDetails.size(),
            errorsDetails
        );
    }

    private UploadHeaderResponse parseHeader(String headerLine) {
        
        String fileName = headerLine.substring(0, 29).trim();
        
        String fileDate = headerLine.substring(29, 37).trim();

        String lot = headerLine.substring(37, 45).trim();

        Integer expectedRecords = Integer.parseInt(
                                        headerLine.substring(45, 51).trim()
                                    );

        LocalDate dateFormated = LocalDate.parse(fileDate, DateTimeFormatter.BASIC_ISO_DATE);

        return new UploadHeaderResponse(fileName, dateFormated.toString(), lot, expectedRecords);
    }

    private UploadTrailerResponse parseTrailer(String trailerLine) {
        
        String lot = trailerLine.substring(0, 8).trim();

        Integer expectedRecords = Integer.parseInt(
                                        trailerLine.substring(8, 14).trim()
                                    );

        return new UploadTrailerResponse(lot, expectedRecords);
    }
}