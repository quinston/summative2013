all:
	dia -e uml.pdf uml.dia
	pdftk uml.pdf cat 1-endR output uml2.pdf
	rm uml.pdf
	mv uml2.pdf uml.pdf
