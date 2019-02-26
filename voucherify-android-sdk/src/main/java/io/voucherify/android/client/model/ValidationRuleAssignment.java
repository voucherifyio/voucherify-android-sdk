package io.voucherify.android.client.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ValidationRuleAssignment {

    private String id;

    @SerializedName("rule_id")
    private String ruleId;

    @SerializedName("related_object_id")
    private String relatedObjectId;

    @SerializedName("related_object_type")
    private String relatedObjectType;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("updated_at")
    private Date updatedAt;

    private String object;

    public String getId() {
        return id;
    }

    public String getRuleId() {
        return ruleId;
    }

    public String getRelatedObjectId() {
        return relatedObjectId;
    }

    public String getRelatedObjectType() {
        return relatedObjectType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getObject() {
        return object;
    }
}
