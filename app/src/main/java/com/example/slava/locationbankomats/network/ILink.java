package com.example.slava.locationbankomats.network;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Slava on 12.07.2017.
 */

public interface ILink {

    @GET("infrastructure?json&atm&address=&city=Днепропетровск")
    Call<ATMPojo>getArrayDevice();

    @GET("infrastructure?json&tso&address=&city=Днепропетровск")
    Call<TSOPojo> getArrayDeviceTSO();
}
