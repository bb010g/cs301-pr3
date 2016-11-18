Usage Instructions
===

To run a program, you can use `./run`, the jar distribution, or compile a
standalone pre-processed source. `./run` is the simpler option, if available:
simply run it with any desired arguments. This will be slow, however, as it is
calling `./gradlew run` behind the scenes and have the overhead of spinning up
Gradle & (re)compiling if necessary. Also, this requires your compiler to
support Java 8. However, the compiled `.class`es will run from Java 7 down to
Java 5 thanks to [Retrolambda](https://github.com/orfjackal/retrolambda) and
evant's [Gradle plugin](https://github.com/evant/gradle-retrolambda).

To avoid this slowdown and possible compiler inconvenience, you can use the
included distribution archive. Simply unpack `pr3-1.0.tar` (located under
`gbuild/distributions` if compiling) to your desired location, `cd` in, and run
the script at `bin/pr2` (appending `.bat` if on Windows). This is the preferred
method of using the program on systems without Java 8.

Alternatively, you can compile the source provided in `normal-src`. Note that
this will not work with any tests due to the package hierarchy being flattened.
Also, the source will likely be less readable due to the Delombok process
(extra annotations, possible fully qualified type references, lots of
boilerplate). Also note that this requires a Java 8 compiler and the
[streamsupport](https://github.com/streamsupport/streamsupport) JARs on your
classpath, unless you change the relevant `java8` includes to the equivalent
actual Java 8 packages and modify some calls that use the streamsupport utility
classes (e.g. `Maps`, `Spliterators`, `RefStreams`) to use their equivalent
Java 8 default instance methods.

If you want to review the code from an IDE, Eclipse support is already included
in the build file, and IntelliJ IDEA support isn't much harder. To use Eclipse,
simply run `./gradlew eclipse` to generate the project files and import it as a
Gradle project into Eclipse. (If this is your first time using Lombok 1.16.10
inside Eclipse, you will also have to install it. This can be done by running
`./gradlew installLombok` and following the installer that appears.) To use
IntelliJ, run the tasks listed
[here](https://docs.gradle.org/current/userguide/idea_plugin.html#sec:idea_tasks)
(`./gradlew <task>`) as needed. (If this is your first time using Lombok inside
IntelliJ, you can use
[this](https://github.com/mplushnikov/lombok-intellij-plugin) plugin. For a
simple installation, under `Settings` > `Browse Repositories...` search for
lombok and install mplushnikov's.) Alternatively, if you don't want to setup
Lombok in either of these IDEs, you can copy and view the source under
`gbuild/delombok/`, but be aware that will require you to manually obtain and
put streamsupport on the project's classpath.

(To read these instructions again, find them in `README.md`.)
