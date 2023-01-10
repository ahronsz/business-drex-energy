package com.drex.service.expose.web;

import com.drex.service.expose.dto.reponse.EnergyDtoResponse;
import com.drex.service.expose.dto.request.EnergyDtoRequest;
import com.drex.service.grid.business.EnergyService;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.IOException;

@RestController
@RequestMapping(value = "/grid")
public class EnergyController {

    @Autowired
    private EnergyService energyService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Completable record(@RequestBody EnergyDtoRequest energyDtoRequest) {
        return energyService.save(energyDtoRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/count")
    public Single<Long> record() {
        return energyService.count();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/batch")
    public Completable recordByBatch(@RequestParam("file") MultipartFile file) throws IOException {
        return energyService.send(file);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/list")
    public Flowable<EnergyDtoResponse> list(@Valid @Null @Param("code") String code) {
        return energyService.list(code);
    };

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/last")
    public Single<EnergyDtoResponse> lastRecord(@Valid @NotNull @Param("code") String code) {
        return energyService.getLastRecord(code);
    };

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/status")
    public String checkStatus() {
        return "ok";
    }
}
