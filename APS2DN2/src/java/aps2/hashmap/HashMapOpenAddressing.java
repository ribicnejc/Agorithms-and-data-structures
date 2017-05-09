package aps2.hashmap;

/**
 * Hash map with open addressing.
 */
public class HashMapOpenAddressing {
    private Element table[]; // table content, if element is not present, use Integer.MIN_VALUE for Element's key
    private HashFunction.HashingMethod h;
    private CollisionProbeSequence c;

    public static enum CollisionProbeSequence {
        LinearProbing,    // new h(k) = (h(k) + i) mod m
        QuadraticProbing, // new h(k) = (h(k) + i^2) mod m
        DoubleHashing     // new h(k) = (h(k) + i*h(k)) mod m
    }

    ;

    private int linearProbing(int k, int i, int m) {
        if (this.h == HashFunction.HashingMethod.DivisionMethod) {
            return (HashFunction.DivisionMethod(k, m) + i) % Math.abs(m);
        } else {
            return (HashFunction.KnuthMethod(k, m) + i) % Math.abs(m);
        }
    }

    private int quadraticProbing(int k, int i, int m) {
        if (this.h == HashFunction.HashingMethod.DivisionMethod) {
            return (HashFunction.DivisionMethod(k, m) + i * i) % Math.abs(m);
        } else {
            return (HashFunction.KnuthMethod(k, m) + i * i) % Math.abs(m);
        }
    }

    private int doubleHashing(int k, int i, int m) {
        if (this.h == HashFunction.HashingMethod.DivisionMethod) {
            return (HashFunction.DivisionMethod(k, m) + i * HashFunction.DivisionMethod(k, m)) % Math.abs(m);
        } else {
            return (HashFunction.KnuthMethod(k, m) + i * HashFunction.KnuthMethod(k, m)) % Math.abs(m);
        }
    }

    public HashMapOpenAddressing(int m, HashFunction.HashingMethod h, CollisionProbeSequence c) {
        this.table = new Element[m];
        this.h = h;
        this.c = c;

        // init empty slot as MIN_VALUE
        for (int i = 0; i < m; i++) {
            table[i] = new Element(Integer.MIN_VALUE, "");
        }
    }

    public Element[] getTable() {
        return this.table;
    }

    /**
     * If the element doesn't exist yet, inserts it into the set.
     *
     * @param k Element key
     * @param v Element value
     * @return true, if element was added; false otherwise.
     */
    public boolean add(int k, String v) {
        int id;
        for (int i = 0; i < this.table.length; i++) {
            switch (this.c) {
                case LinearProbing:
                    id = linearProbing(k, i, this.table.length);
                    if (this.table[id].key == Integer.MIN_VALUE) {
                        this.table[id] = new Element(k, v);
                        return true;
                    }
                    break;
                case QuadraticProbing:
                    id = quadraticProbing(k, i, this.table.length);
                    if (this.table[id].key == Integer.MIN_VALUE) {
                        this.table[id] = new Element(k, v);
                        return true;
                    }
                    break;
                case DoubleHashing:
                    id = doubleHashing(k, i, this.table.length);
                    if (this.table[id].key == Integer.MIN_VALUE) {
                        this.table[id] = new Element(k, v);
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    /**
     * Removes the element from the set.
     *
     * @param k Element key
     * @return true, if the element was removed; otherwise false
     */
    public boolean remove(int k) {
        int id;
        for (int i = 0; i < this.table.length; i++) {
            switch (this.c) {
                case LinearProbing:
                    id = linearProbing(k, i, this.table.length);
                    if (this.table[id].key == k) {
                        this.table[id].key = Integer.MIN_VALUE;
                        this.table[id].value = null;
                        return true;
                    }
                    break;
                case QuadraticProbing:
                    id = quadraticProbing(k, i, this.table.length);
                    if (this.table[id].key == k) {
                        this.table[id].key = Integer.MIN_VALUE;
                        this.table[id].value = null;
                        return true;
                    }
                    break;
                case DoubleHashing:
                    id = doubleHashing(k, i, this.table.length);
                    if (this.table[id].key == k) {
                        this.table[id].key = Integer.MIN_VALUE;
                        this.table[id].value = null;
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    /**
     * Finds the element.
     *
     * @param k Element key
     * @return true, if the element was found; false otherwise.
     */
    public boolean contains(int k) {
        int id;
        for (int i = 0; i < this.table.length; i++) {
            switch (this.c) {
                case LinearProbing:
                    id = linearProbing(k, i, this.table.length);
                    if (this.table[id].key == k) {
                        return true;
                    }
                    break;
                case QuadraticProbing:
                    id = quadraticProbing(k, i, this.table.length);
                    if (this.table[id].key == k) {
                        return true;
                    }
                    break;
                case DoubleHashing:
                    id = doubleHashing(k, i, this.table.length);
                    if (this.table[id].key == k) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    /**
     * Maps the given key to its value, if the key exists in the hash map.
     *
     * @param k Element key
     * @return The value for the given key or null, if such a key does not exist.
     */
    public String get(int k) {
        int id;
        for (int i = 0; i < this.table.length; i++) {
            switch (this.c) {
                case LinearProbing:
                    id = linearProbing(k, i, this.table.length);
                    if (this.table[id].key == k) {
                        return this.table[id].value;
                    }
                    break;
                case QuadraticProbing:
                    id = quadraticProbing(k, i, this.table.length);
                    if (this.table[id].key == k) {
                        return this.table[id].value;
                    }
                    break;
                case DoubleHashing:
                    id = doubleHashing(k, i, this.table.length);
                    if (this.table[id].key == k) {
                        return this.table[id].value;
                    }
                    break;
            }
        }
        return null;
    }
}

