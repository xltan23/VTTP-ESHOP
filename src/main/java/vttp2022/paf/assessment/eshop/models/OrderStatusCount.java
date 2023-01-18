package vttp2022.paf.assessment.eshop.models;

public class OrderStatusCount {
    
    private String name;
    private Integer dispatched = 0;
    private Integer pending = 0;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getDispatched() {
        return dispatched;
    }
    public void setDispatched(Integer dispatched) {
        this.dispatched = dispatched;
    }
    public Integer getPending() {
        return pending;
    }
    public void setPending(Integer pending) {
        this.pending = pending;
    }

    @Override
    public String toString() {
        return "OrderStatusCount [name=" + name + ", dispatched=" + dispatched + ", pending=" + pending + "]";
    }
}
