@Bean
public class TestPojo {

    private String value;

    private Integer param;

    public TestPojo(String value, Integer param) {
        this.value = value;
        this.param = param;
    }

    public TestPojo(String value) {
        this.value = value;
        this.param = 42;
    }

    public String getValue() {
        return this.value;
    }

    public Integer getParam() {
        return this.param;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setParam(Integer param) {
        this.param = param;
    }
}
