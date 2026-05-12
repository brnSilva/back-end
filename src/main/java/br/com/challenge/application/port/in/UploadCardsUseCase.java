package br.com.challenge.application.port.in;

import org.springframework.web.multipart.MultipartFile;

import br.com.challenge.adapter.in.rest.response.UploadCardsResponse;

public interface UploadCardsUseCase {
    
    UploadCardsResponse execute(MultipartFile file);
}
