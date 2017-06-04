cmake_minimum_required(VERSION 3.3)
project(implementacao_c)

include_directories("include")

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES
    include/evolutionary.h
    include/functions.h
    include/genetic_algorithm.h
    src/evolutionary.cpp
    src/functions.cpp
    src/genetic_algorithm.cpp
    main.cpp)

add_executable(implementacao_c ${SOURCE_FILES})