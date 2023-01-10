package com.drex.service.grid.business;

import com.drex.service.expose.dto.reponse.EnergyDtoResponse;
import com.drex.service.expose.dto.request.EnergyDtoRequest;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface EnergyService {

    Completable save(EnergyDtoRequest energyDtoRequest);

    Completable send(MultipartFile file) throws IOException;

    Flowable<EnergyDtoResponse> list(String gridCode);

    Single<EnergyDtoResponse> getLastRecord(String code);

    Single<Long> count();
}
