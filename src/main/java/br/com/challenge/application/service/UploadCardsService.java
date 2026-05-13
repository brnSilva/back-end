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
import br.com.challenge.application.service.parser.UploadFileParser;
import br.com.challenge.application.service.validator.UploadConsistencyValidator;

@Service
public class UploadCardsService implements UploadCardsUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadCardsService.class);

    private final CreateCardUseCase createCardUseCase;

    private final UploadFileParser uploadFileParser;

    private final UploadConsistencyValidator validator;

    public UploadCardsService(CreateCardUseCase createCardUseCase,
                              UploadFileParser uploadFileParser,
                              UploadConsistencyValidator validator) {
        this.createCardUseCase = createCardUseCase;
        this.uploadFileParser = uploadFileParser;
        this.validator = validator;
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

            header = uploadFileParser.parseHeader(
                                                headerLine
                                        );

            String line;
            String cardIdentifier = null;
            String cardNumber = null;

            while ((line = reader.readLine()) != null) {

                if(line.startsWith("LOTE")){
                    trailer = uploadFileParser.parseTrailer(line);
                    continue;
                }

                if(!line.startsWith("C"))
                    continue; // Skip invalid lines

                processed++;

                
                try {
                    cardIdentifier = uploadFileParser
                                        .extractCardIdentifier(line);

                    cardNumber = uploadFileParser
                                    .extractCardNumber(line);

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

        boolean lotConsistent = validator.isLotConsistent(header, trailer);

        boolean quantityConsistent = validator.isQuantityConsistent(header, trailer, processed);
        
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
}