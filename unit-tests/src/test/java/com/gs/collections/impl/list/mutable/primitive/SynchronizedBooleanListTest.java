/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.list.mutable.primitive;

import java.util.NoSuchElementException;

import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.block.factory.primitive.BooleanPredicates;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link SynchronizedBooleanList}.
 */
public class SynchronizedBooleanListTest
{
    private final MutableBooleanList list = new SynchronizedBooleanList(BooleanArrayList.newListWith(true, false, true));

    @Test
    public void newListWith()
    {
        Verify.assertSize(3, this.list);
        Assert.assertTrue(this.list.containsAll(true, false, true));
    }

    @Test
    public void newList()
    {
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true), BooleanArrayList.newList(this.list));
    }

    @Test
    public void isEmpty()
    {
        Verify.assertEmpty(new SynchronizedBooleanList(new BooleanArrayList()));
        Verify.assertNotEmpty(this.list);
    }

    @Test
    public void clear()
    {
        this.list.clear();
        Verify.assertSize(0, this.list);
        Assert.assertFalse(this.list.contains(true));
        Assert.assertFalse(this.list.contains(false));
    }

    @Test
    public void containsAllArray()
    {
        Assert.assertTrue(this.list.containsAll(true));
        Assert.assertTrue(this.list.containsAll(true, false, true));
    }

    @Test
    public void containsAllIterable()
    {
        Assert.assertTrue(new SynchronizedBooleanList(new BooleanArrayList()).containsAll(new BooleanArrayList()));
        Assert.assertFalse(new SynchronizedBooleanList(new BooleanArrayList()).containsAll(BooleanArrayList.newListWith(true)));
        Assert.assertTrue(this.list.containsAll(BooleanArrayList.newListWith(true)));
        Assert.assertTrue(this.list.containsAll(BooleanArrayList.newListWith(true, false, true)));
    }

    @Test
    public void get()
    {
        Assert.assertTrue(this.list.get(0));
        Assert.assertFalse(this.list.get(1));
        Assert.assertTrue(this.list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void get_throws_index_greater_than_size()
    {
        this.list.get(3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void get_throws_index_negative()
    {
        this.list.get(-1);
    }

    @Test
    public void getFirst()
    {
        MutableBooleanList singleItemList = new SynchronizedBooleanList(BooleanArrayList.newListWith(false));
        Assert.assertFalse(singleItemList.getFirst());
        Assert.assertTrue(this.list.getFirst());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getFirst_emptyList_throws()
    {
        MutableBooleanList emptyList = new SynchronizedBooleanList(new BooleanArrayList());
        emptyList.getFirst();
    }

    @Test
    public void getLast()
    {
        MutableBooleanList singleItemList = new SynchronizedBooleanList(BooleanArrayList.newListWith(false));
        Assert.assertFalse(singleItemList.getLast());
        Assert.assertTrue(this.list.getLast());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getLast_emptyList_throws()
    {
        MutableBooleanList emptyList = new SynchronizedBooleanList(new BooleanArrayList());
        emptyList.getLast();
    }

    @Test
    public void indexOf()
    {
        MutableBooleanList arrayList = new SynchronizedBooleanList(BooleanArrayList.newListWith(false, true, false));
        Assert.assertEquals(0L, arrayList.indexOf(false));
        Assert.assertEquals(1L, arrayList.indexOf(true));
    }

    @Test
    public void lastIndexOf()
    {
        MutableBooleanList arrayList = new SynchronizedBooleanList(BooleanArrayList.newListWith(true, false, true));
        Assert.assertEquals(2L, arrayList.lastIndexOf(true));
        Assert.assertEquals(1L, arrayList.lastIndexOf(false));
    }

    @Test
    public void add()
    {
        MutableBooleanList emptyList = new SynchronizedBooleanList(new BooleanArrayList());
        Assert.assertTrue(emptyList.add(true));
        Assert.assertEquals(BooleanArrayList.newListWith(true), emptyList);
        Assert.assertTrue(this.list.add(false));
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true, false), this.list);
    }

    @Test
    public void addAtIndex()
    {
        MutableBooleanList emptyList = new SynchronizedBooleanList(new BooleanArrayList());
        emptyList.addAtIndex(0, true);
        Assert.assertEquals(BooleanArrayList.newListWith(true), emptyList);
        MutableBooleanList arrayList = this.list;
        arrayList.addAtIndex(3, false);
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true, false), arrayList);
        arrayList.addAtIndex(2, false);
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, false, true, false), arrayList);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addAtIndex_throws_index_greater_than_size()
    {
        MutableBooleanList emptyList = new SynchronizedBooleanList(new BooleanArrayList());
        emptyList.addAtIndex(1, false);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addAtIndex_throws_index_negative()
    {
        this.list.addAtIndex(-1, true);
    }

    @Test
    public void addAllArray()
    {
        Assert.assertFalse(this.list.addAll());
        Assert.assertFalse(this.list.addAllAtIndex(1));
        Assert.assertTrue(this.list.addAll(false, true, false));
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true, false, true, false), this.list);
        Assert.assertTrue(this.list.addAllAtIndex(4, true, true));
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true, false, true, true, true, false), this.list);
    }

    @Test
    public void addAllIterable()
    {
        Assert.assertFalse(this.list.addAll(new BooleanArrayList()));
        Assert.assertFalse(this.list.addAllAtIndex(1));
        Assert.assertTrue(this.list.addAll(BooleanArrayList.newListWith(false, true, false)));
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true, false, true, false), this.list);
        Assert.assertTrue(this.list.addAllAtIndex(4, BooleanArrayList.newListWith(true, true)));
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true, false, true, true, true, false), this.list);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addAll_throws_index_negative()
    {
        this.list.addAllAtIndex(-1, true, true);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addAll_throws_index_greater_than_size()
    {
        this.list.addAllAtIndex(5, true, true);
    }

    @Test
    public void remove()
    {
        Assert.assertTrue(this.list.remove(false));
        Assert.assertFalse(this.list.remove(false));
        Assert.assertTrue(this.list.remove(true));
        Assert.assertEquals(BooleanArrayList.newListWith(true), this.list);
    }

    @Test
    public void removeAll()
    {
        Assert.assertFalse(this.list.removeAll());
        Assert.assertTrue(this.list.removeAll(true));
        Assert.assertEquals(BooleanArrayList.newListWith(false, true), this.list);
        Assert.assertTrue(this.list.removeAll(false, true));
        Assert.assertEquals(new BooleanArrayList(), this.list);
    }

    @Test
    public void removeAllIterable()
    {
        Assert.assertFalse(this.list.removeAll(new BooleanArrayList()));
        Assert.assertTrue(this.list.removeAll(BooleanArrayList.newListWith(false)));
        Assert.assertEquals(BooleanArrayList.newListWith(true, true), this.list);
        Assert.assertTrue(this.list.removeAll(BooleanArrayList.newListWith(true, true)));
        Assert.assertEquals(new BooleanArrayList(), this.list);
    }

    @Test
    public void removeAtIndex()
    {
        this.list.removeAtIndex(1);
        Assert.assertEquals(BooleanArrayList.newListWith(true, true), this.list);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeAtIndex_throws_index_greater_than_size()
    {
        MutableBooleanList emptyList = new SynchronizedBooleanList(new BooleanArrayList());
        emptyList.removeAtIndex(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeAtIndex_throws_index_negative()
    {
        this.list.removeAtIndex(-1);
    }

    @Test
    public void set()
    {
        this.list.set(1, true);
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, true), this.list);
    }

    @Test
    public void with()
    {
        MutableBooleanList arrayList = new SynchronizedBooleanList(new BooleanArrayList()).with(true);
        Assert.assertEquals(BooleanArrayList.newListWith(true), arrayList);
    }

    @Test
    public void withAll()
    {
        MutableBooleanList arrayList = new SynchronizedBooleanList(new BooleanArrayList().withAll(BooleanArrayList.newListWith(true)));
        MutableBooleanList arrayList0 = new SynchronizedBooleanList(new BooleanArrayList().withAll(BooleanArrayList.newListWith(true, false)));
        MutableBooleanList arrayList1 = new SynchronizedBooleanList(new BooleanArrayList().withAll(BooleanArrayList.newListWith(true, false, true)));
        MutableBooleanList arrayList2 = new SynchronizedBooleanList(new BooleanArrayList().withAll(BooleanArrayList.newListWith(true, false, true, false)));
        MutableBooleanList arrayList3 = new SynchronizedBooleanList(new BooleanArrayList().withAll(BooleanArrayList.newListWith(true, false, true, false, true)));
        Assert.assertEquals(BooleanArrayList.newListWith(true), arrayList);
        Assert.assertEquals(BooleanArrayList.newListWith(true, false), arrayList0);
        Assert.assertEquals(this.list, arrayList1);
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true, false), arrayList2);
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true, false, true), arrayList3);
    }

    @Test
    public void without()
    {
        MutableBooleanList mainArrayList = new SynchronizedBooleanList(new BooleanArrayList(true, false, true, false, true));
        Assert.assertEquals(BooleanArrayList.newListWith(false, true, false, true), mainArrayList.without(true));
    }

    @Test
    public void withoutAll()
    {
        MutableBooleanList mainArrayList = new SynchronizedBooleanList(new BooleanArrayList(true, false, true, false, true));
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, true), mainArrayList.withoutAll(BooleanArrayList.newListWith(false, false)));
    }

    @Test
    public void iterator()
    {
        BooleanIterator iterator = this.list.booleanIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void iterator_throws()
    {
        BooleanIterator iterator = this.list.booleanIterator();
        while (iterator.hasNext())
        {
            iterator.next();
        }

        iterator.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void iterator_throws_non_empty_list()
    {
        MutableBooleanList arrayList = new SynchronizedBooleanList(new BooleanArrayList());
        arrayList.add(true);
        arrayList.add(true);
        arrayList.add(true);
        BooleanIterator iterator = arrayList.booleanIterator();
        while (iterator.hasNext())
        {
            iterator.next();
        }
        iterator.next();
    }

    @Test
    public void forEach()
    {
        final long[] sum = new long[1];
        this.list.forEach(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                sum[0] += each ? 1 : 0;
            }
        });

        Assert.assertEquals(2L, sum[0]);
    }

    @Test
    public void size()
    {
        Verify.assertSize(0, new SynchronizedBooleanList(new BooleanArrayList()));
        Verify.assertSize(3, this.list);
    }

    @Test
    public void empty()
    {
        Assert.assertTrue(this.list.notEmpty());
        Verify.assertNotEmpty(this.list);
    }

    @Test
    public void count()
    {
        Assert.assertEquals(2L, new SynchronizedBooleanList(BooleanArrayList.newListWith(true, false, true)).count(BooleanPredicates.isTrue()));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertTrue(new SynchronizedBooleanList(BooleanArrayList.newListWith(true)).anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(new SynchronizedBooleanList(BooleanArrayList.newListWith(false)).anySatisfy(BooleanPredicates.isTrue()));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertFalse(new SynchronizedBooleanList(BooleanArrayList.newListWith(true, false)).allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(new SynchronizedBooleanList(BooleanArrayList.newListWith(true, true, true)).allSatisfy(BooleanPredicates.isTrue()));
    }

    @Test
    public void noneSatisfy()
    {
        Assert.assertTrue(new SynchronizedBooleanList(BooleanArrayList.newListWith(true, true)).noneSatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(new SynchronizedBooleanList(BooleanArrayList.newListWith(true, true)).noneSatisfy(BooleanPredicates.isTrue()));
    }

    @Test
    public void select()
    {
        Verify.assertSize(2, this.list.select(BooleanPredicates.isTrue()));
        Verify.assertSize(1, this.list.select(BooleanPredicates.isFalse()));
    }

    @Test
    public void reject()
    {
        Verify.assertSize(1, this.list.reject(BooleanPredicates.isTrue()));
        Verify.assertSize(2, this.list.reject(BooleanPredicates.isFalse()));
    }

    @Test
    public void detectIfNone()
    {
        Assert.assertFalse(this.list.detectIfNone(BooleanPredicates.isFalse(), true));
        Assert.assertTrue(this.list.detectIfNone(BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.isFalse()), true));
    }

    @Test
    public void collect()
    {
        Assert.assertEquals(FastList.newListWith(1, 0, 1), this.list.collect(new BooleanToObjectFunction<Object>()
        {
            public Object valueOf(boolean value)
            {
                return Integer.valueOf(value ? 1 : 0);
            }
        }));
    }

    @Test
    public void toArray()
    {
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true, false),
                BooleanArrayList.newListWith(new SynchronizedBooleanList(BooleanArrayList.newListWith(true, false, true, false)).toArray()));

    }

    @Test
    public void reverseThis()
    {
        Assert.assertEquals(new BooleanArrayList(), new SynchronizedBooleanList(new BooleanArrayList()).reverseThis());
        MutableBooleanList emptyList = new SynchronizedBooleanList(new BooleanArrayList());
        Assert.assertSame(emptyList, emptyList.reverseThis());
        Assert.assertEquals(BooleanArrayList.newListWith(true), new SynchronizedBooleanList(BooleanArrayList.newListWith(true)).reverseThis());
        Assert.assertEquals(BooleanArrayList.newListWith(true, false), new SynchronizedBooleanList(BooleanArrayList.newListWith(false, true)).reverseThis());
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, false, false), new SynchronizedBooleanList(BooleanArrayList.newListWith(false, false, true, true)).reverseThis());
        MutableBooleanList sameList = new SynchronizedBooleanList(BooleanArrayList.newListWith(true, false, true, false));
        Assert.assertSame(sameList, sameList.reverseThis());
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, false, false, true),
                new SynchronizedBooleanList(BooleanArrayList.newListWith(true, false, false, true, true)).reverseThis());
    }

    @Test
    public void toReversed()
    {
        Assert.assertEquals(new BooleanArrayList(), new SynchronizedBooleanList(new BooleanArrayList()).toReversed());
        MutableBooleanList emptyList = new SynchronizedBooleanList(new BooleanArrayList());
        Assert.assertNotSame(emptyList, emptyList.toReversed());
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, false, false),
                new SynchronizedBooleanList(BooleanArrayList.newListWith(false, false, true, true)).toReversed());
        MutableBooleanList evenList = new SynchronizedBooleanList(BooleanArrayList.newListWith(true, false, true, false));
        Assert.assertNotSame(evenList, evenList.toReversed());
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true, false, false),
                new SynchronizedBooleanList(BooleanArrayList.newListWith(false, false, true, false, true)).toReversed());
    }

    @Test
    public void testEquals()
    {
        MutableBooleanList list1 = new SynchronizedBooleanList(BooleanArrayList.newListWith(true, false, true, false));
        MutableBooleanList list2 = new SynchronizedBooleanList(BooleanArrayList.newListWith(true, false, true, false));
        MutableBooleanList list3 = new SynchronizedBooleanList(BooleanArrayList.newListWith(false, true, false, true));
        MutableBooleanList list4 = new SynchronizedBooleanList(BooleanArrayList.newListWith(false, false, true, true));
        MutableBooleanList list5 = new SynchronizedBooleanList(BooleanArrayList.newListWith(true, true, true));

        Verify.assertEqualsAndHashCode(list1, list2);
        Verify.assertPostSerializedEqualsAndHashCode(list1);
        Assert.assertNotEquals(list1, list3);
        Assert.assertNotEquals(list1, list4);
        Assert.assertNotEquals(list1, list5);
    }

    @Test
    public void testHashCode()
    {
        Assert.assertEquals(FastList.newListWith(true, false, true).hashCode(),
                new SynchronizedBooleanList(BooleanArrayList.newListWith(true, false, true)).hashCode());
        Assert.assertEquals(FastList.newList().hashCode(), new SynchronizedBooleanList(new BooleanArrayList()).hashCode());
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals("[true, false, true]", this.list.toString());
        Assert.assertEquals("[]", new SynchronizedBooleanList(new BooleanArrayList()).toString());
    }

    @Test
    public void makeString()
    {
        Assert.assertEquals("true, false, true", this.list.makeString());
        Assert.assertEquals("true", new SynchronizedBooleanList(BooleanArrayList.newListWith(true)).makeString("/"));
        Assert.assertEquals("true/false/true", this.list.makeString("/"));
        Assert.assertEquals(this.list.toString(), this.list.makeString("[", ", ", "]"));
        Assert.assertEquals("", new SynchronizedBooleanList(new BooleanArrayList()).makeString());
    }

    @Test
    public void appendString()
    {
        StringBuilder appendable = new StringBuilder();
        new BooleanArrayList().appendString(appendable);
        Assert.assertEquals("", appendable.toString());
        StringBuilder appendable2 = new StringBuilder();
        this.list.appendString(appendable2);
        Assert.assertEquals("true, false, true", appendable2.toString());
        StringBuilder appendable3 = new StringBuilder();
        this.list.appendString(appendable3, "/");
        Assert.assertEquals("true/false/true", appendable3.toString());
        StringBuilder appendable4 = new StringBuilder();
        this.list.appendString(appendable4, "[", ", ", "]");
        Assert.assertEquals(this.list.toString(), appendable4.toString());
    }

    @Test
    public void toList()
    {
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true), this.list.toList());
    }

    @Test
    public void toSet()
    {
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false, true), this.list.toSet());
    }

    @Test
    public void toBag()
    {
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true), this.list.toBag());
    }

    @Test
    public void asLazy()
    {
        Assert.assertEquals(this.list.toList(), this.list.asLazy().toList());
    }
}
