package org.secuso.privacyfriendlyexample;

import android.widget.Toast;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.secuso.privacyfriendlyfoodtracker.activities.MainActivity;
import org.secuso.privacyfriendlyfoodtracker.network.ApiManager;
import org.secuso.privacyfriendlyfoodtracker.network.ProductApiService;
import org.secuso.privacyfriendlyfoodtracker.network.models.ProductResponse;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertTrue;


public class ApiManagerTest {
    ProductApiService mProductApiService = null ;

    @Before
    public void createManager() {
        this.mProductApiService =  ApiManager.getInstance().getProductApiService();

    }

    @Test
    public void readProductInformations() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        Call<List<ProductResponse>> call = mProductApiService.listProducts();
        call.enqueue(new Callback<List<ProductResponse>>() {
            @Override
            public void onResponse(Call<List<ProductResponse>> call, Response<List<ProductResponse>> response) {
                signal.countDown();
            }

            @Override
            public void onFailure(Call<List<ProductResponse>> call, Throwable t) {

            }
        });
        assertTrue("", ((List<ProductResponse>)call).size() == 1 );
    }
}
