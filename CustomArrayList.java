import java.util.AbstractList;
import java.util.Arrays;

public class CustomArrayList<T> extends AbstractList<T> {
    private Object[] elements;
    private int size;

    public CustomArrayList() {
        elements = new Object[10];
        size = 0;
    }

    private void checkIndex(int index) {
        // Проверка индекса
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс " + index + " вне границ размера списка " + size);
        }
    }

    @Override
    public T get(int index) {
        checkIndex(index);
        return (T) elements[index];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(T element) {
        // Авторасширение массива при достижении его предела
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, elements.length * 2);
        }
        // Добавление нового элемента в массив
        elements[size++] = element;
        return true;
    }

    @Override
    public T remove(int index) {
        checkIndex(index);
        T removedElement = (T) elements[index];
        // Сдвиг элементов влево на место удаляемого элемента
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
        return removedElement;
    }
    
}
