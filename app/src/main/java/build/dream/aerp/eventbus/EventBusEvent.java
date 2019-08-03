package build.dream.aerp.eventbus;

public class EventBusEvent {
    private Object source;
    private String type;

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
