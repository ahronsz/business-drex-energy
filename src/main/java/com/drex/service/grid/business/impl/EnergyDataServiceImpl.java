package com.drex.service.grid.business.impl;

import com.drex.service.expose.dto.response.EnergyDtoResponse;
import com.drex.service.expose.dto.request.EnergyDtoRequest;
import com.drex.service.expose.dto.response.GraphicDtoResponse;
import com.drex.service.grid.business.EnergyDataService;
import com.drex.service.grid.mapper.EnergyDataMapper;
import com.drex.service.grid.mapper.GraphicMapper;
import com.drex.service.grid.repository.EnergyDataRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Service
public class EnergyDataServiceImpl implements EnergyDataService {

    @Autowired
    private EnergyDataRepository energyDataRepository;

    @Autowired
    private EnergyDataMapper mapper;

    @Autowired
    private GraphicMapper graphicMapper;

    @Override
    public Completable save(EnergyDtoRequest energyDtoRequest) {
        return Completable.fromSingle(energyDataRepository.save(mapper.toEntity(energyDtoRequest)));
    }

    @Override
    public Completable send(MultipartFile file) throws IOException {
        this.saveByBatch(file.getInputStream());
        return Completable.complete();
    }

    private void saveByBatch(InputStream inputStream) {
        List<EnergyDtoRequest> list = this.csvToListEnergyData(inputStream);
        Flowable.fromIterable(list)
                .count()
                .doOnSuccess(aLong -> System.out.println("Número de registros del csv: " + aLong))
                .flatMapPublisher(aLong ->
                        Flux.range(0, (int) Math.ceil((double) aLong / 10)).delayElements(Duration.ofMillis(500)))
                .doOnNext(integer -> System.out.println("Batch N°: " + (integer + 1)))
                .map(integer -> this.findPaginated(list, PageRequest.of(integer, 10)))
                .flatMapCompletable(gridDtoRequests ->
                        energyDataRepository.saveAll(Flowable.fromIterable(mapper.toEntityList(gridDtoRequests.toList())))
                                .ignoreElements()).subscribe();
    }

    @Override
    public Flowable<EnergyDtoResponse> list(String deviceCode) {
        if (deviceCode != null) {
            return energyDataRepository.getEnergyDataByDeviceCode(deviceCode)
                    .switchIfEmpty(Flowable.defer(() -> {
                        throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No se encontró la planta de energía, revise su request");
                    }))
                    .map(mapper::toResponse);
        } else {
            return energyDataRepository.findAll().map(mapper::toResponse);
        }

    }

    @Override
    public Single<EnergyDtoResponse> getLastRecord(String code) {
        return energyDataRepository.getLastRecord(code)
                .switchIfEmpty(Single.defer(() -> {
                    throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No se encontró la planta de energía, revise su request");
                }))
                .map(energyData -> mapper.toResponse(energyData));
    }

    @Override
    public Single<Long> count() {
        return energyDataRepository.count();
    }

    @Override
    public Flowable<GraphicDtoResponse> getGraphicByDeviceCode(String code, String time) {
        return energyDataRepository.getGraphicByDeviceCode(code, time)
                .switchIfEmpty(Flowable.defer(() -> {
                    throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No se encontró la planta de energía, revise su request");
                }))
                .map(graphic -> graphicMapper.toResponse(graphic));
    }

    private List<EnergyDtoRequest> csvToListEnergyData(InputStream inputStream) {
        return new CsvToBeanBuilder(new InputStreamReader(inputStream))
                .withType(EnergyDtoRequest.class)
                .build()
                .parse();
    }

    private Page<EnergyDtoRequest> findPaginated(List<EnergyDtoRequest> grid, Pageable pageable) {

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<EnergyDtoRequest> list;

        if (grid.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, grid.size());
            list = grid.subList(startItem, toIndex);
        }
        return new PageImpl<EnergyDtoRequest>(list, PageRequest.of(currentPage, pageSize), grid.size());
    }
}
