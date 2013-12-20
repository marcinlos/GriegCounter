
LATEX_FLAGS = -shell-escape -interaction=nonstopmode -file-line-error
LATEX = pdflatex $(LATEX_FLAGS)
MAIN = documentation.tex

DIAGRAMS = $(wildcard uml/*.uxf)
UML_PDFS = $(DIAGRAMS:.uxf=.pdf)

TEX_FILES = $(wildcard *.tex)

all: check_umlet documentation.pdf

documentation.pdf: $(TEX_FILES) $(UML_PDFS)
	$(LATEX) $(MAIN)
	while ( grep -qs 'Label(s) may have changed' $(MAIN:.tex=.log) ); do \
		$(LATEX) $(MAIN); done

uml/%.pdf: uml/%.uxf
	$(UMLET) -action=convert -format=pdf -filename=$<

.PHONY: clean check_umlet

check_umlet:
ifndef UMLET
	$(error UMLET is undefined)
endif

clean:
	rm -rf *.aux *.out *.log *.pdf uml/*.pdf
