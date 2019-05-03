# Eyal Styskin
# 206264749 
compile: bin
	find src | grep .java > out.txt
	javac -d bin -cp biuoop-1.4.jar @out.txt
	
jar:
	jar cfm space-invaders.jar manifest.mf -C bin . -C resources .
	
run:
	java -cp biuoop-1.4.jar:bin:resources gamelogic/Ass7Game
	
bin:
	mkdir bin