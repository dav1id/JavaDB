import java.util.ArrayDeque;

enum bufferState {
    BUFFER_CLOSED,
    BUFFER_OPEN
}

interface Buffer <K>{
    public default bufferState closeBuffer(){
        return bufferState.BUFFER_CLOSED;
    }

    public default bufferState returnBufferStatus(){
        return bufferState.BUFFER_CLOSED;
    };

    public K getBufferInput();

    public K getPreviousContents();

}

class inputBuffer <T extends Number, K> implements Buffer<K>{
    private T buffer_length;
    private T input_length;
    private final ArrayDeque<K> buffer;

    public inputBuffer(T buffer_length, T input_length){
        this.buffer_length = buffer_length;
        this.input_length = input_length;
        buffer = new ArrayDeque<>();
    }

    public bufferState returnBufferStatus(){
        if (!(buffer.isEmpty()))
            return bufferState.BUFFER_OPEN;

        return bufferState.BUFFER_CLOSED;
    }

    public K getBufferInput(){
        return buffer.getFirst();
    }

    public void setBufferInput(K contents){
        buffer.addFirst(contents);
    }

    public K getPreviousContents(){
        return buffer.getLast();
    }
}
