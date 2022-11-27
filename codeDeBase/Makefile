
blank:
	@echo -e "\nPossible options:\n\tcompile\n\trun\n\tclean\n\tfresh"

compile:
	javac -d ./out src/*.java src/VueControleur/*.java src/modele/deplacements/*.java src/modele/plateau/*.java

run:
	java -cp out Main

clean:
	rm -vfr ./out/*

fresh:
	rm -vfr ./out/*
	javac -d ./out src/*.java src/VueControleur/*.java src/modele/deplacements/*.java src/modele/plateau/*.java
	java -cp out Main
