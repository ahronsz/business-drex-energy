package com.drex.service.expose.web;

import com.drex.service.expose.dto.response.EnergyDtoResponse;
import com.drex.service.expose.dto.request.EnergyDtoRequest;
import com.drex.service.expose.dto.response.GraphicDtoResponse;
import com.drex.service.grid.business.EnergyDataService;
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
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RequestMapping(value = "/plant")
public class EnergyDataController {

    @Autowired
    private EnergyDataService energyDataService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Completable record(@RequestBody EnergyDtoRequest energyDtoRequest) {
        return energyDataService.save(energyDtoRequest);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/batch")
    public Completable recordByBatch(@RequestParam("file") MultipartFile file) throws IOException {
        return energyDataService.send(file);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/list")
    public Flowable<EnergyDtoResponse> list(@Valid @Null @Param("code") String code) {
        return energyDataService.list(code);
    };

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/graphic")
    public Flowable<GraphicDtoResponse> getGraphic(
            @Valid @NotNull @Param("code") String code,
            @Valid @NotNull @Param("time") String time) {
        return energyDataService.getGraphicByDeviceCode(code, time);
    };

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/last")
    public Single<EnergyDtoResponse> lastRecord(@Valid @NotNull @Param("code") String code) {
        return energyDataService.getLastRecord(code);
    };

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/status")
    public String checkStatus() {
        return "ok";
    }
}
