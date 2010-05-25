/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.bcel;

public class NanoTimer {

	private long time = 0;

	public NanoTimer start() {
		time -= System.nanoTime();
		return this;
	}

	public void stop() {
		time += System.nanoTime();
	}

	public void subtract(NanoTimer o) {
		time -= o.time;
	}

	public void reset() {
		time = 0;
	}

	/**
	 * May ony be called after stop has been called as many times as start.
	 */
	@Override
    public String toString() {
		return ((double) (time) / 1000000000) + " s";
	}



}
