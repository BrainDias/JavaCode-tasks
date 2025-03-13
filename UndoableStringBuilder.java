import java.util.Stack;

public class UndoableStringBuilder {
    private StringBuilder stringBuilder;
    private Stack<String> history; // Храним предыдущие состояния

    public UndoableStringBuilder() {
        this.stringBuilder = new StringBuilder();
        this.history = new Stack<>();
    }

    private void saveSnapshot() {
        history.push(stringBuilder.toString());
    }

    public UndoableStringBuilder append(String str) {
        saveSnapshot();
        stringBuilder.append(str);
        return this;
    }

    public UndoableStringBuilder insert(int offset, String str) {
        saveSnapshot();
        stringBuilder.insert(offset, str);
        return this;
    }

    public UndoableStringBuilder delete(int start, int end) {
        saveSnapshot();
        stringBuilder.delete(start, end);
        return this;
    }

    public UndoableStringBuilder replace(int start, int end, String str) {
        saveSnapshot();
        stringBuilder.replace(start, end, str);
        return this;
    }

    public UndoableStringBuilder reverse() {
        saveSnapshot();
        stringBuilder.reverse();
        return this;
    }

    public void undo() {
        if (!history.isEmpty()) {
            stringBuilder = new StringBuilder(history.pop());
        }
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        UndoableStringBuilder usb = new UndoableStringBuilder();
        usb.append("Hello").append(" World");
        System.out.println(usb);

        usb.delete(5, 11);
        System.out.println(usb);

        usb.undo();
        System.out.println(usb);
    }
}
