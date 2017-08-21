package RetrofitApi;

import RetrofitApiModelsATM.Example;
import RetrofitApiModelsTSO.ExampleTSO;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Slava on 12.07.2017.
 */

public interface IBank {

    @GET("infrastructure?json&atm&address=&city=Днепропетровск")
    Call<Example>getArrayDevice();

    @GET("infrastructure?json&tso&address=&city=Днепропетровск")
    Call<ExampleTSO> getArrayDeviceTSO();
}
