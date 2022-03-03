package reco.challenge.model;

import java.io.Serializable;

public class SummaryRow implements Serializable {
    String UserId;
    String SessionId;
    String ProductIds;

    public SummaryRow(){

    }

    public SummaryRow(String gUserId, String gSessionId, String gProductIds){
        this.UserId = gUserId;
        this.SessionId = gSessionId;
        this.ProductIds = gProductIds;
    }
    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getSessionId() {
        return SessionId;
    }

    public void setSessionId(String sessionId) {
        SessionId = sessionId;
    }

    public String getProductIds() {
        return ProductIds;
    }

    public void setProductIds(String productIds) {
        ProductIds = productIds;
    }
}
