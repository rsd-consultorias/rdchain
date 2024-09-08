package br.com.rsdconsultoria.rdcoin.dto;

public class ValidationState<T> {
    private ModeState state;

    private T result;
    private String message;

    public ModeState getState() {
        return state;
    }

    public void setState(ModeState state) {
        this.state = state;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public enum ModeState {
        M_VALID, // !< everything ok
        M_INVALID, // !< network rule violation (DoS value may be set)
        M_ERROR, // !< run-time error
    }
}
