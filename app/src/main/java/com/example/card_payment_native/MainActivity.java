package com.example.card_payment_native;

import android.accounts.Account;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// env
import com.example.card_payment_native.Utils;

// clover libs
import com.clover.connector.sdk.v3.PaymentConnector;
import com.clover.sdk.v3.connector.ExternalIdUtils;
import com.clover.sdk.v3.connector.IPaymentConnectorListener;
import com.clover.sdk.v3.remotepay.AuthResponse;
import com.clover.sdk.v3.remotepay.CapturePreAuthResponse;
import com.clover.sdk.v3.remotepay.CloseoutResponse;
import com.clover.sdk.v3.remotepay.ConfirmPaymentRequest;
import com.clover.sdk.v3.remotepay.ManualRefundResponse;
import com.clover.sdk.v3.remotepay.PreAuthResponse;
import com.clover.sdk.v3.remotepay.ReadCardDataResponse;
import com.clover.sdk.v3.remotepay.RefundPaymentResponse;
import com.clover.sdk.v3.remotepay.RetrievePaymentResponse;
import com.clover.sdk.v3.remotepay.RetrievePendingPaymentsResponse;
import com.clover.sdk.v3.remotepay.SaleRequest;
import com.clover.sdk.v3.remotepay.SaleResponse;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v3.remotepay.TipAdded;
import com.clover.sdk.v3.remotepay.TipAdjustAuthResponse;
import com.clover.sdk.v3.remotepay.VaultCardResponse;
import com.clover.sdk.v3.remotepay.VerifySignatureRequest;
import com.clover.sdk.v3.remotepay.VoidPaymentRefundResponse;
import com.clover.sdk.v3.remotepay.VoidPaymentResponse;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private PaymentConnector paymentConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // initialize payment connector
        paymentConnector = initializePaymentConnector();

        //  TEXT VIEW
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.textView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // BUTTON
        Button saleButton = findViewById(R.id.saleButton);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Sale button pressed!", Toast.LENGTH_LONG).show();

                SaleRequest saleRequest = setupSaleRequest();

                if(paymentConnector != null){
                    paymentConnector.sale(saleRequest);
                }else{
                    Log.e(TAG, "Paymentconnector is not initialized!");
                    Toast.makeText(MainActivity.this, "Paymentconnector is not initialized!", Toast.LENGTH_LONG).show();
                }
            }


        });
    }

    private PaymentConnector initializePaymentConnector(){
        // get account details
        Account cloverAccount = CloverAccount.getAccount(this);
        if(cloverAccount == null){
            Log.e(TAG, "Clover account is null!");
            Toast.makeText(this, "CLover account unavailable", Toast.LENGTH_LONG).show();

            return null;
        }

        String remoteApplicationId = Utils.getRemoteApplicationId(this);

        IPaymentConnectorListener paymentConnectorListener = new IPaymentConnectorListener() {
            @Override
            public void onPreAuthResponse(PreAuthResponse response) {

            }

            @Override
            public void onAuthResponse(AuthResponse response) {

            }

            @Override
            public void onTipAdjustAuthResponse(TipAdjustAuthResponse response) {

            }

            @Override
            public void onCapturePreAuthResponse(CapturePreAuthResponse response) {

            }

            @Override
            public void onVerifySignatureRequest(VerifySignatureRequest request) {

            }

            @Override
            public void onConfirmPaymentRequest(ConfirmPaymentRequest request) {

            }

            @Override
            public void onSaleResponse(SaleResponse response) {
                String result;

                if(response.getSuccess()){
                    result = "Sale Success!";
                }else {
                    result = "Sale unsuccessful: " + response.getReason() + ": " + response.getMessage();
                }

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onManualRefundResponse(ManualRefundResponse response) {

            }

            @Override
            public void onRefundPaymentResponse(RefundPaymentResponse response) {

            }

            @Override
            public void onTipAdded(TipAdded tipAdded) {

            }

            @Override
            public void onVoidPaymentResponse(VoidPaymentResponse response) {

            }

            @Override
            public void onVaultCardResponse(VaultCardResponse response) {

            }

            @Override
            public void onRetrievePendingPaymentsResponse(RetrievePendingPaymentsResponse retrievePendingPaymentResponse) {

            }

            @Override
            public void onReadCardDataResponse(ReadCardDataResponse response) {

            }

            @Override
            public void onCloseoutResponse(CloseoutResponse response) {

            }

            @Override
            public void onRetrievePaymentResponse(RetrievePaymentResponse response) {

            }

            @Override
            public void onVoidPaymentRefundResponse(VoidPaymentRefundResponse response) {

            }

            @Override
            public void onDeviceDisconnected() {

            }

            @Override
            public void onDeviceConnected() {

            }
        };

        return  new PaymentConnector(this, cloverAccount, paymentConnectorListener, remoteApplicationId);
    }

    private SaleRequest setupSaleRequest(){
        SaleRequest saleRequest = new SaleRequest();
        saleRequest.setExternalId(ExternalIdUtils.generateNewID());
        saleRequest.setAmount(1000L);

        return saleRequest;
    }

    protected void onDestroy(){
        super.onDestroy();

        if(paymentConnector != null){
            paymentConnector.dispose();
        }
    }
}