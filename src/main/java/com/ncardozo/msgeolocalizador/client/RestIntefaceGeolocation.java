package com.ncardozo.msgeolocalizador.client;

import com.ncardozo.msgeolocalizador.dto.external.IpLocationDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestIntefaceGeolocation {

    @GET("{ip}")
    Call<IpLocationDto> getIpLocation(@Path("ip") String ip, @Query("language") String language, @Query("access_key") String accessKey);
}
