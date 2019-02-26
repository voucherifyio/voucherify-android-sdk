package io.voucherify.android.client.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class PromotionTier {

    public static PromotionTier create(String id) {
        return new PromotionTier(id);
    }

    private String id;
    private String object;
    private String name;
    private String banner;
    private Campaign campaign;
    private Condition condition;
    private Action action;
    private Map<String, Object> metadata;

    @SerializedName("validation_rule_assignments")
    private List<ValidationRuleAssignmentsList> validationRuleAssignments;

    public PromotionTier(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public String getName() {
        return name;
    }

    public String getBanner() {
        return banner;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public Condition getCondition() {
        return condition;
    }

    public Action getAction() {
        return action;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public List<ValidationRuleAssignmentsList> getValidationRuleAssignments() {
        return validationRuleAssignments;
    }
}
