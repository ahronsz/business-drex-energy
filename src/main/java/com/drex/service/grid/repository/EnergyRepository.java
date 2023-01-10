package com.drex.service.grid.repository;

import com.drex.service.grid.model.entity.EnergyData;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;

public interface EnergyRepository extends RxJava3CrudRepository<EnergyData, Long>{

    @Query("SELECT * FROM energy_data WHERE device_code = :code ORDER BY timestamp DESC LIMIT 1")
    Maybe<EnergyData> getLastRecord(String code);


    @Query("SELECT * FROM energy_data WHERE device_code = :code")
    Flowable<EnergyData> getEnergyDataByDeviceCode(String code);

    @Query("SELECT * FROM energy_data WHERE device_code = :code ORDER BY timestamp DESC OFFSET :offset LIMIT :limit")
    Flowable<EnergyData> getByPaginable(String code, Integer offset, Integer limit);
}
