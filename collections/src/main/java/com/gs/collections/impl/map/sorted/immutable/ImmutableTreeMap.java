/*
 * Copyright 2015 Goldman Sachs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gs.collections.impl.map.sorted.immutable;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.sorted.ImmutableSortedMap;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.factory.SortedSets;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.tuple.ImmutableEntry;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.MapIterate;
import net.jcip.annotations.Immutable;

/**
 * @see ImmutableSortedMap
 */
@Immutable
public class ImmutableTreeMap<K, V>
        extends AbstractImmutableSortedMap<K, V>
        implements Serializable
{
    private final K[] keys;
    private final V[] values;
    private final Comparator<? super K> comparator;

    public ImmutableTreeMap(SortedMap<K, V> sortedMap)
    {
        if (sortedMap == null)
        {
            throw new NullPointerException("Cannot convert null to ImmutableSortedMap");
        }
        this.comparator = sortedMap.comparator();
        K[] keysCopy = (K[]) new Object[sortedMap.size()];
        V[] valuesCopy = (V[]) new Object[sortedMap.size()];
        int index = 0;
        for (Entry<K, V> entry : sortedMap.entrySet())
        {
            keysCopy[index] = entry.getKey();
            valuesCopy[index] = entry.getValue();
            index++;
        }
        this.keys = keysCopy;
        this.values = valuesCopy;
    }

    public static <K, V> ImmutableSortedMap<K, V> newMap(SortedMap<K, V> sortedMap)
    {
        return new ImmutableTreeMap<K, V>(sortedMap);
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof Map))
        {
            return false;
        }
        Map<?, ?> other = (Map<?, ?>) object;
        if (this.size() != other.size())
        {
            return false;
        }
        for (int index = 0; index < this.size(); index++)
        {
            if (!Comparators.nullSafeEquals(this.values[index], other.get(this.keys[index])))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int length = this.keys.length;
        int hashCode = 0;
        for (int index = 0; index < length; index++)
        {
            K key = this.keys[index];
            V value = this.values[index];
            hashCode += (key == null ? 0 : key.hashCode())
                    ^ (value == null ? 0 : value.hashCode());
        }
        return hashCode;
    }

    @Override
    public String toString()
    {
        int length = this.keys.length;
        if (length == 0)
        {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (int index = 0; index < length; index++)
        {
            sb.append(this.keys[index]);
            sb.append('=');
            sb.append(this.values[index]);
            if (index < length - 1)
            {
                sb.append(',').append(' ');
            }
        }
        return sb.append('}').toString();
    }

    public int size()
    {
        return this.keys.length;
    }

    public boolean containsKey(Object key)
    {
        return Arrays.binarySearch(this.keys, (K) key, this.comparator) >= 0;
    }

    public boolean containsValue(Object value)
    {
        return ArrayIterate.contains(this.values, value);
    }

    public V get(Object key)
    {
        int index = Arrays.binarySearch(this.keys, (K) key, this.comparator);
        if (index >= 0)
        {
            return this.values[index];
        }
        return null;
    }

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure)
    {
        for (int index = 0; index < this.keys.length; index++)
        {
            procedure.value(this.keys[index], this.values[index]);
        }
    }

    public ImmutableMap<V, K> flipUniqueValues()
    {
        return MapIterate.flipUniqueValues(this).toImmutable();
    }

    public RichIterable<K> keysView()
    {
        return FastList.newListWith(this.keys).asLazy();
    }

    public RichIterable<V> valuesView()
    {
        return FastList.newListWith(this.values).asLazy();
    }

    public RichIterable<Pair<K, V>> keyValuesView()
    {
        return FastList.newListWith(this.keys).asLazy().zip(FastList.newListWith(this.values));
    }

    public Comparator<? super K> comparator()
    {
        return this.comparator;
    }

    public K firstKey()
    {
        if (this.keys.length == 0)
        {
            throw new NoSuchElementException();
        }
        return this.keys[0];
    }

    public K lastKey()
    {
        if (this.keys.length == 0)
        {
            throw new NoSuchElementException();
        }
        return this.keys[this.keys.length - 1];
    }

    public Set<K> keySet()
    {
        return new ImmutableSortedMapKeySet();
    }

    public Collection<V> values()
    {
        return FastList.newListWith(this.values).asUnmodifiable();
    }

    public Set<Entry<K, V>> entrySet()
    {
        int length = this.keys.length;
        MutableSortedSet<Entry<K, V>> entrySet = SortedSets.mutable.with(new EntryComparator<K, V>(this.comparator));
        for (int index = 0; index < length; index++)
        {
            entrySet.add(ImmutableEntry.of(this.keys[index], this.values[index]));
        }
        return entrySet.toImmutable().castToSortedSet();
    }

    protected Object writeReplace()
    {
        return new ImmutableSortedMapSerializationProxy<K, V>(this);
    }

    private static final class EntryComparator<K, V> implements Comparator<Entry<K, V>>
    {
        private final Comparator<? super K> comparator;

        private EntryComparator(Comparator<? super K> comparator)
        {
            if (comparator == null)
            {
                this.comparator = Comparators.naturalOrder();
            }
            else
            {
                this.comparator = comparator;
            }
        }

        public int compare(Entry<K, V> o1, Entry<K, V> o2)
        {
            return this.comparator.compare(o1.getKey(), o2.getKey());
        }
    }

    protected class ImmutableSortedMapKeySet extends AbstractSet<K> implements Serializable
    {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean contains(Object o)
        {
            return ImmutableTreeMap.this.containsKey(o);
        }

        @Override
        public boolean containsAll(Collection<?> collection)
        {
            return Iterate.allSatisfyWith(collection, Predicates2.in(), this);
        }

        @Override
        public int size()
        {
            return ImmutableTreeMap.this.keys.length;
        }

        @Override
        public Iterator<K> iterator()
        {
            return FastList.newListWith(ImmutableTreeMap.this.keys).asUnmodifiable().iterator();
        }

        @Override
        public Object[] toArray()
        {
            int size = ImmutableTreeMap.this.keys.length;
            Object[] result = new Object[size];
            System.arraycopy(ImmutableTreeMap.this.keys, 0, result, 0, size);
            return result;
        }

        @Override
        public <T> T[] toArray(T[] a)
        {
            T[] result = a.length < this.size()
                    ? (T[]) Array.newInstance(a.getClass().getComponentType(), this.size())
                    : a;

            System.arraycopy(ImmutableTreeMap.this.keys, 0, result, 0, ImmutableTreeMap.this.keys.length);

            if (result.length > this.size())
            {
                result[this.size()] = null;
            }
            return result;
        }

        @Override
        public boolean add(K key)
        {
            throw new UnsupportedOperationException("Cannot call add() on " + this.getClass().getSimpleName());
        }

        @Override
        public boolean addAll(Collection<? extends K> collection)
        {
            throw new UnsupportedOperationException("Cannot call addAll() on " + this.getClass().getSimpleName());
        }

        @Override
        public boolean remove(Object key)
        {
            throw new UnsupportedOperationException("Cannot call remove() on " + this.getClass().getSimpleName());
        }

        @Override
        public boolean removeAll(Collection<?> collection)
        {
            throw new UnsupportedOperationException("Cannot call removeAll() on " + this.getClass().getSimpleName());
        }

        @Override
        public void clear()
        {
            throw new UnsupportedOperationException("Cannot call clear() on " + this.getClass().getSimpleName());
        }

        @Override
        public boolean retainAll(Collection<?> collection)
        {
            throw new UnsupportedOperationException("Cannot call retainAll() on " + this.getClass().getSimpleName());
        }

        protected Object writeReplace()
        {
            return TreeSortedSet.newSetWith(ImmutableTreeMap.this.comparator, ImmutableTreeMap.this.keys).toImmutable();
        }
    }
}

