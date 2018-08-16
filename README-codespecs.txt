This is a version of Apache BCEL that contains local (PLSE) fixes to the release
version of Apache BCEL.  It is then used as an upstream master for the annotated
version of BCEL in https://github.com/typetools/commons-bcel.  It is this
later version that is released as part of Daikon via bcel-util-<version>-all.jar.

If there are no local fixes needed, this repository is not needed/used for a Daikon release;
https://github/typetools/commons-bcel can pull directly from https://github.com/apache/commons-bcel.


To build this project
---------------------

```
mvn verify
```

The `.jar` file is found at, for example, `target/bcel-6.2.0.1.jar`.


To update to a newer version of the upstream library
----------------------------------------------------

At https://github.com/apache/commons-bcel/releases ,
find the commit corresponding to a public release.

Pull in that commit:
git pull https://github.com/apache/commons-bcel <commitid>

Resolve any conflicts with our local changes and rebuild.

