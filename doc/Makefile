
LATEX_FLAGS = -shell-escape -interaction=nonstopmode -file-line-error
LATEX = pdflatex $(LATEX_FLAGS)
BIBTEX = bibtex

DOCS = techniczna.pdf uzytkownika.pdf procesowa.pdf przewodnik.pdf

DIAGRAMS = $(wildcard uml/*.uxf)
UML_PDFS = $(DIAGRAMS:.uxf=.pdf)

TEX_FILES = $(wildcard *.tex)



all: check_umlet $(DOCS)

$(DOCS): %.pdf: %.tex $(TEX_FILES) $(UML_PDFS)
	$(LATEX) $<
	if [ -f $*.bib ]; then \
		$(BIBTEX) $*.aux; \
		$(LATEX) $<;      \
		$(LATEX) $<;     \
	fi
	while ( grep -qs 'Label(s) may have changed' $*.log ); do \
		$(LATEX) $<; done

uml/%.pdf: uml/%.uxf
	$(UMLET) -action=convert -format=pdf -filename=$<

.PHONY: clean check_umlet

check_umlet:
ifndef UMLET
	$(error UMLET is undefined)
endif

clean:
	rm -rf *.aux *.out *.log *.pdf uml/*.pdf *.toc *.pyg *.bbl *.blg
