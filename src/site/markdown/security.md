<!--
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

   https://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
-->

# Apache Commons Security

## About Security

For information about reporting or asking questions about security, please see [Apache Commons Security](https://commons.apache.org/security.html).

This page lists all security vulnerabilities fixed in released versions of this component.

Please note that binary patches are never provided. If you need to apply a source code patch, use the building instructions for the component version that you are using.

If you need help on building this component or other help on following the instructions to mitigate the known vulnerabilities listed here, please send your questions to the public [user mailing list](mail-lists.html).

If you have encountered an unlisted security vulnerability or other unexpected behavior that has security impact, or if the descriptions here are incomplete, please report them privately to the Apache Security Team. Thank you.

## Security Model

The [Apache Commons security model](https://commons.apache.org/security.html#Security_Model) specifies that it is unsafe to pass possibly malicious input to Commons libraries unless otherwise specified. For Commons BCEL, processing untrusted class data is supported to the extent that this should never allow the supplier of the data to trigger arbitrary code execution, filesystem or network access. It may still trigger other crashes, such as for example `StackOverflowError` or `OutOfMemoryError`: if your code uses BCEL to process untrusted input then it is up to you to compensate for that as necessary. Loading or executing the generated classes is unsafe and may cause unexpected behavior, including execute arbitrary code execution.

## Security Vulnerabilities Fixed in 6.13.0

### CVE-2026-105111

- CVE-2026-105111: Apache Commons BCEL `Class2HTML` emits unescaped class strings, enabling stored cross-site scripting (XSS).
- Severity: Low
- CWE-ID: CWE-79
- Vendor: The Apache Software Foundation
- Versions Affected: Apache Commons BCEL before 6.13.0.
- Description: When `Class2HTML` is used to generate web pages for possibly attacker-controlled class files, its HTML emitters write class-file strings into HTML without escaping them. This enables stored XSS in the generated reports.
- Mitigation: Users are recommended to upgrade to version 6.13.0 or later, which fixes the issue.
- Credit: Found by The Apache Software Foundation using Claude Security.
- Reference: [Apache security advisory](https://lists.apache.org/thread.html/d87nxx7nb5bombqggxhxo9lz16nwtsf9)
- Reference: [Fix commit](https://github.com/apache/commons-bcel/commit/fb72c225cbc6ec3d94060ed6edb269f07428d504)

### CVE-2026-94114 Fixed

- CVE-2026-94114: Apache Commons BCEL class repositories cache class files under their self-declared names, enabling cache poisoning.
- Severity: Important
- CWE-ID: CWE-386
- Vendor: The Apache Software Foundation
- Versions Affected: Apache Commons BCEL before 6.13.0.
- Description: BCEL caches attacker-controlled classes under their self-declared names without validating the requested name, allowing subsequent lookups and name-keyed verification results to refer to a different class.
- Mitigation: Users are recommended to upgrade to version 6.13.0 or later, which fixes the issue.
- Credit: Found by The Apache Software Foundation using Claude Security.
- Reference: [Apache security advisory](https://lists.apache.org/thread.html/co1wfk2lyrmpnfhn49o370pvfl978rw6)
- Reference: [Fix commit](https://github.com/apache/commons-bcel/commit/14890bf2b9014df25f9b4de86f29b5e917e5656b)

## Security Vulnerabilities Fixed in 6.6.0
### CVE-2022-42920

- CVE-2022-42920: Apache Commons BCEL prior to 6.6.0 allows producing arbitrary bytecode via out-of-bounds writing.
- Severity: Critical
- CWE-ID: CWE-787
- Vendor: The Apache Software Foundation
- Versions Affected: Apache Commons BCEL before 6.6.0.
- Description: Apache Commons BCEL has a number of APIs that would normally only allow changing specific class characteristics. However, due to an out-of-bounds writing issue, these APIs can be used to produce arbitrary bytecode. This could be abused in applications that pass attacker-controllable data to those APIs, giving the attacker more control over the resulting bytecode than otherwise expected. Update to Apache Commons BCEL 6.6.0.
- Mitigation: Users are recommended to upgrade to version 6.6.0 or later, which fixes the issue.
- Credit: Reported by Felix Wilhelm (Google)
- Credit: GitHub pull request to Apache Commons BCEL #147 by Richard Atkins (https://github.com/rjatkins)
- Credit: PR derived from OpenJDK (https://github.com/openjdk/jdk11u/) commit 13bf52c8d876528a43be7cb77a1f452d29a21492 by Aleksei Voitylov and RealCLanger (Christoph Langer https://github.com/RealCLanger)

## Safe Deserialization

For information about safe deserialization, please see [Safe Deserialization](https://commons.apache.org/io/description.html#Safe_Deserialization).
