package br.com.challenge.application.service.validator;

import org.springframework.stereotype.Component;

import br.com.challenge.adapter.in.rest.response.UploadHeaderResponse;
import br.com.challenge.adapter.in.rest.response.UploadTrailerResponse;

@Component
public class UploadConsistencyValidator {

    public boolean isLotConsistent(
            UploadHeaderResponse header,
            UploadTrailerResponse trailer
    ) {

        return header != null
                && trailer != null
                && header.lot()
                        .equals(trailer.lot());
    }

    public boolean isQuantityConsistent(
            UploadHeaderResponse header,
            UploadTrailerResponse trailer,
            int processed
    ) {

        return header != null
                && trailer != null
                && header.expectedRecords()
                        .equals(
                                trailer.expectedRecords()
                        )
                && header.expectedRecords()
                        .equals(processed);
    }
}