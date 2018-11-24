package org.secuso.privacyfriendlyexample;

import android.widget.Toast;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.secuso.privacyfriendlyfoodtracker.activities.MainActivity;
import org.secuso.privacyfriendlyfoodtracker.network.ApiManager;
import org.secuso.privacyfriendlyfoodtracker.network.ProductApiService;
import org.secuso.privacyfriendlyfoodtracker.network.models.ProductResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertTrue;


public class ApiManagerTest {
    ProductApiService mProductApiService = null ;
    ProductResponse productResponse = new ProductResponse();


    @Before
    public void createManager() {
        this.mProductApiService =  ApiManager.getInstance().getProductApiService();

    }

    @Test
    public void readProductInformations() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        Call<ProductResponse> call = mProductApiService.listProducts("banane");
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful())  {
                    productResponse = response.body();
                    }
                else{
                    //show error
                }
                signal.countDown();

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                signal.countDown();

            }
        });
        signal.await();// wait for callback
        // uses the current location. To pass the test, the location must be "de"
        assertTrue("Responds should contains 20 product informations ", productResponse.getProducts().size() == 20 );
    }
}
