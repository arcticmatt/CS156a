# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.0

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list

# Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/local/Cellar/cmake/3.0.1/bin/cmake

# The command to remove a file.
RM = /usr/local/Cellar/cmake/3.0.1/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = "/Users/mattlim/Documents/Caltech 2014-2015/CS156a/code/Week6/armadillo-4.500.0"

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = "/Users/mattlim/Documents/Caltech 2014-2015/CS156a/code/Week6/armadillo-4.500.0"

# Include any dependencies generated for this target.
include CMakeFiles/armadillo.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/armadillo.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/armadillo.dir/flags.make

CMakeFiles/armadillo.dir/src/wrapper.cpp.o: CMakeFiles/armadillo.dir/flags.make
CMakeFiles/armadillo.dir/src/wrapper.cpp.o: src/wrapper.cpp
	$(CMAKE_COMMAND) -E cmake_progress_report "/Users/mattlim/Documents/Caltech 2014-2015/CS156a/code/Week6/armadillo-4.500.0/CMakeFiles" $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building CXX object CMakeFiles/armadillo.dir/src/wrapper.cpp.o"
	/usr/bin/c++   $(CXX_DEFINES) $(CXX_FLAGS) -o CMakeFiles/armadillo.dir/src/wrapper.cpp.o -c "/Users/mattlim/Documents/Caltech 2014-2015/CS156a/code/Week6/armadillo-4.500.0/src/wrapper.cpp"

CMakeFiles/armadillo.dir/src/wrapper.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/armadillo.dir/src/wrapper.cpp.i"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -E "/Users/mattlim/Documents/Caltech 2014-2015/CS156a/code/Week6/armadillo-4.500.0/src/wrapper.cpp" > CMakeFiles/armadillo.dir/src/wrapper.cpp.i

CMakeFiles/armadillo.dir/src/wrapper.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/armadillo.dir/src/wrapper.cpp.s"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -S "/Users/mattlim/Documents/Caltech 2014-2015/CS156a/code/Week6/armadillo-4.500.0/src/wrapper.cpp" -o CMakeFiles/armadillo.dir/src/wrapper.cpp.s

CMakeFiles/armadillo.dir/src/wrapper.cpp.o.requires:
.PHONY : CMakeFiles/armadillo.dir/src/wrapper.cpp.o.requires

CMakeFiles/armadillo.dir/src/wrapper.cpp.o.provides: CMakeFiles/armadillo.dir/src/wrapper.cpp.o.requires
	$(MAKE) -f CMakeFiles/armadillo.dir/build.make CMakeFiles/armadillo.dir/src/wrapper.cpp.o.provides.build
.PHONY : CMakeFiles/armadillo.dir/src/wrapper.cpp.o.provides

CMakeFiles/armadillo.dir/src/wrapper.cpp.o.provides.build: CMakeFiles/armadillo.dir/src/wrapper.cpp.o

# Object files for target armadillo
armadillo_OBJECTS = \
"CMakeFiles/armadillo.dir/src/wrapper.cpp.o"

# External object files for target armadillo
armadillo_EXTERNAL_OBJECTS =

libarmadillo.4.50.0.dylib: CMakeFiles/armadillo.dir/src/wrapper.cpp.o
libarmadillo.4.50.0.dylib: CMakeFiles/armadillo.dir/build.make
libarmadillo.4.50.0.dylib: CMakeFiles/armadillo.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking CXX shared library libarmadillo.dylib"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/armadillo.dir/link.txt --verbose=$(VERBOSE)
	$(CMAKE_COMMAND) -E cmake_symlink_library libarmadillo.4.50.0.dylib libarmadillo.4.dylib libarmadillo.dylib

libarmadillo.4.dylib: libarmadillo.4.50.0.dylib

libarmadillo.dylib: libarmadillo.4.50.0.dylib

# Rule to build all files generated by this target.
CMakeFiles/armadillo.dir/build: libarmadillo.dylib
.PHONY : CMakeFiles/armadillo.dir/build

CMakeFiles/armadillo.dir/requires: CMakeFiles/armadillo.dir/src/wrapper.cpp.o.requires
.PHONY : CMakeFiles/armadillo.dir/requires

CMakeFiles/armadillo.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/armadillo.dir/cmake_clean.cmake
.PHONY : CMakeFiles/armadillo.dir/clean

CMakeFiles/armadillo.dir/depend:
	cd "/Users/mattlim/Documents/Caltech 2014-2015/CS156a/code/Week6/armadillo-4.500.0" && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" "/Users/mattlim/Documents/Caltech 2014-2015/CS156a/code/Week6/armadillo-4.500.0" "/Users/mattlim/Documents/Caltech 2014-2015/CS156a/code/Week6/armadillo-4.500.0" "/Users/mattlim/Documents/Caltech 2014-2015/CS156a/code/Week6/armadillo-4.500.0" "/Users/mattlim/Documents/Caltech 2014-2015/CS156a/code/Week6/armadillo-4.500.0" "/Users/mattlim/Documents/Caltech 2014-2015/CS156a/code/Week6/armadillo-4.500.0/CMakeFiles/armadillo.dir/DependInfo.cmake" --color=$(COLOR)
.PHONY : CMakeFiles/armadillo.dir/depend

