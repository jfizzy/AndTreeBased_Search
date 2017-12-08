# And-Tree Based Search
Repo for CPSC 433 - Artificial Intelligence - And-Tree Based Search

## ARGUMENTS

Program accepts 1 required argument and two optional sections of 4 additional arguments 

Examples of acceptable program argument strings (assume jar is search.jar):

java -jar search.jar file.txt
java -jar search.jar file.txt 1 1 1 1
java -jar search.jar file.txt 1 1 1 1 1 1 0.5 0.5

### Required Argument

input file name (example: test.txt)

### Optional Argument Section (Penalties)

each specified as an integer. Must include all or none. This is their order of specification:

pen_coursemin 
pen_labmin 
pen_notpaired 
pen_section

(example: 1 1 1 1)

### Optional Argument Section (Weights)

each specified as a double. Must include Penalties and then these. This is their order of specification:

w_min 
w_pref 
w_pair 
w_secdiff

(example: 1 1 0.5 0.5)



## USAGE - command line

<jar name> <argmuents>
