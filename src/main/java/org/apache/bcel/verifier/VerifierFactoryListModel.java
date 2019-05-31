/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.bcel.verifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * This class implements an adapter; it implements both a Swing ListModel and a VerifierFactoryObserver.
 *
 */
public class VerifierFactoryListModel implements VerifierFactoryObserver, javax.swing.ListModel<String> {

    private final List<ListDataListener> listeners = new ArrayList<>();
    private final Set<String> cache = new TreeSet<>();

    public VerifierFactoryListModel() {
        VerifierFactory.attach(this);
        update(null); // fill cache.
    }

    @Override
    public synchronized void update(final String s) {
        final Verifier[] verifiers = VerifierFactory.getVerifiers();
        final int num_of_verifiers = verifiers.length;
        cache.clear();
        for (final Verifier verifier : verifiers) {
            cache.add(verifier.getClassName());
        }
        for (final ListDataListener listener : listeners) {
            final ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, num_of_verifiers - 1);
            listener.contentsChanged(e);
        }
    }

    @Override
    public synchronized void addListDataListener(final ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public synchronized void removeListDataListener(final javax.swing.event.ListDataListener l) {
        listeners.remove(l);
    }

    @Override
    public synchronized int getSize() {
        return cache.size();
    }

    @Override
    public synchronized String getElementAt(final int index) {
        return cache.toArray(new String[cache.size()])[index];
    }

}
