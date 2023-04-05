public enum Response {
    OK("ok"), FAIL("fail"), FINISH("finish");

    private final String value;

    Response(String value) {
        this.value = value;
    }

    public static Response create(String value) {
        for (Response response : Response.values()) {
            if (response.value.equalsIgnoreCase(value)) {
                return response;
            }
        }

        return Response.FAIL;
    }

    public String getValue() {
        return value;
    }
}
