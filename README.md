42cc-test-jetmind
=================

42cc android developer test

Setup
=====

This project uses maven.

Run tests:

```bash
$ mvn clean test
```

Build apk:

```bash
$ mvn clean install
```

Deploy to connected device:

```bash
$ mvn clean install android:deploy
```

You can import project into Eclipse using m2eclipse plugin.
