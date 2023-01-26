package com.drex.service.grid.repository;

import com.drex.service.grid.model.entity.EnergyData;
import com.drex.service.grid.model.entity.Graphic;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;

public interface EnergyDataRepository extends RxJava3CrudRepository<EnergyData, Long>{

    @Query("SELECT * FROM energy_data WHERE device_code = :code ORDER BY timestamp DESC LIMIT 1")
    Maybe<EnergyData> getLastRecord(String code);

    @Query("SELECT * FROM energy_data WHERE device_code = :code")
    Flowable<EnergyData> getEnergyDataByDeviceCode(String code);

    @Query("SELECT date_trunc(:time, timestamp) as utc_date_time,\n" +
            "       energy as energy,\n" +
            "       MAX(energy_accumulated) as energy_accumulated\n" +
            "FROM energy_data WHERE device_code = :deviceCode GROUP BY utc_date_time, energy ORDER BY utc_date_time")
    Flowable<Graphic> getGraphicByDeviceCode(String deviceCode, String time);
}
