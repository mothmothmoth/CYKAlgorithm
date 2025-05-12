# CYKAlgorithm
Note: We coded this project in IntelliJ, so using IntelliJ to test/evaluate it is ideal, otherwise JDK setup may be needed for VS code or eclipse.
## To run the program
1. Unzip the folder. 
2. Make sure the "grammar" file is in the root of the working directory, otherwise in main replace the file path entry in the GrammarCNF.ImportGrammar() with full path of the file.
3. Write the grammar for the chosen Context free Grammar in Chomsky Normal Form (see appendix) inside the grammar file.
4. Navigate back to the main file and hit RUN.
5. The terminal will prompt the user to enter a string that they want to check if it exists in the language. Entering the string like 'aaba' will continue and finish the execution.
6. The step by step CYK algorithm will be shown in the console complete with visualization of changes to the matrix and a final verdict : Whether the string is accepted or not by the specified grammar.

## Appendix
Chomsky Normal Form: 
- Must be context free grammar.
- Only one non-terminal on left hand side of each rule.
- Either only two non-terminals or one terminal on the right hand side of each rule.