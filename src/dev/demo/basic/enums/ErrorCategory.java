package dev.demo.basic.enums;

public enum ErrorCategory {
    NoCategory(0, "场景未分类"),
    CumulantEvent(1, "场景1"),
    QotingItemReturn(2, "场景2"),
    Voucher(3, "场景3"),
    Estimate(4, "场景4"),
    BusinessContinuity(5, "场景5"),
    BusinessChange(6, "场景6");
    public final Integer id;
    public final String content;

    ErrorCategory(Integer id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public static ErrorCategory fromId(Integer id) {
        for (ErrorCategory errorCategory:ErrorCategory.values()){
            if(errorCategory.id==id){
                return errorCategory;
            }
        }
        return NoCategory;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
