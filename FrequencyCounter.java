import java.util.HashMap;
import java.util.Map;

public class FrequencyCounter {
    public static <T> Map<T, Integer> countFrequencies(T[] array) {
        Map<T, Integer> frequencyMap = new HashMap<>();
        for (T element : array) {
            frequencyMap.put(element, frequencyMap.getOrDefault(element, 0) + 1);
        }
        return frequencyMap;
    }

    public static void main(String[] args) {
        String[] words = {"apple", "banana", "apple", "orange", "banana", "apple"};
        System.out.println(countFrequencies(words));
    }
}
