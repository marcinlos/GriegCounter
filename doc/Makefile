
LATEX_FLAGS = -shell-escape -interaction=nonstopmode -file-line-error
LATEX = pdflatex ${LATEX_FLAGS}
MAIN = documentation.tex

all: documentation.pdf

documentation.pdf: *.tex
	${LATEX} ${MAIN}

.PHONY: clean

clean:
	rm -rf *.aux *.out *.log *.pdf
