import java.lang.reflect.Array;

public class GenericFilter {
    public static <T> T[] filter(T[] array, Filter filter) {
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length);
        for (int i = 0; i < array.length; i++) {
            result[i] = (T) filter.apply(array[i]);
        }
        return result;
    }

    public interface Filter {
        Object apply(Object o);
    }

    public static class UpperCaseFilter implements Filter {
        @Override
        public Object apply(Object o) {
            return o instanceof String ? ((String) o).toUpperCase() : o;
        }
    }

    public static void main(String[] args) {
        String[] words = {"hello", "world"};
        Filter upperCaseFilter = new UpperCaseFilter();
        String[] filtered = filter(words, upperCaseFilter);
        for (String word : filtered) {
            System.out.println(word);
        }
    }
}
