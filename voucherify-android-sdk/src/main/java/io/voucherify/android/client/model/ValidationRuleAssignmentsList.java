package io.voucherify.android.client.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ValidationRuleAssignmentsList {

    private String object;

    @SerializedName("data_ref")
    private String dataRef;

    private int total;

    private List<ValidationRuleAssignment> data;

    public String getObject() {
        return object;
    }

    public String getDataRef() {
        return dataRef;
    }

    public int getTotal() {
        return total;
    }

    public List<ValidationRuleAssignment> getData() {
        return data;
    }
}
