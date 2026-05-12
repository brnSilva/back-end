package br.com.challenge.application.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.challenge.adapter.in.rest.response.UploadCardsResponse;
import br.com.challenge.adapter.in.rest.response.UploadErrorResponse;
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
        
        int processed = 0;
        int success = 0;

        List<UploadErrorResponse> errorsDetails = new ArrayList<>();

        try (
            BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(
                                            file.getInputStream()))
        ) {

            String line;
            String cardIdentifier = null;
            String cardNumber = null;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                if(lineNumber == 1) {
                    continue; // Skip header
                }

                if(line.startsWith("LOTE")){
                    continue; // Skip trailer
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
                    LOGGER.warn( "Upload failed - identifier={} reason={}", cardIdentifier, e.getMessage());
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

        return new UploadCardsResponse(
            processed,
            success,
            errorsDetails.size(),
            errorsDetails
        );
    }
}