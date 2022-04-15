package com.example.json;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ImageView spaceImage;
    TextView description;
    public final String api_nasa = "https://api.nasa.gov";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spaceImage = findViewById(R.id.space_image);
        description = findViewById(R.id.description);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api_nasa)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SpaceService spaceService = retrofit.create(SpaceService.class);
        Call<SpaceInfo> call = spaceService.getSpaceInfo("DEMO_KEY");
        call.enqueue(new SpaceCallback());
    }

    class SpaceCallback implements Callback<SpaceInfo>{

        @Override
        public void onResponse(Call<SpaceInfo> call, Response<SpaceInfo> response) {
            if (response.isSuccessful()){
                SpaceInfo spaceInfo = response.body();
                String s = spaceInfo.title + "\n"
                        + spaceInfo.explanation;
                description.setText(s);
                Picasso.get()
                        .load(spaceInfo.url)
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(spaceImage);
            }
            else {
                description.setText(response.message());
            }
        }

        @Override
        public void onFailure(Call<SpaceInfo> call, Throwable t) {
            description.setText(t.getMessage());
        }
    }
}