<!---
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->
# Test fixtures and local builds

Run the default Maven verification from the project root:

```sh
mvn -B -ntp
```

Use a JDK, and set `JAVA_HOME` and `PATH` to the same JDK so subprocess tests use
matching `java`, `javac`, and `javap` executables. The Maven build targets Java 8
class files; its compiler configuration includes debug information needed by
local-variable and line-number tests.

## IDE and Maven output directories

Eclipse and Maven can both write to `target/classes` and `target/test-classes`.
Stop Eclipse auto-build and wait for an active build to finish before running
Maven in the same checkout, or use a separate checkout or output directories.
After switching compilers, use a clean Maven build rather than mixing classes
from both builds.

Keep `src/test/resources` out of Java source compilation. The checked-in Eclipse
`.classpath` already excludes it with `excluding="**"`. Do not add the `.java`
files in that directory to an IDE source folder: some are reference sources for
compiled fixtures, not sources that should be rebuilt during ordinary tests.
The Eclipse project also compiles `src/examples`; avoid concurrent writes from
that source folder while Maven runs.

## Source fixtures

Prefer assertions about the behavior being tested over assumptions about a
compiler's method order, bytecode offsets, or local-variable slots. Select
methods and variables by name when their layout is incidental to the test.
Compile source fixtures with debug information (`javac -g` or ECJ `-g`) when a
test examines local-variable tables.

`PLSETest` constructs and parses an enum class-file fixture containing the array
`clone()` invocation for BCEL-262, so it does not depend on a compiler's
implementation of enum `values()`. Its BCEL-361 cases serialize and read explicit
line-number records around the signed-short boundary and parse the original
12-level nested try/finally fixture from a checked-in javac-generated class.
This preserves its large constructor, branches, exception handlers, and debug
attributes regardless of the compiler used to build the test suite. The test
Javadocs show the Java patterns represented by these fixtures. BCEL-295 finds
the loop variable by name and compares its live range after a `MethodGen` round
trip.

## Checked-in class files

Treat the `.class` files under `src/test/resources` as test inputs. Maven copies
them as resources; some tests also read them directly from the source tree.
Do not regenerate all of them with the current compiler. Compiler version,
options, and bytecode layout can be part of the case a fixture is testing.
A class file does not contain a standard compiler-identity field.

When intentionally changing a compiled fixture, retain its reference source and
record the JDK/compiler version and exact command. Compile into a separate
scratch directory, inspect the result with `javap -v -p`, and copy back only the
intended `.class` files. Run the affected tests and the full Maven verification.
Review `git diff --stat` and `git diff --numstat` for unintended binary changes.

For example, the existing `typeannotations/TypeAnnotated.class` fixture can be
reproduced with OpenJDK `javac 21.0.13.1` using the following commands from the project
root (create a fresh scratch directory first):

```sh
javac --release 8 -d /path/to/scratch src/test/resources/typeannotations/TypeAnnotated.java
javap -v -p /path/to/scratch/typeannotations/TypeAnnotated.class
```

Only `TypeAnnotated.class` is consumed by these tests; the generated annotation
classes do not need to be copied back. Other fixtures can require different
compiler versions or options. `--release 8` sets the target platform, not a fixed
javac implementation or an identical bytecode layout across compiler versions.

### Deeply nested try/finally fixture

`issue361/LargeMethod.java` retains the original twelve-level nested try/finally
source unchanged. `issue361/LargeMethod.jar` contains its complete class file,
compiled with OpenJDK `javac 21.0.13.1`. The constructor has 64,507 bytes of code,
8,188 exception handlers, and line-number offsets beyond 32,767. The class also
retains its local-variable and stack-map tables. Compression keeps this 6.6 MB
class file in a roughly 157 KB fixture without removing bytecode or attributes.

Reproduce it from the project root with that JDK's `javac` and `jar` (using a
fresh scratch directory):

```sh
javac --release 8 -g -d /path/to/scratch src/test/resources/issue361/LargeMethod.java
TZ=UTC jar --create --file /path/to/scratch/LargeMethod.jar --no-manifest --date=2000-01-01T00:00:00Z -C /path/to/scratch org/apache/bcel/data/LargeMethod.class
javap -v -p /path/to/scratch/org/apache/bcel/data/LargeMethod.class
```

The fixed archive timestamp and omitted manifest make the archive reproducible.
Only replace `src/test/resources/issue361/LargeMethod.jar` after reviewing the
compiled structure and running `PLSETest`. Its size and handler-count assertions
are intentional: a compiler that simplifies the nested finally blocks must not
silently reduce this integration test's coverage.
