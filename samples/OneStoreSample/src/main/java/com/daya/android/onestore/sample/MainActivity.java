package com.daya.android.onestore.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.skplanet.dodo.IapPlugin;
import com.skplanet.dodo.ProcessType;
import com.skplanet.dodo.helper.PaymentParams;
import com.skplanet.dodo.pdu.Action;
import com.skplanet.dodo.pdu.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "OneStoreSample";

    /**
     * 개발자 센터에서 생성/발행되는 Application ID(AID)
     * 영문자 OA와 숫자를 포함한 10글자
     */
    private static final String APP_ID = "OA00726404";

    /**
     * 개발자 센터에서 생성발생되는 앱 내부 상품(product) ID
     * 인앱 상품 ID, 10자리 숫자
     */
    private static final String PRODUCT_ID = "0910100008";

    /**
     * 개발사에서 생성한 ID 값
     * 결제시 개발사에서 발행하는 결제에 대한 고유 ID 값
     * (원스토어에서 발생하는 TXID값과는 별개로 개발사에서 발행 관리 할 수 있는 결제 transaction의 고유값
     * TID 사용시 서버에서 제공하는 'TID 구매이력 조회 API'를 통해 결제 진행 여부와 상관없이 구매 이력의 조회가 가능
     */
    private static final String TID = "01234";

    /**
     * 캠페인 통계 등을 위해서 개발사가 자유롭게 사용하는 태그
     */
    private static final String BP_INFO = "bp info";

    /**
     * 인앱 상품명
     * 개발사에서 선택적으로 결제 화면에 설정할 수 있는 상품명
     * PID는 동일하나 이벤트 등으로 상품명을 일시적으로 바꿔야하는 경우 활용 가능
     * 상품이 매우 다양하나 금액이 동일한 경우 하나의 PID 설정 후 상품명을 다양화하여 활용 가능
     */
    private static final String PRODUCT_NAME = "OneStoreSample";

    /**
     * 원스토어 프로모션 중복 참여 방지를 위한 값으로 어플리케이션 사용자의 고유 식별 정보를 전달
     * (정보 노출을 피하기 위해 Hash 하여 전달 권장)
     * 16.04에 추가
     */
    private static final String USER_ID = "shnoble";

    /**
     * gameUserId 사용자가 원스토어 프로모션에 참가 가능한지 여부를 설정
     * 16.04에 추가
     */
    private static final boolean PROMOTION_APPLICABLE = false;

    private IapPlugin mOneStorePlugin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOneStorePlugin = IapPlugin.getPlugin(this, IapPlugin.DEVELOPMENT_MODE);

        findViewById(R.id.query_product_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onQueryProductInfoButtonClicked();
            }
        });

        findViewById(R.id.query_purchases).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onQueryPurchasesButtonClicked();
            }
        });

        findViewById(R.id.check_available_purchase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckAvailablePurchaseButtonClicked();
            }
        });

        findViewById(R.id.request_payment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRequestPaymentButtonClicked();
            }
        });

        findViewById(R.id.subtract_points).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubtractPointsButtonClicked();
            }
        });
    }

    private void onQueryProductInfoButtonClicked() {
        String requestId = mOneStorePlugin.sendCommandProductInfo(new IapPlugin.AbsRequestCallback() {
            @Override
            protected void onResponse(Response response) {
                Log.d(TAG, response.toString());
            }

            @Override
            public void onError(String requestId, String errorCode, String errorMessage) {

            }
        }, ProcessType.FOREGROUND_IF_NEEDED, APP_ID);
    }

    private void onQueryPurchasesButtonClicked() {
        String requestId = mOneStorePlugin.sendCommandPurchaseHistory(new IapPlugin.AbsRequestCallback() {
            @Override
            protected void onResponse(Response response) {
                Log.d(TAG, response.toString());
            }

            @Override
            public void onError(String requestId, String errorCode, String errorMessage) {

            }
        }, ProcessType.FOREGROUND_IF_NEEDED, APP_ID, null);
    }

    private void onCheckAvailablePurchaseButtonClicked() {
        String requestId = mOneStorePlugin.sendCommandCheckPurchasability(new IapPlugin.AbsRequestCallback() {
            @Override
            protected void onResponse(Response response) {
                Log.d(TAG, response.toString());
            }

            @Override
            public void onError(String requestId, String errorCode, String errorMessage) {

            }
        }, ProcessType.FOREGROUND_IF_NEEDED, APP_ID, PRODUCT_ID);
    }

    private void onRequestPaymentButtonClicked() {
        String appId = APP_ID;
        String productId = PRODUCT_ID;
        String tid = TID;
        String bpInfo = BP_INFO;
        String productName = PRODUCT_NAME;
        String gameUserId = USER_ID;

        PaymentParams.Builder builder = new PaymentParams.Builder(appId, productId);
        builder.addTid(tid);
        builder.addBpInfo(bpInfo);

        if (!TextUtils.isEmpty(productName)) {
            builder.addProductName(productName);
        }

        if (!TextUtils.isEmpty(gameUserId)) {
            builder.addGameUserId(gameUserId).
                    addPromotionApplicable(PROMOTION_APPLICABLE);
        }

        PaymentParams params = builder.build();
        String requestId = mOneStorePlugin.sendPaymentRequest(new IapPlugin.AbsRequestCallback() {

            @Override
            protected void onResponse(Response response) {
                Log.d(TAG, response.toString());
            }

            @Override
            public void onError(String requestId, String errorCode, String errorMessage) {

            }
        }, params);
        Log.d(TAG, "Request ID: " + requestId);
    }

    private void onSubtractPointsButtonClicked() {
        String requestId = mOneStorePlugin.sendCommandChangeProductProperties(new IapPlugin.AbsRequestCallback() {
            @Override
            protected void onResponse(Response response) {
                Log.d(TAG, response.toString());
            }

            @Override
            public void onError(String requestId, String errorCode, String errorMessage) {

            }
        }, ProcessType.FOREGROUND_IF_NEEDED, APP_ID, Action.subtract_points.action(), PRODUCT_ID);
    }
}
