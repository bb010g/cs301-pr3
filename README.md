Usage Instructions
===

To run a program, you can use `./run`, the jar distribution, or compile a
standalone pre-processed source. `./run` is the simpler option: simply run it
with any desired arguments. This will be slow, however, as it is calling
`./gradlew run` behind the scenes and have the overhead of spinning up Gradle &
(re)compiling if necessary.

To avoid this slowdown, you can use the distribution archive & included jars.
Simply unpack gbuild/distributions/pr2-1.0.tar to your desired location,
`cd` in, and run the script at `bin/pr2` (appending `.bat` if on Windows).

Alternatively, you can compile the source provided in `normal-src`. Note that
this will not work with any tests due to the package hierarchy being flattened.
Also, the source will likely be less readable due to the Delombok process (extra
annotations, possible fully qualified type references, lots of boilerplate).
