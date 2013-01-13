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

### Installing Facebook SDK apklib

* Download facebook android sdk.

* Install it to your local maven repo:

```bash
$ cd /path/to/your/facebook-android-sdk-3.0/facebook
$ ln -s /path/to/project/facebook_pom.xml pom.xml
$ mvn clean package install
```

That's all.

To get Eclipse autocompletion for facebook classes to work import facebook sdk project to your workspace and link it to your project (as explained in Facebook SDK installation guide).
