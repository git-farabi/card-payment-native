package com.example.card_payment_native;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {
    public static String getRemoteApplicationId(Context context){
        Properties properties = new Properties();

        try(InputStream input = context.getAssets().open("config.properties");){
            properties.load(input);
        }catch (IOException ex){
            ex.printStackTrace();
        }

        return properties.getProperty("remoteApplicationId");
    }
}
