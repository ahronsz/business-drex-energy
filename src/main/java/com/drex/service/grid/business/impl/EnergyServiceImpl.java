package com.drex.service.grid.business.impl;

import com.drex.service.expose.dto.reponse.EnergyDtoResponse;
import com.drex.service.expose.dto.request.EnergyDtoRequest;
import com.drex.service.grid.business.EnergyService;
import com.drex.service.grid.mapper.EnergyMapper;
import com.drex.service.grid.model.entity.EnergyData;
import com.drex.service.grid.repository.EnergyRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class EnergyServiceImpl implements EnergyService {

    @Autowired
    private EnergyRepository energyRepository;

    @Autowired
    private EnergyMapper mapper;

    @Override
    public Completable save(EnergyDtoRequest energyDtoRequest) {
        return Completable.fromSingle(energyRepository.save(mapper.toEntity(energyDtoRequest)));
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
                        energyRepository.saveAll(Flowable.fromIterable(mapper.toEntityList(gridDtoRequests.toList())))
                                .ignoreElements()).subscribe();
    }

    @Override
    public Flowable<EnergyDtoResponse> list(String gridCode) {
        if (gridCode != null) {
            return energyRepository.getEnergyDataByDeviceCode(gridCode).map(mapper::toResponse);
        } else {
            return energyRepository.findAll().map(mapper::toResponse);
        }

    }

    @Override
    public Single<EnergyDtoResponse> getLastRecord(String code) {
        return energyRepository.getLastRecord(code)
                .defaultIfEmpty(EnergyData.builder().build())
                .map(energyData -> mapper.toResponse(energyData));
    }

    @Override
    public Single<Long> count() {
        return energyRepository.count();
    }

    private Flowable<EnergyDtoResponse> listget(String gridCode) {
        return energyRepository.getEnergyDataByDeviceCode(gridCode)
                .count()
                .doOnSuccess(aLong -> System.out.println("Registre N°: " + aLong))
                .flatMapPublisher(aLong -> Flowable.range(0, (int) (aLong / 100) + 12)
                        .delay(500, TimeUnit.MILLISECONDS))
                .doOnNext(integer -> System.out.println("Batch N°: " + (integer + 1)))
                .flatMap(integer -> energyRepository.getByPaginable(gridCode, integer, 5))
                .map(mapper::toResponse);
                //.flatMapIterable(gridDtoResponse -> gridDtoResponse);

        /*
        Flowable.fromIterable(list)
                .count()
                .doOnSuccess(aLong -> System.out.println("Número de registros del csv: " + aLong))
                .flatMapPublisher(aLong ->
                        Flux.range(0, (int) Math.ceil((double) aLong / 10)).delayElements(Duration.ofMillis(500)))
                .doOnNext(integer -> System.out.println("Batch N°: " + (integer + 1)))
                .map(integer -> this.findPaginated(list, PageRequest.of(integer, 10)))
                .flatMapCompletable(gridDtoRequests ->
                        gridRepository.saveAll(Flowable.fromIterable(mapper.toEntityList(gridDtoRequests.toList())))
                                .ignoreElements()).subscribe();
                                */
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
